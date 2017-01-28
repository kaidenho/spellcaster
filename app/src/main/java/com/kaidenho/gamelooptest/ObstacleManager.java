package com.kaidenho.gamelooptest;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Kaiden Ho on 2016-10-04.
 */
public class ObstacleManager extends ObjectManager{
    private static final String TAG = ObjectManager.class.getSimpleName();

    private static final int MIN_DISTANCE_BETWEEN_OBSTACLES = 400;
    private static final int MAX_DISTANCE_BETWEEN_OBSTACLES = 600;

    private float distanceSinceLastAddition = 0;
    private int distanceNextAddition = 0;
    private int mObstacleCounter = 0;

    private int previousLeft = 0;
    private boolean previousCenter = false;

    private Context mContext;

    private Scaling mScaling;


    public ObstacleManager(Context context) {
        mContext = context;
        mScaling = new Scaling(context);
    }

    @Override
    public void update(long timeDelta) {
        super.update(timeDelta);

        distanceSinceLastAddition -= BaseObject.scrollDistance;

        Random rand = new Random();
        int rand1;
        int rand2;

        if (distanceSinceLastAddition > distanceNextAddition) {
            // Range is 0 - 2
            rand1 = rand.nextInt(3);    //Excludes top value and includes 0
            while(previousLeft == rand1) {
                rand1 = rand.nextInt(3);
            }
            previousLeft = rand1;

            rand2 = rand.nextInt(2);    // 1 in 2 odds of double obstacle


            // Zero is at the top?
            Obstacle obstacle = new Obstacle(new RectF(
                    rand1 * 200,
                    mScaling.gameHeight + 200,
                    rand1 * 200 + 200,
                    mScaling.gameHeight
            ), "Obstacle" + mObstacleCounter);
            mObstacleCounter++;

            add(obstacle);

            if ((rand1 == 0 || rand1 == 2) && rand2 == 1) {
                if (previousCenter) {
                    if (rand1 == 0) {
                        Obstacle obstacle2 = new Obstacle(new RectF(
                                400,
                                mScaling.gameHeight + 200,
                                600,
                                mScaling.gameHeight
                        ), "Obstacle" + mObstacleCounter);
                        mObstacleCounter++;

                        add(obstacle2);
                    } else if (rand1 == 2) {
                        Obstacle obstacle2 = new Obstacle(new RectF(
                                0,
                                mScaling.gameHeight + 200,
                                200,
                                mScaling.gameHeight
                        ), "Obstacle" + mObstacleCounter);
                        mObstacleCounter++;

                        add(obstacle2);
                    }
                } else {
                    Obstacle obstacle2 = new Obstacle(new RectF(
                            200,
                            mScaling.gameHeight + 200,
                            400,
                            mScaling.gameHeight
                    ), "Obstacle" + mObstacleCounter);
                    mObstacleCounter++;

                    add(obstacle2);
                }
            }

            //Log.d(TAG, "Object added to " + this.getClass().getSimpleName() + ". New size is " + getSize());

            distanceSinceLastAddition = 0;
            distanceNextAddition = rand.nextInt(MAX_DISTANCE_BETWEEN_OBSTACLES - MIN_DISTANCE_BETWEEN_OBSTACLES) + MIN_DISTANCE_BETWEEN_OBSTACLES;
        }

        for (int i = 0; i < mObjects.size(); i++) {
            BaseObject.renderSystem.add((Obstacle)mObjects.get(i));
        }

        for (int i = 0; i < super.getSize(); i++) {
            Obstacle obstacle = (Obstacle)mObjects.get(i);
            if (obstacle.getLocationRect().top < 0) {
                mObjects.remove(i);
            }
        }
    }
}
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

    private static final int MIN_TIME_BETWEEN_OBSTACLES = 1000;
    private static final int MAX_TIME_BETWEEN_OBSTACLES = 3000;

    private long timeLastAddition = 0;
    private int timeNextAddition;
    private int mObstacleCounter = 0;


    private Context mContext;

    private Scaling mScaling;

    public ObstacleManager(Context context) {
        mContext = context;
        mScaling = new Scaling(context);
    }

    @Override
    public void update(long timeDelta) {
        super.update(timeDelta);

        Random rand = new Random();
        int column;
        int column2;
        boolean twoObstacles;

        if (SystemClock.uptimeMillis() - timeLastAddition > timeNextAddition) {
            // Range is 0 - 2
            column = rand.nextInt(3);   //Excludes top value and includes 0


            // Zero is at the top?
            Obstacle obstacle = new Obstacle(new RectF(
                    column * 200,
                    mScaling.gameHeight + 200,
                    column * 200 + 200,
                    mScaling.gameHeight
            ), "Obstacle" + mObstacleCounter);
            mObstacleCounter++;

            add(obstacle);

            //Log.d(TAG, "Object added to " + this.getClass().getSimpleName() + ". New size is " + getSize());

            timeLastAddition = SystemClock.uptimeMillis();
            timeNextAddition = rand.nextInt(MAX_TIME_BETWEEN_OBSTACLES - MIN_TIME_BETWEEN_OBSTACLES) + MIN_TIME_BETWEEN_OBSTACLES;
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
package com.kaidenho.gamelooptest;

import android.graphics.RectF;
import android.util.Log;

/**
 * A GameObject that scrolls and can be collided with
 *
 * Created by Kaiden Ho on 2016-10-04.
 */
public class Obstacle extends GameObject {
    private static final String TAG = Obstacle.class.getSimpleName();

    private static final int OBSTACLE_TEXTURE_INDEX = 1;

    private static final int OBJECT_HEALTH = 4;

    private int mDamageCounter = 0;

    //private static final int MOVEMENT_SPEED = -300;  // pixels per second, negative is down


    public Obstacle(RectF initialLocation, String name) {
        super(OBSTACLE_TEXTURE_INDEX, initialLocation, name);
    }

    @Override
    public void update(long timeDelta) {
        //float movementDistance = MOVEMENT_SPEED * timeDelta / 1000;


        getLocationRect().top += BaseObject.scrollDistance;
        getLocationRect().bottom += BaseObject.scrollDistance;


        BaseObject.renderSystem.add(this);
    }

    /**
     *
     * @param manager the manager this object belongs to
     * @param index this object's index in the manager
     * @return if the health was maxed out
     */
    public boolean checkDamage(ObjectManager manager, int index) {
        if (mDamageCounter > OBJECT_HEALTH) {
            manager.remove(index);
            return true;
        }
        return false;
    }

    public void addToDamageCounter(int damage) {
        mDamageCounter += damage;
    }
}

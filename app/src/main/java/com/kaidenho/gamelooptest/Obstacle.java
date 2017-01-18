package com.kaidenho.gamelooptest;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

/**
 * A GameObject that scrolls and can be collided with
 *
 * Created by Kaiden Ho on 2016-10-04.
 */
public class Obstacle extends GameObject {
    private static final int OBSTACLE_TEXTURE_INDEX = 1;

    private static final int MOVEMENT_SPEED = -300;  // pixels per second, negative is down

    public Obstacle(RectF initialLocation, String name) {
        super(OBSTACLE_TEXTURE_INDEX, initialLocation, name);
    }

    @Override
    public void update(long timeDelta) {
        float movementDistance = MOVEMENT_SPEED * timeDelta / 1000;

        getLocationRect().top += movementDistance;
        getLocationRect().bottom += movementDistance;

        BaseObject.renderSystem.add(this);
    }
}

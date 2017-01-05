package com.kaidenho.gamelooptest;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;

/**
 * A GameObject that scrolls and can be collided with
 *
 * Created by Kaiden Ho on 2016-10-04.
 */
public class Obstacle extends GameObject {
    private static final int OBSTACLE_TEXTURE_INDEX = 1;

    private static final int MOVEMENT_SPEED = -600;  // pixels per second, negative is down

    public Obstacle(Rect initialLocation, Context context, String name) {
        super(OBSTACLE_TEXTURE_INDEX, initialLocation, context, name);
    }

    @Override
    public void update(long timeDelta) {
        Rect oldLocation = getLocationRect();
        float movementDistance = MOVEMENT_SPEED * timeDelta / 1000;

        super.setLocationRect(new Rect(
                oldLocation.left,
                oldLocation.top + (int)(movementDistance),
                oldLocation.right,
                oldLocation.bottom + (int)(movementDistance)
        ));

        setVertexBuffer(updateLocation(getLocationRect()));

        BaseObject.renderSystem.add(this);
    }

    public Rect getLocationRect() {
        return super.getLocationRect();
    }


}

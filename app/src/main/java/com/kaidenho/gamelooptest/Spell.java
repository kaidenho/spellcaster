package com.kaidenho.gamelooptest;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by Kaiden Ho on 2016-11-22.
 */
public class Spell extends GameObject {
    private static final String TAG = Spell.class.getSimpleName();
    private int mDebugCounter = 0;
    private int m2DebugCounter = 0;

    private static final int MOVEMENT_SPEED = 600;  // pixels per second

    // Image Source constant
    private final static int SPELL_IMAGE_SOURCE = R.drawable.mage;

    private boolean mHasCollided = false;

    public Spell(Rect locationRect, Context context) {
        super(SPELL_IMAGE_SOURCE, locationRect, context, "Spell");
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

        mDebugCounter++;
        if (mDebugCounter > 20) {
            Log.d(TAG, "Spell " + m2DebugCounter + " added to drawQueue");
            m2DebugCounter++;
            mDebugCounter = 0;
        }
    }

    public int checkCollisions(ObjectManager collection) {
        for (int i = 0; i < collection.getSize(); i++) {
            if (collection.get(i) instanceof GameObject) {
                Rect locationRect = ((GameObject) collection.get(i)).getLocationRect();

                // This collision logic is column-based
                if (getLocationRect().left >= locationRect.left && getLocationRect().right <= locationRect.right) {
                    if (getLocationRect().top >= locationRect.bottom && getLocationRect().top <= locationRect.top) {
                        Log.d(TAG, "Object Collided with is " + i + " out of " + collection.getSize());
                        mHasCollided = true;
                        return i;
                    }
                }
            }
        }
        return -1;
    }
}

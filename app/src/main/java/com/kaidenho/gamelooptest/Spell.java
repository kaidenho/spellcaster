package com.kaidenho.gamelooptest;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

/**
 * Created by Kaiden Ho on 2016-11-22.
 */
public class Spell extends GameObject {
    private static final String TAG = Spell.class.getSimpleName();
    private int mDebugCounter = 0;
    private int m2DebugCounter = 0;

    private static final int MOVEMENT_SPEED = 400;  // pixels per second

    // Image index find in RenderSystem
    private final static int SPELL_TEXTURE_INDEX = 2;

    // sound index find in SoundSystem
    private final static int SPELL_SOUND_INDEX = 0;

    private boolean mHasCollided = false;

    public Spell(RectF locationRect) {
        super(SPELL_TEXTURE_INDEX, locationRect, "Spell");
    }

    @Override
    public void update(long timeDelta) {
        float movementDistance = timeDelta * MOVEMENT_SPEED / 1000;

        getLocationRect().top += movementDistance;
        getLocationRect().bottom += movementDistance;

        BaseObject.renderSystem.add(this);

        mDebugCounter++;
        if (mDebugCounter > 20) {
            m2DebugCounter++;
            mDebugCounter = 0;
        }
    }

    public void playSound() {
        BaseObject.soundSystem.playSound(SPELL_SOUND_INDEX);
    }

    public int checkCollisions(ObjectManager collection) {
        for (int i = 0; i < collection.getSize(); i++) {
            if (collection.get(i) instanceof GameObject) {
                RectF locationRect = ((GameObject) collection.get(i)).getLocationRect();

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

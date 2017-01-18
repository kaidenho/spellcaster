package com.kaidenho.gamelooptest;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

/**
 * A touch-interactable game object. Also, the player
 *
 * Created by Kaiden Ho on 2016-10-03.
 */
public class Player extends GameObject {
    private final static String TAG = GameObject.class.getSimpleName();

    // Image texture index, find in RenderSystem
    private final static int[] PLAYER_TEXTURE_INDEXES = {16, 17, 16, 18};

    private final static int SPRITE_INTERVAL = 150;

    private long mSpriteCounter = 0;
    private int mCurrentSprite = 0;

    // Collision variables
    private boolean mHasCollided = false;

    private int hasBeenAdded = 0;

    // Touch state variables
    private float originX;
    private float originY;
    private boolean movementSwipe = false;
    private static int swipeDistance = 150;     // 1/3 of the screen width. Always. See Scaling


    public Player (Context context, String name) {
        super(PLAYER_TEXTURE_INDEXES[0], new RectF(200, 200, 400, 0), name);
        Log.d(TAG,"Player created");
    }

    @Override
    public void update(long timeDelta) {
        // sprite feature
        if(mSpriteCounter > SPRITE_INTERVAL) {
            mCurrentSprite = (mCurrentSprite + 1) % PLAYER_TEXTURE_INDEXES.length;

            setTextureIndex(PLAYER_TEXTURE_INDEXES[mCurrentSprite]);

            mSpriteCounter = 0;
        }
        mSpriteCounter += timeDelta;

        //Log.d(TAG,"Player location = " + getLocationRect());
        super.update(timeDelta);
    }

    public void moveRight() {
        if (getLocationRect().right + 200 <= 600) {
            getLocationRect().left += 200;
            getLocationRect().right += 200;
        }
    }

    public void moveLeft() {
        if(getLocationRect().left - 200 >= 0) {
            getLocationRect().left -= 200;  // 200 equals 1/3 the scaled screen width
            getLocationRect().right -= 200;
        }
    }

    public void shoot() {
        BaseObject.gameSystem.getMagicManager().shoot();
    }

    public boolean checkCollisions(ObjectManager collection) {
        for (int i = 0; i < collection.getSize(); i++) {
            if (collection.get(i) instanceof GameObject) {
                RectF locationRect = ((GameObject) collection.get(i)).getLocationRect();

                // This collision logic is column-based
                if (locationRect.left >= getLocationRect().left && locationRect.right <= getLocationRect().right
                        && locationRect.bottom <= getLocationRect().top && locationRect.bottom >= getLocationRect().bottom) {
                        Log.d(TAG, "Object collided with is " + i + " out of " + collection.getSize()
                                + " Object collided with is " + ((GameObject)collection.mObjects.get(i)).getName());
                        Log.d(TAG, "Object collided with location = " + ((GameObject)collection.mObjects.get(i)).getLocationRect());
                    Log.d(TAG, "Current location = " + getLocationRect());
                        mHasCollided = true;
                        return true;
                }
            }
        }
        return false;
    }
}

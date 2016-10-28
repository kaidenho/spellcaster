package com.kaidenho.gamelooptest;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

/**
 * A touch-interactable game object. Also, the player
 *
 * Created by Kaiden Ho on 2016-10-03.
 */
public class Player extends GameObject {
    private final static String TAG = GameObject.class.getSimpleName();

    // Image Source constant
    private final static int PLAYER_IMAGE_SOURCE = R.drawable.mage;

    // Collision variables
    private boolean mHasCollided = false;

    // Touch state variables
    private float originX;
    private float originY;
    private boolean movementSwipe = false;
    private static int swipeDistance = 200;     // 1/3 of the screen width. Always. See Scaling


    public Player (Context context, String name) {
        super(PLAYER_IMAGE_SOURCE, new Rect(200, 200, 400, 0), context, name);

    }

    public void onTouch(MotionEvent event) {
        boolean changeLocation = false;
        float x = event.getX();
        // Inverting the y axis is preference, but I always think of 0 as being at the bottom
        float y = getContext().getResources().getDisplayMetrics().heightPixels - event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (within(x, y, getLocationRect())) {
                movementSwipe = true;
            }
            originX = x;
            originY = y;
        }

        // TODO: Make absolute values relative to screen size
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (y > getLocationRect().bottom && y < getLocationRect().top) {
                if (x < originX - swipeDistance) {
                    // Left Swipe
                    getLocationRect().left -= 200 * getScaling().gameUnit;  // 200 always equals 1/3 the screen width
                    getLocationRect().right -= 200 * getScaling().gameUnit;
                    changeLocation = true;
                }
                if (x > originX + swipeDistance) {
                    // Right Swipe
                    getLocationRect().left += 200 * getScaling().gameUnit;
                    getLocationRect().right += 200 * getScaling().gameUnit;
                    changeLocation = true;
                }
            }
        }

        if (changeLocation) {
            setVertexBuffer(updateLocation(getLocationRect()));
        }
    }

    public boolean checkCollisions(ObjectManager collection) {
        for (int i = 0; i < collection.getSize(); i++) {
            if (collection.get(i) instanceof GameObject) {
                Rect locationRect = ((GameObject) collection.get(i)).getLocationRect();

                // This collision logic is column-based
                if (locationRect.left >= getLocationRect().left && locationRect.right <= getLocationRect().right) {
                    if ((locationRect.top >= getLocationRect().bottom && locationRect.top <= getLocationRect().top)
                            || (locationRect.bottom <= getLocationRect().top && locationRect.bottom >= getLocationRect().bottom)) {
                        Log.d(TAG, "Object Collided with is " + i + " out of " + collection.getSize());
                        mHasCollided = true;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean within(float x, float y, Rect rect) {
        Log.d("Within", "x " + x + ", y " + y + ", left " + rect.left + ", right " + rect.right + ", bottom " + rect.bottom + ", top " + rect.top);
        if (x < rect.left || x > rect.right || y < rect.bottom || y > rect.top) {
            return false;
        }
        return true;
    }
}

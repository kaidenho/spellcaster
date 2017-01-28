package com.kaidenho.gamelooptest;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

/**
 * Created by Kaiden Ho on 2017-01-16.
 */
public class BackgroundManager extends ObjectManager {
    private static final String TAG = BackgroundManager.class.getSimpleName();

    private static final int MOVEMENT_SPEED = -300;  // pixels per second, negative is down

    private static final int BACKGROUND_TEXTURE_INDEX = 19;

    private float mHeight;

    public BackgroundManager(Context context, float height) {
        super();

        mHeight = height;

        RectF locationRect = new RectF(
                0,
                200,
                200,
                0
        );

        for (int i = 0; i < Math.ceil(mHeight / 200) + 1; i++) {
            mObjects.add(new GameObject(BACKGROUND_TEXTURE_INDEX, locationRect, "Background" + i));

            locationRect.left += 200;
            locationRect.right += 200;
            mObjects.add(new GameObject(BACKGROUND_TEXTURE_INDEX, locationRect, "Background" + i));

            locationRect.left += 200;
            locationRect.right += 200;
            mObjects.add(new GameObject(BACKGROUND_TEXTURE_INDEX, locationRect, "Background" + i));

            locationRect.left = 0;
            locationRect.right = 200;
            locationRect.top += 200;
            locationRect.bottom += 200;
        }
    }

    @Override
    public void update(long timeDelta) {
        float movementDistance = MOVEMENT_SPEED * timeDelta / 1000;


            for (int i = 0; i < mObjects.size(); i++) {
                GameObject object = ((GameObject) mObjects.get(i));

                if (object.getLocationRect().top < 0) {
                    object.getLocationRect().top += mObjects.size() / 3 * 200;
                    object.getLocationRect().bottom += mObjects.size() / 3 * 200;
                }

                object.getLocationRect().top += BaseObject.scrollDistance;
                object.getLocationRect().bottom += BaseObject.scrollDistance;

            }

        super.update(timeDelta);

    }
}

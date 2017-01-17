package com.kaidenho.gamelooptest;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by Kaiden Ho on 2017-01-16.
 */
public class BackgroundManager extends ObjectManager {
    private static final String TAG = BackgroundManager.class.getSimpleName();

    private static final int BACKGROUND_TEXTURE_INDEX = 19;

    private Context mContext;
    private Scaling mScaling;

    private int debugCounter;

    public BackgroundManager(Context context) {
        super();

        mContext = context;

        mScaling = new Scaling(mContext);

        Rect locationRect = new Rect(
                0,
                200,
                200,
                0
        );

        for (int i = 0; i < Math.ceil(mScaling.gameHeight / 200) + 1; i++) {
            mObjects.add(new GameObject(BACKGROUND_TEXTURE_INDEX, locationRect, mContext, "Background" + i));
            locationRect.top += 200;
            locationRect.bottom += 200;
        }
    }

    @Override
    public void update(long timeDelta) {
    //    debugCounter++;

       // if (debugCounter > 5) {
          //  Log.d(TAG,"NEW OBJECT GROUP\n\n\n");
            for (int i = 0; i < mObjects.size(); i++) {
                GameObject object = ((GameObject) mObjects.get(i));

                if (object.getLocationRect().top < 0) {
                    object.getLocationRect().top += mObjects.size() * 200 * mScaling.gameUnit;
                    object.getLocationRect().bottom += mObjects.size() * 200 * mScaling.gameUnit;
                }
                object.getLocationRect().top -= 10;
                object.getLocationRect().bottom -= 10;

                object.setVertexBuffer(object.updateLocation());
                object.addToDebugCounter();
         //       Log.d(TAG,object.getName() + " top location = " + object.getLocationRect().top);
            }

            debugCounter = 0;
   //     }
        super.update(timeDelta);

    }
}

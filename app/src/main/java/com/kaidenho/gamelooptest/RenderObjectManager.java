package com.kaidenho.gamelooptest;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Kaiden Ho on 2016-10-02.
 */
public class RenderObjectManager extends ObjectManager {
    private final static String TAG = RenderObjectManager.class.getSimpleName();

    public void draw(float[] mtrxProjectionAndView) {
        for (int i = 0; i < mObjects.size(); i++) {
            if( mObjects.get(i) != null && mObjects.get(i) instanceof GameObject) {
                try {
                    ((GameObject) mObjects.get(i)).draw(mtrxProjectionAndView);
                }
                catch (IndexOutOfBoundsException e) {
                    Log.d(TAG, "ERROR: Item " + i + " out-of-bounds of arraylist size " + mObjects.size());
                }
                //Log.d(TAG,"Objects being drawn - " + mObjects.size());
            } else {
                Log.d(TAG, "ERROR: Tried to draw null or incompatible object.");
            }
        }
    }

    @Override
    public void add(BaseObject object) {
        super.add(object);

        //Log.v(TAG, "RenderMangaer size is " + mObjects.size());

        if (!(object instanceof GameObject)) {
            throw new IllegalArgumentException("Object added to RenderManager not a GameObject");
        }
    }

    public void printAll() {
        Log.d(TAG,"BREAK");
        for (int i = 0; i < mObjects.size(); i++) {
            GameObject object = ((GameObject) mObjects.get(i));

            if (object.getName().equals("Background2") ||
                    object.getName().equals("Background3") ||
                    object.getName().equals("Background4") ||
                    object.getName().equals("Background5") ||
                    object.getName().equals("Background1")) {
               /* if (object.previousTop == object.getLocationRect().top) {
                    Log.d(TAG,"REALLY BIG ERROR");
                }*/
                object.previousTop = object.getLocationRect().top;

                Log.d(TAG, object.getName() + " location is " + object.getLocationRect().top + " at " + object.getDebugCounter());
            }
        }
    }
}

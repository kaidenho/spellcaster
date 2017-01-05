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
        for (int i = 0; i < mObjects.size(); i++) {
            Log.d(TAG, "The " + i + " object is " + mObjects.get(i).getClass().getSimpleName());
        }
    }
}

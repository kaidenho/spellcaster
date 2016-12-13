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
            ((GameObject)mObjects.get(i)).draw(mtrxProjectionAndView);
            //Log.d(TAG,"Objects being drawn - " + mObjects.size());
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

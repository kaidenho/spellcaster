package com.kaidenho.gamelooptest;

import java.util.ArrayList;

/**
 * Created by Kaiden Ho on 2016-10-02.
 */
public class RenderObjectManager extends ObjectManager {
    private final static String TAG = RenderObjectManager.class.getSimpleName();

    public void draw(float[] mtrxProjectionAndView) {
        for (int i = 0; i < mObjects.size(); i++) {
            ((GameObject)mObjects.get(i)).draw(mtrxProjectionAndView);
        }
    }

    @Override
    public void add(BaseObject object) {
        super.add(object);

        if (object.getClass() != GameObject.class) {
            throw new IllegalArgumentException("Object added to RenderManager not a GameObject");
        }
    }
}

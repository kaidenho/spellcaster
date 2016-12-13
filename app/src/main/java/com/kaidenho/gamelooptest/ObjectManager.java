package com.kaidenho.gamelooptest;

import android.renderscript.BaseObj;
import android.util.Log;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Object Manager holds a collection of child objects
 *
 * Calling update on an ObjectManager invokes update on all its children
 *
 * Created by Kaiden Ho on 2016-09-26.
 */
public class ObjectManager extends BaseObject {
    private final static String TAG = ObjectManager.class.getSimpleName();

    protected ArrayList<BaseObject> mObjects;

    public ObjectManager(){
        mObjects = new ArrayList<>();
    }

    @Override
    public void update(long timeDelta) {
        for (int i = 0; i < mObjects.size(); i++) {
            mObjects.get(i).update(timeDelta);
        }
    }

    public void add(BaseObject object) {
        if (object == null) {
            throw new NullPointerException("Null object added to ObjectManager");
        }
        mObjects.add(object);
    }

    public void remove(int objectReference) {
        mObjects.remove(objectReference);
    }

    // TODO: Change this so that GameRenderer can just read off of a single render queue.
    // TODO: This will probably require having a drawlock and a function callback to swap the queues once rendering is completed
    public void copy(ObjectManager source) {
        for (int i = 0; i < source.mObjects.size(); i++) {
            try {
                mObjects.set(i, source.mObjects.get(i));
            } catch (IndexOutOfBoundsException e) {
                mObjects.add(i, source.mObjects.get(i));
            }
        }
    }

    public void clear(){
        mObjects.clear();
    }

    public int getSize() {
        return mObjects.size();
    }

    public BaseObject get(int index) {
        return mObjects.get(index);
    }
}

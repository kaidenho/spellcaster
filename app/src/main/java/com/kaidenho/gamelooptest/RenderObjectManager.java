package com.kaidenho.gamelooptest;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Kaiden Ho on 2016-10-02.
 */
public class RenderObjectManager {
    private final static String TAG = RenderObjectManager.class.getSimpleName();

    private Context mContext;
    private Scaling mScaling;

    protected ArrayList<RenderObject> mObjects;

    public RenderObjectManager(Context context) {
        mObjects = new ArrayList<>();

        mContext = context;
        mScaling = new Scaling(mContext);
    }

    public void draw(float[] mtrxProjectionAndView, GameRenderer renderer) {
        int currentUpdate = -1;

        //Log.d(TAG,"Drawing renderqueue" + renderer.getCurrentQueue());

        for (int i = 0; i < mObjects.size(); i++) {
            if( mObjects.get(i) != null) {
               /* RenderObject object = mObjects.get(i);

                if (object.getName().equals("Background2") ||
                        object.getName().equals("Background3") ||
                        object.getName().equals("Background4") ||
                        object.getName().equals("Background5") ||
                        object.getName().equals("Background1")) {
                    if (object.getName().equals("Background1")) {
                        currentUpdate = object.getDebugCounter();
                    }
                    if (object.getDebugCounter() != currentUpdate) {
                        Log.d(TAG,"BIG ERROR");
                    }
                    object.previousTop = object.getLocationRect().top;

                    Log.d(TAG, object.getName() + " of renderqueue" + renderer.getCurrentQueue()
                            + " location is " + object.getLocationRect().top + " at " + object.getDebugCounter());
                }*/

                try {
                    mObjects.get(i).draw(mtrxProjectionAndView);
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

    // copies location and sprite info to the render object
    public void copy(ObjectManager source) {
        for (int i = 0; i < source.getSize(); i++) {
            Rect locationRect = new Rect(
                    (int)(((GameObject)source.get(i)).getLocationRect().left * mScaling.gameUnit),
                    (int)(((GameObject)source.get(i)).getLocationRect().top * mScaling.gameUnit),
                    (int)(((GameObject)source.get(i)).getLocationRect().right * mScaling.gameUnit),
                    (int)(((GameObject)source.get(i)).getLocationRect().bottom * mScaling.gameUnit)
            );

            if (i >= mObjects.size()) {
                // add new renderobject
                RenderObject object = new RenderObject(
                        ((GameObject)source.get(i)).getTextureIndex(),
                        locationRect);

                mObjects.add(object);
            } else {
                if(((GameObject)source.get(i)).getName().equals("Player")) {
         //           Log.d(TAG,"player location = " + locationRect);
          //          Log.d(TAG,"player texture index = " + ((GameObject)source.get(i)).getTextureIndex());
                }

                mObjects.get(i).setLocationRect(locationRect);
                mObjects.get(i).updateLocation();

                mObjects.get(i).setTextureIndex(((GameObject)source.get(i)).getTextureIndex());
            }
        }

        for (int i = 0; i < mObjects.size() - source.getSize(); i++) {
            mObjects.remove(source.getSize() + i);
        }
    }

    public int getSize() {
        return mObjects.size();
    }

  /*  public void add(BaseObject object) {
        if (object == null) {
            throw new NullPointerException("Null object added to ObjectManager");
        }

        mObjects.add(object);
        if (!(object instanceof GameObject)) {
            throw new IllegalArgumentException("Object added to RenderManager not a GameObject");
        }
    }*/


}

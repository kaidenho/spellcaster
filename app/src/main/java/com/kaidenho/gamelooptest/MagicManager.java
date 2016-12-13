package com.kaidenho.gamelooptest;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;

/**
 * Manages magic spells (bullet shots) cast by the player.
 *
 * Most managing tasks are handled by parent(ObjectManager).
 * Magic Manager initializes, keeps track of, and detects spell collisions.
 *
 * Created by Kaiden Ho on 2016-11-22.
 */
public class MagicManager extends ObjectManager {
    private static final String TAG = MagicManager.class.getSimpleName();
    private int mDebugCounter = 0;

    private Context mContext;
    private Player mPlayer;
    private Scaling mScaling;

    public MagicManager(Context context, Player player) {
        super();
        mContext = context;
        mPlayer = player;
        mScaling = new Scaling(mContext);
    }

    public void shoot() {
        Rect locationRect = new Rect(
                (int)(mPlayer.getLocationRect().left / mScaling.gameUnit) + 50,
                (int)(mPlayer.getLocationRect().top / mScaling.gameUnit) - 50,
                (int)(mPlayer.getLocationRect().right / mScaling.gameUnit) - 50,
                (int)(mPlayer.getLocationRect().bottom / mScaling.gameUnit) + 50
        );

        Spell spell = new Spell(locationRect, mContext);
        add(spell);
    }

    public boolean checkCollisions(ObstacleManager collection) {
        for (int i = 0; i < getSize(); i++) {
            Spell spell = (Spell)mObjects.get(i);   // This must by typecast, hence the declaration

            int objectCollided = spell.checkCollisions(collection);
            if (objectCollided >= 0) {
                Log.d(TAG,"Spell collided");
                collection.remove(objectCollided);  // Remove the object the spell hit
                mObjects.remove(i);                 // Remove the spell
                return true;
            }
        }
        return false;
    }

    @Override
    public void update(long timeDelta) {
        super.update(timeDelta);

        int height = mContext.getResources().getDisplayMetrics().heightPixels;
        for (int i = 0; i < getSize(); i++) {
            Spell spell = (Spell)mObjects.get(i);
            if (spell.getLocationRect().bottom > height) {
                mObjects.remove(i);
                Log.d(TAG, "Spell removed. Current total = " + getSize());
            }
        }

    }
}

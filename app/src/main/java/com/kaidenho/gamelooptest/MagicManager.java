package com.kaidenho.gamelooptest;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
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

    private Player mPlayer;

    private int mHeight;

    public MagicManager(Player player, int height) {
        super();
        mPlayer = player;
        mHeight = height;
    }

    public void shoot() {
        RectF locationRect = new RectF(
                mPlayer.getLocationRect().left + 50,
                mPlayer.getLocationRect().top + 50,
                mPlayer.getLocationRect().right - 50,
                mPlayer.getLocationRect().top - 50
        );

        Spell spell = new Spell(locationRect);
        spell.playSound();
        add(spell);
    }

    public boolean checkCollisions(ObstacleManager collection) {
        for (int i = 0; i < getSize(); i++) {
            Spell spell = (Spell)mObjects.get(i);   // This must by typecast, hence the declaration

            int iObjectCollided = spell.checkCollisions(collection);
            if (iObjectCollided >= 0) {
                Log.d(TAG,"Spell collided with " + ((GameObject) collection.mObjects.get(iObjectCollided)).getName());
                if (collection.get(iObjectCollided) instanceof Obstacle) {
                    ((Obstacle) collection.get(iObjectCollided)).addToDamageCounter(1);
                    ((Obstacle) collection.get(iObjectCollided)).checkDamage(collection, iObjectCollided);
                } else {
                    BaseObject removedObject = collection.remove(iObjectCollided);  // Remove the object the spell hit

                    if (removedObject == null) {
                        Log.d(TAG,"obstacle not removed");
                    }
                }
                Log.d(TAG,"Obstacle size is now " + collection.getSize());
                mObjects.remove(i);                // Remove the spell
                return true;
            }
        }
        return false;
    }

    @Override
    public void update(long timeDelta) {
        super.update(timeDelta);

        for (int i = 0; i < getSize(); i++) {
            Spell spell = (Spell)mObjects.get(i);
            if (spell.getLocationRect().bottom > mHeight) {
                mObjects.remove(i);
                Log.d(TAG,"height = " + mHeight + ", spell bottom = " + spell.getLocationRect().bottom);
                Log.d(TAG, "Spell removed. Current total = " + getSize());
            }
        }

    }
}

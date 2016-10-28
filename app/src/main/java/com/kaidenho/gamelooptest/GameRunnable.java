package com.kaidenho.gamelooptest;

import android.content.Context;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.Log;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Kaiden Ho on 2016-09-27.
 */
public class GameRunnable implements Runnable {
    private static String TAG = GameRunnable.class.getSimpleName();

    private final static int FRAME_DURATION_MILLIS = 16;

    private Game mGameRoot;

    private boolean mRunning = true;
    private boolean mPaused = false;
    private Object mPauseLock;

    public GameRunnable(Game gameRoot) {
        mGameRoot = gameRoot;
    }

    public void run() {
        long updateDuration = 0;

        long currentFrameStart = SystemClock.uptimeMillis();
        long previousFrameStart = currentFrameStart;

        while(mRunning) {
            // Time at start
            currentFrameStart = SystemClock.uptimeMillis();

            // Calls update on all the current game objects. See ObjectManager
            mGameRoot.getGameManager().update(currentFrameStart - previousFrameStart);
            //Log.v(TAG, "Object Update Count is " + mGameManager.getSize());

            if (mGameRoot.getPlayer().checkCollisions(mGameRoot.getObstacleManager())) {
                //Log.d(TAG, "Collision detected");
            }

            // After updating the objects location
            BaseObject.renderSystem.swap(mGameRoot.getRenderer());

            updateDuration = SystemClock.uptimeMillis() - currentFrameStart;

            if (updateDuration < FRAME_DURATION_MILLIS) {
                try {
                    Thread.sleep(FRAME_DURATION_MILLIS - updateDuration);
                } catch (InterruptedException e) {
                    Log.d(TAG,"Game Runnable Sleep Interrupted");
                }
            }
            while (mPaused) {
                try {
                    mPauseLock.wait();
                } catch (InterruptedException e) {
                    // No big deal if this wait is interrupted.
                }
            }

            previousFrameStart = currentFrameStart;
        }
    }

    public void pauseGame() {
        synchronized (mPauseLock) {
            mPaused = true;
        }
    }

    public void resumeGame() {
        synchronized (mPauseLock) {
            Log.d(TAG, "resumeGame() was called");
            mPaused = false;
            mPauseLock.notifyAll();
        }
    }
}

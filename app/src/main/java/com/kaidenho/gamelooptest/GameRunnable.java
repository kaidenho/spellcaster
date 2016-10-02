package com.kaidenho.gamelooptest;

import android.os.SystemClock;
import android.util.Log;

/**
 * Created by Kaiden Ho on 2016-09-27.
 */
public class GameRunnable implements Runnable {
    private static String TAG = GameRunnable.class.getSimpleName();

    private final static int FRAME_DURATION_MILLIS = 500;

    private ObjectManager mGameManager;
    private GameRenderer mRenderer;

    private boolean mRunning = true;

    public GameRunnable(GameRenderer renderer) {
        mRenderer = renderer;
    }

    public void run() {
        long updateDuration = 0;

        long currentFrameStart = SystemClock.uptimeMillis();
        long previousFrameStart = currentFrameStart;

        while(mRunning) {
            // Time at start
            currentFrameStart = SystemClock.uptimeMillis();

            // Calls update on all the current game objects. See ObjectManager
            mGameManager.update(currentFrameStart - previousFrameStart);

            // After updating the objects location
            BaseObject.renderSystem.swap(mRenderer);

            updateDuration = SystemClock.uptimeMillis() - currentFrameStart;

            if (updateDuration < FRAME_DURATION_MILLIS) {
                try {
                    Thread.sleep(FRAME_DURATION_MILLIS - updateDuration);
                } catch (InterruptedException e) {
                    Log.d(TAG,"Game Thread Frame Sleep");
                }
            }

            previousFrameStart = currentFrameStart;
        }
    }

    public void setGameManager(ObjectManager gameRoot) {
        mGameManager = gameRoot;
    }
}

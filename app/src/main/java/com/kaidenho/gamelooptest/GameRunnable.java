package com.kaidenho.gamelooptest;

import android.app.Activity;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by Kaiden Ho on 2016-09-27.
 */
public class GameRunnable implements Runnable {
    private static String TAG = GameRunnable.class.getSimpleName();

    private final static int FRAME_DURATION_MILLIS = 16;

    private Game mGameRoot;

    private boolean mRunning = true;
    private boolean mPaused = true;
    private Object mPauseLock = new Object();


    public GameRunnable(Game gameRoot) {
        mGameRoot = gameRoot;
    }

    public void run() {
        // Since this loop will later wait on the lock object, it must first acquire the lock.
        synchronized (mPauseLock) {
            long updateDuration = 0;
            long currentFrameStart = SystemClock.uptimeMillis();
            long previousFrameStart = currentFrameStart;

            while(mRunning) {
                // Time at start
                currentFrameStart = SystemClock.uptimeMillis();

                // Calls update on all the current game objects. See ObjectManager
                mGameRoot.getGameManager().update(currentFrameStart - previousFrameStart);
                //Log.v(TAG, "Object Update Count is " + mGameManager.getSize());

                // TODO: why aren't the obstacles and spels disappearing at the right time?
                mGameRoot.getMagicManager().checkCollisions(mGameRoot.getObstacleManager());

                if (mGameRoot.getPlayer().checkCollisions(mGameRoot.getObstacleManager())) {
                    Intent intent = new Intent(mGameRoot.getContext(), GameOverActivity.class);
                    Activity activity = (Activity)mGameRoot.getContext();
                    Log.d(TAG, "Game Over");
                    activity.startActivity(intent);
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

                previousFrameStart = currentFrameStart;

                while (mPaused) {
                    try {
                        mPauseLock.wait();
                        previousFrameStart = SystemClock.uptimeMillis();
                    } catch (InterruptedException e) {
                        // No big deal if this wait is interrupted.
                    }
                }
            }
        }
    }

    /**
     * Pauses the game thread. This is called when the activity is paused.
     */
    public void pauseGame() {
        Log.v(TAG, "pauseGame +");
        mPaused = true;
        Log.v(TAG, "pauseGame -");
    }

    /**
     * Resumes the game. This is called when the activity is resumed. Based on the Android
     * activity lifecycle, we also expect this to be called when the activity is creates.
     */
    public void resumeGame() {
        Log.v(TAG, "resumeGame +");
        synchronized (mPauseLock) {
            mPaused = false;
            mPauseLock.notifyAll();
        }
        Log.v(TAG, "resumeGame -");
    }
}

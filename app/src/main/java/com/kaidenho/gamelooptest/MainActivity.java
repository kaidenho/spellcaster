package com.kaidenho.gamelooptest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/**
 * This activity holds the 'Game'. It shouldn't be the first activity
 * in the finished game, as there should be a menu before this.
 *
 * When this activity starts, gameplay begins.
 */
public class MainActivity extends Activity {
    private final static String TAG = MainActivity.class.getSimpleName();

    // Game will sort UI events and sets up high level game objects and systems
    private Game mGame;

    // The surface view that openGL will draw on
    private GameSurfaceView mSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGame = new Game(this);

        // make the window fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mSurfaceView = new GameSurfaceView(this);
        setContentView(mSurfaceView);

        // Set up GameManager, Renderer and GameRunnable
        mGame.bootstrap();
        Log.d(TAG,"Renderer created");
        mSurfaceView.setRenderer(mGame.getRenderer());
    }
}

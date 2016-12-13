package com.kaidenho.gamelooptest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 * This activity holds the 'Game'. It shouldn't be the first activity
 * in the finished game, as there should be a menu before this.
 *
 * When this activity starts, gameplay begins.
 */
public class GameActivity extends Activity {
    private final static String TAG = GameActivity.class.getSimpleName();

    // Game will sort UI events and sets up high level game objects and systems
    private Game mGame;

    // The surface view that openGL will draw on
    private GameSurfaceView mSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate +");
        super.onCreate(savedInstanceState);

        mGame = new Game(this);
        BaseObject.gameSystem = mGame;

        // make the window fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mSurfaceView = new GameSurfaceView(this);
        setContentView(mSurfaceView);

        // Set up GameManager, Renderer and GameRunnable
        mGame.bootstrap();
        mSurfaceView.setRenderer(mGame.getRenderer());
        Log.v(TAG, "onCreate -");
    }

    @Override
    protected void onResume() {
        Log.v(TAG, "onResume +");
        super.onResume();
        // Resume the surface view first and then the game
        mSurfaceView.onResume();
        mGame.onResume();
        Log.v(TAG, "onResume -");
    }

    @Override
    protected void onPause() {
        Log.v(TAG, "onPause +");
        super.onPause();
        // Pause the game first then the surface view
        mGame.onPause();
        mSurfaceView.onPause();
        Log.v(TAG, "onPause -");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGame.handleTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.d("Main Activity", "Touch event at " + event.getX() + ", " + event.getY());
        }
        return true;
    }

}

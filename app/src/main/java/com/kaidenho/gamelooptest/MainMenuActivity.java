package com.kaidenho.gamelooptest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by kaidenho on 28/10/16.
 */
public class MainMenuActivity extends Activity {
    private final static String TAG = MainMenuActivity.class.getSimpleName();

    private final int START_BUTTON_TEXTURE_INDEX = 3;

    private Scaling mScaling;

    private SharedPreferences mSavedData;

    // OpenGL SurfaceView and Renderer
    private GameSurfaceView mSurfaceView;
    private GameRenderer mRenderer;
    private ScoreManager mScoreManager;

    private GameObject mStartButton;

    private long mHighScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);

        // make the window fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mSavedData = getSharedPreferences(getString(R.string.saved_data), MODE_PRIVATE);
        mHighScore = mSavedData.getLong(this.getString(R.string.high_score), 0); // '0' provided as default value
        Log.d(TAG, "High score is " + mSavedData.getLong(this.getString(R.string.high_score), 0));

        mScaling = new Scaling(this);

        mSurfaceView = new GameSurfaceView(this);
        setContentView(mSurfaceView);

        mRenderer = new GameRenderer(this);
        mSurfaceView.setRenderer(mRenderer);
        mSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);    // requestRender() must be called whenever a new frame is created
        Log.v(TAG,"Renderer created");

        // TODO: Move this into a new function specifically for rendering
        createMenu();
        createScoreDisplay();

        BaseObject.renderSystem.swap(mRenderer);
        mSurfaceView.requestRender();
        Log.d(TAG,"Renderer Requested");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        handleTouchEvent(event);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSurfaceView.onResume();
        Log.d(TAG, "Resumed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSurfaceView.onPause();
        Log.d(TAG, "Paused");
    }

    private void createMenu() {
        // Location given in scaled gameUnits
        Rect location = new Rect(
                200,
                (int)(mScaling.gameHeight / 2 + 100),
                400,
                (int)(mScaling.gameHeight / 2 - 100)
        );

        mStartButton = new GameObject(START_BUTTON_TEXTURE_INDEX, location, this, "Start Button");

        BaseObject.renderSystem.add(mStartButton);
    }

    private void createScoreDisplay() {
        // get digits to score so that it can be centered
        int scoreDigitCount = 0;
        long score = mHighScore;
        while (score >= 1) {
            scoreDigitCount++;
            score /= 10;
        }

        // display the high score
        Rect scoreLocationRect = new Rect(
                300 + (int)(scoreDigitCount * 30) - 60,  // each digit is 60 x 60
                (int)(mScaling.gameHeight / 2) - 200,
                300 + (int)(scoreDigitCount * 30 ),
                (int)(mScaling.gameHeight / 2) - 260
        );

        mScoreManager = new ScoreManager(this, scoreLocationRect);
        mScoreManager.setScore(mHighScore);
        mScoreManager.update(0);
    }

    private void handleTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = this.getResources().getDisplayMetrics().heightPixels - event.getY(); // invert the y axis to conform with openGL 0y at the bottom

        if(within(x, y, mStartButton.getLocationRect())){
            Log.d(TAG,"Launching GameActivity");
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        }
    }

    public boolean within(float x, float y, Rect rect) {
        //Log.d("Within", "x " + x + ", y " + y + ", left " + rect.left + ", right " + rect.right + ", bottom " + rect.bottom + ", top " + rect.top);
        if (x < rect.left || x > rect.right || y < rect.bottom || y > rect.top) {
            return false;
        }
        return true;
    }
}

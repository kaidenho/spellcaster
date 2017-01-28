package com.kaidenho.gamelooptest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Kaiden Ho on 2016-11-23.
 */
public class GameOverActivity extends Activity {
    private final static String TAG = GameOverActivity.class.getSimpleName();

    private final int RETRY_BUTTON_TEXTURE_INDEX = 5;
    private final int MAIN_MENU_BUTTON_TEXTURE_INDEX = 4;
    private final int NEW_HIGH_SCORE_TEXTURE_INDEX = 16;

    private Scaling mScaling;

    // OpenGL SurfaceView and Renderer
    private GameSurfaceView mSurfaceView;
    private GameRenderer mRenderer;
    private RenderObjectManager mRenderQueue;
    private ScoreManager mScoreManager;

    private GameObject mRetryButton;
    private GameObject mMainMenuButton;
    private GameObject mNewHighScore;

    private long mScore;
    private boolean mCheckNewHighScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);

        // make the window fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mScore = getIntent().getLongExtra("SCORE", 100);
        mCheckNewHighScore = getIntent().getBooleanExtra("NEW_HIGH_SCORE", false);

        mScaling = new Scaling(this);

        mSurfaceView = new GameSurfaceView(this);
        setContentView(mSurfaceView);

        mRenderer = new GameRenderer(this);
        mSurfaceView.setRenderer(mRenderer);
        Log.d(TAG,"Renderer created");

        createMenu();
        createScoreDisplay();

        BaseObject.renderSystem.swap(mRenderer);
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
        RectF retryButtonlocation = new RectF(
                350,
                (int)(mScaling.gameHeight / 2 - 100),   // 300 is half of the screen, because gameUnit is divided by 600
                500,
                (int)(mScaling.gameHeight / 2 - 250)
        );

        mRetryButton = new GameObject(RETRY_BUTTON_TEXTURE_INDEX, retryButtonlocation, "Start Button");
        BaseObject.renderSystem.add(mRetryButton);

        RectF mainMenuButtonlocation = new RectF(
                100,
                (int)(mScaling.gameHeight / 2 - 100),   // 300 is half of the screen, because gameUnit is divided by 600
                250,
                (int)(mScaling.gameHeight / 2 - 250)
        );

        mMainMenuButton = new GameObject(MAIN_MENU_BUTTON_TEXTURE_INDEX, mainMenuButtonlocation, "Start Button");
        BaseObject.renderSystem.add(mMainMenuButton);
    }

    private void createScoreDisplay() {
        // get digits to score so that it can be centered
        int scoreDigitCount = 0;
        long score = mScore;
        while (score >= 1) {
            scoreDigitCount++;
            score /= 10;
        }

        RectF scoreLocationRect = new RectF(
                300 + (int)(scoreDigitCount / 2 * 60) - 30,  // each digit is 60 x 60
                (int)(mScaling.gameHeight / 2) + 60,
                300 + (int)(scoreDigitCount / 2 * 60) + 30,
                (int)(mScaling.gameHeight / 2)
        );

        mScoreManager = new ScoreManager(this, scoreLocationRect);
        mScoreManager.setScore(mScore);

        // TODO: Draw 'New high score' image
        /*if (mCheckNewHighScore) {
            scoreLocationRect.left += 60;
            scoreLocationRect.top += 100;
            scoreLocationRect.right += 100;
            scoreLocationRect.bottom += 60;

            mNewHighScore = new GameObject(NEW_HIGH_SCORE_TEXTURE_INDEX, scoreLocationRect, this, "New High Score");
            mRenderQueue.add(mNewHighScore);
        }*/

        mScoreManager.update(0);    // adds the digits to the renderqueue
    }

    private void handleTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = this.getResources().getDisplayMetrics().heightPixels - event.getY(); // invert the y axis to conform with openGL 0y at the bottom

        if(within(x, y, mRetryButton.getLocationRect())){
            Log.d(TAG,"Launching GameActivity");
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        }

        if(within(x, y, mMainMenuButton.getLocationRect())){
            Log.d(TAG,"Launching MainMenuActivity");
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
        }
    }

    public boolean within(float x, float y, RectF rect) {
        //Log.d("Within", "x " + x + ", y " + y + ", left " + rect.left + ", right " + rect.right + ", bottom " + rect.bottom + ", top " + rect.top);
        if (x < rect.left * mScaling.gameUnit || x > rect.right * mScaling.gameUnit
                || y < rect.bottom * mScaling.gameUnit || y > rect.top * mScaling.gameUnit) {
            return false;
        }
        return true;
    }
}
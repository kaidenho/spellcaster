package com.kaidenho.gamelooptest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Kaiden Ho on 2016-11-23.
 */
public class GameOverActivity extends Activity {
    private final static String TAG = MainMenuActivity.class.getSimpleName();

    private final int RETRY_BUTTON_IMAGE_SOURCE = R.drawable.mage;
    private final int MAIN_MENU_BUTTON_IMAGE_SOURCE = R.drawable.mage;

    private Scaling mScaling;

    // OpenGL SurfaceView and Renderer
    private GameSurfaceView mSurfaceView;
    private GameRenderer mRenderer;
    private RenderObjectManager mRenderQueue;

    private GameObject mRetryButton;
    private GameObject mMainMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);

        // make the window fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mScaling = new Scaling(this);

        mSurfaceView = new GameSurfaceView(this);
        setContentView(mSurfaceView);

        mRenderer = new GameRenderer(this);
        mSurfaceView.setRenderer(mRenderer);
        //mSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);    // requestRender() must be called whenever a new frame is created
        Log.v(TAG,"Renderer created");

        // TODO: Move this into a new function specifically for rendering
        mRenderQueue = createMenu();
        mRenderer.setRenderQueue(mRenderQueue);
        // mSurfaceView.requestRender();
        Log.d(TAG,"Renderer Requested");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        handleTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.v("Main Activity", "Touch event at " + event.getX() + ", " + event.getY());
        }
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

    private RenderObjectManager createMenu() {
        RenderObjectManager renderQueue = new RenderObjectManager();

        // Location given in scaled gameUnits
        Rect retryButtonlocation = new Rect(
                350,
                (int)(this.getResources().getDisplayMetrics().heightPixels / mScaling.gameUnit / 2 + 75),   // 300 is half of the screen, because gameUnit is divided by 600
                500,
                (int)(this.getResources().getDisplayMetrics().heightPixels / mScaling.gameUnit / 2 - 75)
        );

        mRetryButton = new GameObject(RETRY_BUTTON_IMAGE_SOURCE, retryButtonlocation, this, "Start Button");

        renderQueue.add(mRetryButton);
        Rect mainMenuButtonlocation = new Rect(
                100,
                (int)(this.getResources().getDisplayMetrics().heightPixels / mScaling.gameUnit / 2 + 75),   // 300 is half of the screen, because gameUnit is divided by 600
                250,
                (int)(this.getResources().getDisplayMetrics().heightPixels / mScaling.gameUnit / 2 - 75)
        );

        mMainMenuButton = new GameObject(RETRY_BUTTON_IMAGE_SOURCE, mainMenuButtonlocation, this, "Start Button");

        renderQueue.add(mMainMenuButton);

        return renderQueue;
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

    public boolean within(float x, float y, Rect rect) {
        Log.d("Within", "x " + x + ", y " + y + ", left " + rect.left + ", right " + rect.right + ", bottom " + rect.bottom + ", top " + rect.top);
        if (x < rect.left || x > rect.right || y < rect.bottom || y > rect.top) {
            return false;
        }
        return true;
    }
}
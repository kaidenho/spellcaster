package com.kaidenho.gamelooptest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by kaidenho on 28/10/16.
 */
public class MainMenuActivity extends Activity {
    private final static String TAG = MainMenuActivity.class.getSimpleName();

    private final int START_BUTTON_IMAGE_SOURCE = R.drawable.mage;

    private Scaling mScaling;

    // OpenGL SurfaceView and Renderer
    private GameSurfaceView mSurfaceView;
    private GameRenderer mRenderer;
    private RenderObjectManager mRenderQueue;

    private GameObject mStartButton;

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
        Rect location = new Rect(
                200,
                (int)(this.getResources().getDisplayMetrics().heightPixels / mScaling.gameUnit / 2 + 100),   // 300 is half of the screen, because gameUnit is divided by 600
                400,
                (int)(this.getResources().getDisplayMetrics().heightPixels / mScaling.gameUnit / 2 - 100)
        );

        mStartButton = new GameObject(START_BUTTON_IMAGE_SOURCE, location, this, "Start Button");

        renderQueue.add(mStartButton);

        return renderQueue;
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
        Log.d("Within", "x " + x + ", y " + y + ", left " + rect.left + ", right " + rect.right + ", bottom " + rect.bottom + ", top " + rect.top);
        if (x < rect.left || x > rect.right || y < rect.bottom || y > rect.top) {
            return false;
        }
        return true;
    }
}

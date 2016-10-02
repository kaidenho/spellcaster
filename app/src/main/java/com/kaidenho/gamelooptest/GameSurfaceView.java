package com.kaidenho.gamelooptest;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Kaiden Ho on 2016-09-27.
 */
public class GameSurfaceView extends GLSurfaceView {

    //private final GameRenderer mRenderer;

    public GameSurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(2);

        //mRenderer = new GameRenderer();
        //setRenderer(mRenderer);
    }
}

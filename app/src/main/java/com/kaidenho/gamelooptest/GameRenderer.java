package com.kaidenho.gamelooptest;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Kaiden Ho on 2016-09-27.
 */
public class GameRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = GameRenderer.class.getSimpleName();

    private RenderObjectManager mDrawQueue;

    // Matrices
    private final float[] mtrxProjection = new float[16];
    private final float[] mtrxView = new float[16];
    private final float[] mtrxProjectionAndView = new float[16];

    // Screenresolution
    private float   mScreenWidth;
    private float   mScreenHeight;

    private Context mContext;

    public GameRenderer(Context context) {
        mContext = context;
        mDrawQueue = new RenderObjectManager();
    }

    @Override
    public void onDrawFrame(GL10 unused){
        // Set our shader program to image shader
        GLES20.glUseProgram(ShaderInfo.sp_Image);

        // Clear Screen and Depth Buffer
        //GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Black Background
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);

        if (mDrawQueue != null) {
            mDrawQueue.draw(mtrxProjectionAndView);
        }
    }

    public void onSurfaceChanged (GL10 gl, int width, int height) {
        // We need to know the current width and height
        mScreenWidth = width;
        mScreenHeight = height;

        // Redo the Viewport, making it fullscreen
        GLES20.glViewport(0, 0, (int)mScreenWidth, (int)mScreenHeight);

        // Clear our matrices
        for(int i=0;i<16;i++)
        {
            mtrxProjection[i] = 0.0f;
            mtrxView[i] = 0.0f;
            mtrxProjectionAndView[i] = 0.0f;
        }

        // Setup our screen width and height for normal sprite translation.
        Matrix.orthoM(mtrxProjection, 0, 0f, mScreenWidth, 0.0f, mScreenHeight, 0, 50);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);
    }

    public void onSurfaceCreated (GL10 gl, EGLConfig config) {
        mScreenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = mContext.getResources().getDisplayMetrics().heightPixels;


        // Set the clear color to black. TODO: Remove this later once there's an actual background
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);

        // Object shaders
        int vertexShader = ShaderInfo.loadShader(GLES20.GL_VERTEX_SHADER, ShaderInfo.vs_Image);
        int fragmentShader = ShaderInfo.loadShader(GLES20.GL_FRAGMENT_SHADER, ShaderInfo.fs_Image);

        ShaderInfo.sp_Image = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(ShaderInfo.sp_Image, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(ShaderInfo.sp_Image, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(ShaderInfo.sp_Image);                  // creates OpenGL ES program executables

        // Set our shader program
        GLES20.glUseProgram(ShaderInfo.sp_Image);

        // Enable blending
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        Log.d(TAG, "onSurfaceCreated");
    }

    public void setRenderQueue(RenderObjectManager renderQueue) {
        if (renderQueue == null) throw new NullPointerException();
        mDrawQueue = renderQueue;
    }
}

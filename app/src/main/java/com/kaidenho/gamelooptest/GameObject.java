package com.kaidenho.gamelooptest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.MotionEvent;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * GameObject is a drawable BaseObject.
 *
 * Created by Kaiden Ho on 2016-10-02.
 */
public class GameObject extends BaseObject {
    private final static String TAG = GameObject.class.getSimpleName();

    // Buffers
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mTextureBuffer;
    private ShortBuffer mDrawlistBuffer;

    // References for openGL rendering
    private final static short[] mIndices = new short[]{0, 1, 2, 0, 2, 3};

    // Image source
    private int mBitmapSource;

    // Location
    private Rect mLocationRect = new Rect();

    // Inherited contexts
    private Context mContext;

    private Scaling mScaling;

    // Debug
    private String mName;

    /**
     *
     * @param bitmapSource
     * @param locationRect pass in initial location. Coordinates should be in scaled units.
     * @param context
     * @param name
     */
    public GameObject (int bitmapSource, Rect locationRect, Context context, String name) {
        Log.v(TAG, "GameObject created");

        setContext(context);
        mScaling = new Scaling(mContext);
        mBitmapSource = bitmapSource;
        mName = name;

        // Initial location
        locationRect.left = (int)(locationRect.left * mScaling.gameUnit);
        locationRect.top = (int)(locationRect.top * mScaling.gameUnit);
        locationRect.right = (int)(locationRect.right * mScaling.gameUnit);
        locationRect.bottom = (int)(locationRect.bottom * mScaling.gameUnit);
        mLocationRect = locationRect;

        // Create the primitive to draw on
        mVertexBuffer = updateLocation(mLocationRect);

        // Create the image information and retrieve our image from the resources.
        mTextureBuffer = setTextureBuffer(mTextureBuffer, mContext, mBitmapSource);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(mIndices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        mDrawlistBuffer = dlb.asShortBuffer();
        mDrawlistBuffer.put(mIndices);
        mDrawlistBuffer.position(0);
    }

    public void draw(float[] mtrxProjectionAndView) {
        // get handle to vertex shader's vPosition member
        int mPositionHandle = GLES20.glGetAttribLocation(ShaderInfo.sp_Image, "vPosition");

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, mVertexBuffer);

        // Get handle to texture coordinates location
        int mTexCoordLoc = GLES20.glGetAttribLocation(ShaderInfo.sp_Image, "a_texCoord" );

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray (mTexCoordLoc);

        // Prepare the texture coordinates
        GLES20.glVertexAttribPointer (mTexCoordLoc, 2, GLES20.GL_FLOAT, false, 0, mTextureBuffer);

        // Get handle to shape's transformation matrix
        int mtrxhandle = GLES20.glGetUniformLocation(ShaderInfo.sp_Image, "uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, mtrxProjectionAndView, 0);

        // Get handle to textures locations
        int mSamplerLoc = GLES20.glGetUniformLocation (ShaderInfo.sp_Image, "s_texture" );

        // Set the sampler texture unit to 0, where we have saved the texture.
        GLES20.glUniform1i ( mSamplerLoc, 0);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mIndices.length, GLES20.GL_UNSIGNED_SHORT, mDrawlistBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);

        //Log.d(TAG,"Drawing " + mName + " at " + mLocationRect.left + ", " + mLocationRect.top + ", " + mLocationRect.right + ", " + mLocationRect.bottom);
    }

    @Override
    public void update(long timeDelta){
        BaseObject.renderSystem.add(this);
        //Log.d(TAG,"RenderQueue size is " + BaseObject.renderSystem.getSize());
    }

    private FloatBuffer setTextureBuffer(FloatBuffer mTextureBuffer, Context context, int resourceId)
    {

        // Set the texture buffer
        float[] uvs = new float[] {
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
        };

        // The texture buffer
        ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        mTextureBuffer = bb.asFloatBuffer();
        mTextureBuffer.put(uvs);
        mTextureBuffer.position(0);

        // Generate Textures, if more needed, alter these numbers.
        int[] texturenames = new int[1];
        GLES20.glGenTextures(1, texturenames, 0);

        // Temporary create a bitmap
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resourceId);

        // Bind texture to texturename
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[0]);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

        // We are done using the bitmap so we should recycle it.
        bmp.recycle();

        return mTextureBuffer;
    }

    protected FloatBuffer updateLocation(Rect mLocationRect) {
        FloatBuffer mVertexBuffer;
        float [] mVertices = new float[12];

        // Vertices coordinates are set in groups of three (x,y,z)
        // Coordinates should be given in counterclockwise order due to openGL rendering procedures
        mVertices[0] = mLocationRect.left;   mVertices[1] = mLocationRect.top;   mVertices[2] = 0;
        mVertices[3] = mLocationRect.left;   mVertices[4] = mLocationRect.bottom;   mVertices[5] = 0;
        mVertices[6] = mLocationRect.right; mVertices[7] = mLocationRect.bottom;   mVertices[8] = 0;
        mVertices[9] = mLocationRect.right; mVertices[10] = mLocationRect.top; mVertices[11] = 0;

        // Set the vertex buffer
        ByteBuffer bb = ByteBuffer.allocateDirect(mVertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        mVertexBuffer = bb.asFloatBuffer();
        mVertexBuffer.put(mVertices);
        mVertexBuffer.position(0);

        //Log.v(TAG, "Location - " + mLocationRect.left + ", " + mLocationRect.right);

        return mVertexBuffer;
    }

    ///
    /// Getters and setters
    ///

    /**
     * @param context the context to set, cannot be null
     */
    private final void setContext(final Context context) {
        if (context == null) {
            throw new NullPointerException();
        }
        mContext = context;
    }

    /**
     * @return the context, never null
     */
    protected Context getContext() {
        return mContext;
    }

    public Rect getLocationRect() {
        return mLocationRect;
    }

    protected void setLocationRect(Rect newLocation) {
        mLocationRect = newLocation;
    }

    public Scaling getScaling() {
        return mScaling;
    }

    public String getName() {
        return mName;
    }

    public FloatBuffer getVertexBuffer() {
        return mVertexBuffer;
    }

    public void setVertexBuffer(FloatBuffer vertexBuffer) {
        mVertexBuffer = vertexBuffer;
    }


}

package com.kaidenho.gamelooptest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * RenderSystem manages the two (or more) render queues used to draw the games.
 *
 * Created by Kaiden Ho on 2016-09-27.
 */
public class RenderSystem {
    private final static String TAG = RenderSystem.class.getSimpleName();

    private final static int DRAW_QUEUE_COUNT = 2;

    private ObjectManager[] mRenderQueues;
    private int mCurrentQueue = 0;

    public RenderSystem() {
        mRenderQueues = new ObjectManager[DRAW_QUEUE_COUNT];

        for (int i = 0; i < DRAW_QUEUE_COUNT; i++) {
            mRenderQueues[i] = new ObjectManager();
        }
    }

    public void generateTextures(Context context) {
        int[] textureID = new int[]{
                R.drawable.player,      // 0
                R.drawable.boulder,
                R.drawable.spell01,
                R.drawable.play_button,
                R.drawable.home_button,
                R.drawable.redo_button,  // 5
                R.drawable.zero,
                R.drawable.one,
                R.drawable.two,
                R.drawable.three,
                R.drawable.four,    // 10
                R.drawable.five,
                R.drawable.six,
                R.drawable.seven,
                R.drawable.eight,
                R.drawable.nine,    // 15
                R.drawable.mage01,
                R.drawable.mage02,
                R.drawable.mage03,
                R.drawable.tile
        };

        // Generate Textures, if more needed, alter these numbers.
        int[] texturenames = new int[textureID.length];
        GLES20.glGenTextures(textureID.length, texturenames, 0);

        // Retrieve our image from resources.
        //int id = context.getResources().getIdentifier("drawable/textureatlas", null, context.getPackageName());
        for (int i = 0; i < textureID.length; i++) {
            // Temporary create a bitmap
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), textureID[i]);

            // Bind texture to texturename
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + i);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[i]);

            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

            // We are done using the bitmap so we should recycle it.
            bmp.recycle();
        }
    }

    public void add(GameObject object) {
        if (object instanceof GameObject) {
            if (object.getName().equals("Background2") ||
                    object.getName().equals("Background3") ||
                    object.getName().equals("Background4") ||
                    object.getName().equals("Background5") ||
                    object.getName().equals("Background1")) {
            //    Log.d(TAG,object.getName() + " added to RenderQueue" + mCurrentQueue + " at " + object.getDebugCounter());
            }
        }
        mRenderQueues[mCurrentQueue].add(object);
    }

    public void swap(GameRenderer renderer) {
        renderer.passToRenderer(mRenderQueues[mCurrentQueue]);
       // Log.v(TAG, "Current Queue = " + mCurrentQueue);

        mCurrentQueue = (mCurrentQueue + 1) % DRAW_QUEUE_COUNT;
        clearQueue();
    }

    public void clearQueue() {
        mRenderQueues[mCurrentQueue].clear();
    }

    public int getSize() { return mRenderQueues[mCurrentQueue].getSize(); }
}

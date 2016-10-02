package com.kaidenho.gamelooptest;

import android.util.Log;

/**
 * RenderSystem manages the two (or more) render queues used to draw the games.
 *
 * Created by Kaiden Ho on 2016-09-27.
 */
public class RenderSystem {
    private final static int DRAW_QUEUE_COUNT = 2;

    private RenderObjectManager[] mRenderQueues;
    private int mCurrentQueue = 0;

    public RenderSystem() {
        mRenderQueues = new RenderObjectManager[DRAW_QUEUE_COUNT];

        for (int i = 0; i < DRAW_QUEUE_COUNT; i++) {
            mRenderQueues[i] = new RenderObjectManager();
        }
    }

    public void swap(GameRenderer renderer) {
        renderer.setRenderQueue(mRenderQueues[mCurrentQueue]);
        clearQueue();
        mCurrentQueue = (mCurrentQueue + 1) % DRAW_QUEUE_COUNT;
    }

    public void add(GameObject object) {
        mRenderQueues[mCurrentQueue].add(object);
    }

    public void clearQueue() {
        mRenderQueues[mCurrentQueue].clear();
    }
}

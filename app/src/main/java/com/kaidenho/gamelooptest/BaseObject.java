package com.kaidenho.gamelooptest;

import android.content.Context;

/**
 * BaseObjects are updatable objects.
 *
 * Created by Kaiden Ho on 2016-09-26.
 */
public abstract class BaseObject {
    static RenderSystem renderSystem = new RenderSystem();

    // Update should be overridden in all children classes; otherwise, use the normal Object class
    public void update(long timeDelta) {
        // Base class does nothing
    }
}

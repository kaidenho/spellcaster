package com.kaidenho.gamelooptest;

/**
 * BaseObjects are updatable game objects.
 *
 * Created by Kaiden Ho on 2016-09-26.
 */
public abstract class BaseObject {
    static RenderSystem renderSystem = new RenderSystem();

    // Update should be overridden in all children classes
    public void update(long timeDelta) {
        // Base class does nothing
    }
}

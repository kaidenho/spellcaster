package com.kaidenho.gamelooptest;

import android.content.Context;

/**
 * Defines scaling variables
 *
 * Created by Kaiden Ho on 2016-10-03.
 */
public class Scaling {
    // Getters and setters?
    // Will these need to be changed to ints?
    public float gameUnit;
    public float gameHeight;

    public Scaling (Context context) {
        // The game scale is taken from the width, as it should be played in portrait view
        // To use scaling, treat the screen as 600 pixels width and then multiply by gameUnit
        gameUnit = (float)context.getResources().getDisplayMetrics().widthPixels / 600F;
        // the path enemies have to travel might be longer or shorter.
        // Additionally, UI elements at the top of the screen are measured against this
        gameHeight = (float)context.getResources().getDisplayMetrics().heightPixels / 600;
    }
}
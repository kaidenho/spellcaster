package com.kaidenho.gamelooptest;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

/**
 * Handles audio
 *
 * Created by kaidenho on 05/01/17.
 */
public class SoundSystem {
    private static final String TAG = SoundSystem.class.getSimpleName();

    private SoundPool mSoundManager;
    private int[] mSoundIDs;

    public SoundSystem() {

    }

    public void loadSounds(Context context, int[] soundIndexs) {
        if (Build.VERSION.SDK_INT > 21) {
            AudioAttributes attrs = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            mSoundManager = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(attrs)
                    .build();
        } else {
            mSoundManager = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        }

        mSoundIDs = new int[soundIndexs.length];
        for (int i = 0; i < soundIndexs.length; i++) {
            mSoundIDs[i] = mSoundManager.load(context, soundIndexs[i], 1);
        }
    }

    public void playSound(int soundIndex){
        Log.d(TAG, "Playing sound");
        mSoundManager.play(mSoundIDs[soundIndex], 1, 1, 1, 0, 1);
    }
}

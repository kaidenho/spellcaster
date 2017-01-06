package com.kaidenho.gamelooptest;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Sets up high level systems (GameManager, GameRenderer, GameRunnable)
 *
 * TODO: Passes UI input to GameRunnable
 *
 * Created by Kaiden Ho on 2016-09-26.
 */
public class Game {
    private final static String TAG = Game.class.getSimpleName();

    private ObjectManager mGameManager;
    private GameRenderer mRenderer;
    private GameRunnable mGameRunnable;
    private Thread mGame;

    private boolean mBootstrapComplete = false;

    private Context mContext;
    private Scaling mScaling;

    private Player mPlayer;
    private ObstacleManager mObstacleManager;
    private MagicManager mMagicManager;
    private ScoreManager mScoreManager;
    private SharedPreferences mSavedData;

    // Touch variables
    private float originX;
    private float originY;
    private boolean movementSwipe;

    public Game (Context context, SharedPreferences savedData) {
        Log.v(TAG, "Game created");
        mContext = context;
        mScaling = new Scaling(mContext);

        mSavedData = savedData;
    }


    public void bootstrap(){
        Log.v(TAG, "bootstrap called");

        mGameManager = new ObjectManager();

        // TODO: Move this to the gamethread?
        mPlayer = new Player(mContext, "GamePlayer");
        mGameManager.add(mPlayer);

        mObstacleManager = new ObstacleManager(mContext);
        mGameManager.add(mObstacleManager);

        mMagicManager = new MagicManager(mContext, mPlayer);
        mGameManager.add(mMagicManager);

        mScoreManager = new ScoreManager(mContext, new Rect(
                //200,200,400,0
                540,
                (int)mScaling.gameHeight,
                600,
                (int)mScaling.gameHeight - 60
        ));
        mGameManager.add(mScoreManager);

        int[] soundsToLoad = {
                R.raw.spell01   // 0
        };
        BaseObject.soundSystem.loadSounds(mContext, soundsToLoad);

        mRenderer = new GameRenderer(mContext);
        Log.v(TAG,"Renderer created");

        mGameRunnable = new GameRunnable(this);

        start();
        mBootstrapComplete = true;
    }

    public void handleTouchEvent(MotionEvent event) {
        // TODO: Create an collection of objects that respond to touch input and must to informed when touch events take place

        if (mBootstrapComplete) {
            float x = event.getX();
            // Inverting the y axis is preference, but I always think of 0 as being at the bottom
            float y = getContext().getResources().getDisplayMetrics().heightPixels - event.getY();

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                originX = x;
                originY = y;
            }

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (Math.abs(x - originX) > Math.abs(y - originY)) {    // swipe sideways
                    if(x - originX > 0) {       // swipe right
                        mPlayer.moveRight();
                    } else {                    // swipe left
                        mPlayer.moveLeft();
                    }

                } else if (y - originY > 0) {       // swipe up
                    mPlayer.shoot();
                }
            }
        }
    }

    public void start() {
        mGame = new Thread(mGameRunnable);
        mGame.setName("Game");
        mGame.start();
    }

    public void onPause() {
        Log.v(TAG, "onPause +");
        if(mBootstrapComplete) {
            mGameRunnable.pauseGame();
        }
        Log.v(TAG, "onPause -");

    }
    public void onResume() {
        Log.v(TAG, "onResume +");
        if (mGameRunnable != null) {
            mGameRunnable.resumeGame();
        }
        Log.v(TAG, "onResume -");
    }

    // Only call this when ready to save a new highscore, i.e. after the player has died
    public boolean checkNewHighScore() {
        Log.d(TAG, "New score = " + mScoreManager.getScore() + ", old score = " + mSavedData.getLong(mContext.getString(R.string.high_score), 0));
        if (mScoreManager.getScore() > mSavedData.getLong(mContext.getString(R.string.high_score), 0)) {
            Log.d(TAG, "High score of " + mScoreManager.getScore() + " saved");

            // New highscore has been set
            SharedPreferences.Editor editor = mSavedData.edit();
            editor.putLong(mContext.getString(R.string.high_score), mScoreManager.getScore());
            editor.commit();


            return true;
        }

        return false;
    }

    private boolean within(float x, float y, Rect rect) {
        Log.d("Within", "x " + x + ", y " + y + ", left " + rect.left + ", right " + rect.right + ", bottom " + rect.bottom + ", top " + rect.top);
        if (x < rect.left || x > rect.right || y < rect.bottom || y > rect.top) {
            return false;
        }
        return true;
    }


    public Context getContext() { return mContext; }

    public GameRenderer getRenderer() {
        return mRenderer;
    }

    public ObjectManager getGameManager() { return mGameManager; }

    public Player getPlayer() { return mPlayer; }

    public ObstacleManager getObstacleManager() { return mObstacleManager; }

    public MagicManager getMagicManager() { return mMagicManager; }

    public ScoreManager getScoreManager() { return mScoreManager; }

}

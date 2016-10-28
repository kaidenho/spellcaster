package com.kaidenho.gamelooptest;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
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

    // TODO: UN-HARDCODE THIS
    private Player player;
    private ObstacleManager obstacleManager;

    public Game (Context context) {
        Log.v(TAG, "Game created");
        mContext = context;
    }


    public void bootstrap(){
        Log.v(TAG, "bootstrap called");

        mGameManager = new ObjectManager();

        // TODO: Move this to the gamethread?
        player = new Player(mContext, "GamePlayer");
        mGameManager.add(player);

        obstacleManager = new ObstacleManager(mContext);
        mGameManager.add(obstacleManager);

        mRenderer = new GameRenderer(mContext);
        Log.v(TAG,"Renderer created");

        mGameRunnable = new GameRunnable(this);

        start();
        mBootstrapComplete = true;
    }

    public void handleTouchEvent(MotionEvent event) {
        // TODO: Work on this.
        // TODO: Add functionality for both swipe and touch events.
        // TODO: Create an collection of objects that respond to touch input and must to informed when touch events take place
        // TODO: DON'T HARDCODE THIS
        if (mBootstrapComplete) {
            // Send the touch event to the player
            player.onTouch(event);
        }
    }

    public void start() {
        mGame = new Thread(mGameRunnable);
        mGame.setName("Game");
        mGame.start();
    }

    public void onPause() {
        if(mBootstrapComplete) {
            mGameRunnable.pauseGame();
        }
    }
    public void onResume() {
        if (mGameRunnable != null) {
            //TODO: This is causing as error
          //  mGameRunnable.resumeGame();
        }
    }

    public GameRenderer getRenderer() {
        return mRenderer;
    }

    public ObjectManager getGameManager() { return mGameManager; }

    public Player getPlayer() { return player; }

    public ObstacleManager getObstacleManager() { return obstacleManager; }
}

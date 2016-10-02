package com.kaidenho.gamelooptest;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;

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

    private Context mContext;

    public Game (Context context) {
        mContext = context;
    }


    public void bootstrap(){
        mGameManager = new ObjectManager();

        GameObject player = new GameObject(mContext);
        mGameManager.add(player);

        mRenderer = new GameRenderer(mContext);
        Log.d(TAG,"Renderer created");

        mGameRunnable = new GameRunnable(mRenderer);
        mGameRunnable.setGameManager(mGameManager);

        start();
    }

    public void start() {
        mGame = new Thread(mGameRunnable);
        mGame.setName("Game");
        mGame.start();
    }

    public GameRenderer getRenderer() {
        return mRenderer;
    }
}

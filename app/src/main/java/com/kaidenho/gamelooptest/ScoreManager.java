package com.kaidenho.gamelooptest;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by kaidenho on 29/12/16.
 */
public class ScoreManager extends ObjectManager {
    private static final String TAG = ScoreManager.class.getSimpleName();

    private static final int SCORE_INCREASE_PER_SECOND = 2;

    // Image texture index, find in RenderSystem
    private static final int ZERO_TEXTURE_INDEX = 6;

    private Context mContext;
    private Scaling mScaling;

    private float mScore;
    private long mDisplayedScore = -1;

    private RectF mScoreLocationRect;

    public ScoreManager(Context context, RectF locationRect) {
        mContext = context;
        mScaling = new Scaling(mContext);
        mScoreLocationRect = new RectF(locationRect);
        Log.d(TAG,"Location rect given: " + mScoreLocationRect);
    }


    @Override
    public void update(long timeDelta){
        //Log.d(TAG,"Location rect: " + mScoreLocationRect);
        // Increase score
        mScore += ((float)timeDelta * SCORE_INCREASE_PER_SECOND) / 1000;

        //Log.d(TAG, "Score is " + mScore);

        // score is modified. Also: only rounding from float to int once
        int score = (int)mScore;

        if (score < 1) {
            if (score > mDisplayedScore) {
                //Log.d(TAG,"Location rect before 0: " + mScoreLocationRect);
                mObjects.add(new GameObject(
                        ZERO_TEXTURE_INDEX,
                        mScoreLocationRect,
                        "Score Digit"
                ));
                mDisplayedScore = 0;
                //Log.d(TAG,"Location rect at 0: " + mScoreLocationRect);
            }
        /*} else if (score < 2) {
            if (mDisplayedScore != 1) {
                if(mObjects.size() < 1) {
                    mObjects.add(new GameObject(
                            ZERO_TEXTURE_INDEX + 1,
                            mScoreLocationRect,
                            mContext,
                            "Score Digit"
                    ));
                } else {
                    ((GameObject) mObjects.get(0)).changeTextureIndex(ZERO_TEXTURE_INDEX + 1);
                }
                mDisplayedScore = 1;
            }*/
        } else if (score > 1) {
            if (score > mDisplayedScore) {
                mDisplayedScore = score;    // have to save 'score' before it is broken down into digits

                //Log.d(TAG, "Score " + score + ", displayscore " + mDisplayedScore);
                int i = 0;

                RectF digitLocationRect = new RectF(mScoreLocationRect);   // Because the digits have to be drawn in different locations, but the score location shouldn't change
                //Log.d(TAG,"Location rect: " + mLocationRect + ", digit rect: " + digitLocationRect);

                // 'score >= 1' because the while loop changes the value of score, dividing it: a score of '10' will be render as '0' without this
                while (score >= 1) {
                    if (mObjects.size() > i) {
                        ((GameObject) mObjects.get(i)).setTextureIndex(ZERO_TEXTURE_INDEX + (int)(Math.floor(score) % 10));
                       // Log.d(TAG, i + " digit location is " + digitLocationRect);
                    } else {
                        mObjects.add(new GameObject(
                                ZERO_TEXTURE_INDEX + (int)(Math.floor(score) % 10),
                                digitLocationRect,
                                "Score Digit"
                        ));
                        //Log.d(TAG,"New digit location is " + digitLocationRect);
                    }

                    score /= 10;
                    i++;
                    digitLocationRect.left -= 60;
                    digitLocationRect.right -= 60;
                }
                BaseObject.scrollSpeed += 2;
                Log.d(TAG,"New Scroll speed = " + BaseObject.scrollSpeed);
            }
        }

        super.update(timeDelta);
    }

    public long getScore() {
        return mDisplayedScore;
    }

    public void setScore(long score) {
        mScore = score;
    }

    private void setLocationRect(RectF locationRect) {
        mScoreLocationRect = locationRect;
        Log.d(TAG, "New locationRect = " + locationRect);
    }
}

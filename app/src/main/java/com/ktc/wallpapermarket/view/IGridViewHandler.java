package com.ktc.wallpapermarket.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.GridView;

import com.ktc.wallpapermarket.utils.Constants;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengjw on 2018/3/14.
 */

public class IGridViewHandler implements IGridViewScal {

    private List<File> mList = new ArrayList<>();
    protected long mDurationTraslate = 600;
    private Context mContext;
    protected View lastFocus, oldLastFocus;
    protected AnimatorSet mAnimatorSet;
    protected List<Animator> mAnimatorList = new ArrayList<>();
    protected View mTarget;
    protected GridView mGridView;
    protected boolean mScalable = true;
    protected float mScale = 1.1f;
    protected int lineNumber;
    private int currentSelectPosition = -1;

    public static final String SELECTACTION = "com.ktc.wallpapermarket.igridviewhandler";

    protected List<IGridViewHandler.FocusListener> mFocusListener = new ArrayList<IGridViewHandler.FocusListener>(1);
    protected List<Animator.AnimatorListener> mAnimatorListener = new ArrayList<Animator.AnimatorListener>(1);

    public IGridViewHandler(Context context, ViewGroup gridView){
        this.mContext = context;
        this.mGridView = (GridView) gridView;
    }

    public IGridViewHandler(Context context, GridView gridView, List<File> tmpArray){
        Constants.debug("IGridViewHandler");
        this.mContext = context;
        this.mGridView = gridView;
        setListener(gridView);
        this.mList = tmpArray;
    }

    private void setListener(final GridView gridView){

        Constants.debug("setListener");

        gridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Constants.debug("onItemSelected position : " + position);
                getScaleAnimator(view, true, position);
                currentSelectPosition = position;
                Intent intent = new Intent(SELECTACTION);
                intent.putExtra("position", position);
                mContext.sendBroadcast(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Constants.debug("onNothingSelected");
            }
        });

    }

    public void setSelection(int position) {
        mGridView.setSelection(position);
    }

    public interface FocusListener{
        void onFocusChanged(View oldFocus, View newFocus);
    }

    @Override
    public void onFocusChanged(View target, View oldFocus, View newFocus) {
        Constants.debug("onFocusChanged");
        if (oldFocus == newFocus) {
            return;
        }

        if (mAnimatorSet != null && mAnimatorSet.isRunning()) {
            mAnimatorSet.end();
        }
        lastFocus = newFocus;
        oldLastFocus = oldFocus;
        mTarget = target;
        for (IGridViewHandler.FocusListener f : this.mFocusListener) {
            f.onFocusChanged(oldFocus, newFocus);
        }

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new DecelerateInterpolator(1));
        animatorSet.setDuration(mDurationTraslate);
        animatorSet.playTogether(mAnimatorList);

        for (Animator.AnimatorListener listener : mAnimatorListener) {
            animatorSet.addListener(listener);
        }
    }

    private AnimationSet mAnimationSet;
    public List<Animator> getScaleAnimator(View view, boolean isScale, int position){
        List<Animator> animatorList = new ArrayList<>(2);
        if (!mScalable){
            return animatorList;
        }
        lineNumber = this.mGridView.getNumColumns();
        Constants.debug("lineNumber : " + lineNumber);
        if (!mGridView.isFocused()){
            Constants.debug("mGridView.isFocused() true");
            return null;
        }
        try {
            float scaleBefore = 1.0f;
            float scaleAfter = mScale;
            if (!isScale){
                scaleBefore = mScale;
                scaleAfter = 1.0f;
            }
            AnimationSet animationSet = new AnimationSet(true);
            if (mAnimationSet != null && mAnimationSet != animationSet){
                mAnimationSet.setFillAfter(false);
                mAnimationSet.cancel();
            }
            ScaleAnimation scaleAnimation = null;
            position = position - mGridView.getFirstVisiblePosition();
            Constants.debug("position : " + position);

            switch (position){
                case 0:
                    Constants.debug("0");
                    scaleAnimation = new ScaleAnimation(scaleBefore, scaleAfter, scaleBefore, scaleAfter,
                            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_PARENT, 0f);
                    break;
                case 1:
                    Constants.debug("1");
                    scaleAnimation = new ScaleAnimation(scaleBefore, scaleAfter, scaleBefore, scaleAfter,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_PARENT, 0f);
                    break;
                case 2:
                    Constants.debug("2");
                    scaleAnimation = new ScaleAnimation(scaleBefore, scaleAfter, scaleBefore, scaleAfter,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_PARENT, 0f);
                    break;
                case 3:
                    Constants.debug("3");
                    scaleAnimation = new ScaleAnimation(scaleBefore, scaleAfter, scaleBefore, scaleAfter,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_PARENT, 0.0f);
                    break;
                case 4:
                    Constants.debug("4");
                    scaleAnimation = new ScaleAnimation(scaleBefore, scaleAfter, scaleBefore, scaleAfter,
                            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_PARENT, 0f);
                    break;
                case 5:
                    Constants.debug("5");
                    scaleAnimation = new ScaleAnimation(scaleBefore, scaleAfter, scaleBefore, scaleAfter,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_PARENT, 0f);
                    break;
                case 6:
                    Constants.debug("6");
                    scaleAnimation = new ScaleAnimation(scaleBefore, scaleAfter, scaleBefore, scaleAfter,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_PARENT, 0f);
                    break;
                case 7:
                    Constants.debug("7");
                    scaleAnimation = new ScaleAnimation(scaleBefore, scaleAfter, scaleBefore, scaleAfter,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_PARENT, 0.0f);
                    break;
                case 8:
                    Constants.debug("8");
                    scaleAnimation = new ScaleAnimation(scaleBefore, scaleAfter, scaleBefore, scaleAfter,
                            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
                    break;
                case 9:
                    Constants.debug("9");
                    scaleAnimation = new ScaleAnimation(scaleBefore, scaleAfter, scaleBefore, scaleAfter,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
                    break;
                case 10:
                    Constants.debug("10");
                    scaleAnimation = new ScaleAnimation(scaleBefore, scaleAfter, scaleBefore, scaleAfter,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
                    break;
                case 11:
                    Constants.debug("11");
                    scaleAnimation = new ScaleAnimation(scaleBefore, scaleAfter, scaleBefore, scaleAfter,
                            Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f);
                    break;
                default:
                    break;
            }

            scaleAnimation.setDuration(300);
            animationSet.addAnimation(scaleAnimation);
            animationSet.setFillAfter(true);
            view.startAnimation(animationSet);
            mAnimationSet = animationSet;
        }catch (Exception e){
            e.printStackTrace();
        }
        return animatorList;
    }


}

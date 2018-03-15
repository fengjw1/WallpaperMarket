package com.ktc.wallpapermarket.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

import com.ktc.wallpapermarket.utils.Constants;

/**
 * Created by fengjw on 2018/3/12.
 */

public class MyGridView extends GridView {

    public boolean isOnMeasure;

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setChildrenDrawingOrderEnabled(true);
    }

    @Override
    public void setSelection(int position) {
        Constants.debug("MyGridView setSelection position : " + position);
        super.setSelection(position);
    }



    @Override
    protected void setChildrenDrawingOrderEnabled(boolean enabled) {
        super.setChildrenDrawingOrderEnabled(enabled);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        isOnMeasure = true;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        isOnMeasure = false;
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (this.getSelectedItemPosition() != -1){
            if (i + this.getFirstVisiblePosition() == this.getSelectedItemPosition()){
                return childCount - 1;
            }
            if (i == childCount - 1){
                return this.getSelectedItemPosition() - this.getFirstVisiblePosition();
            }
        }
        return i;
    }
}

package com.android.hifosystem.hifoevaluatevalue.recycle_pages;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.hifosystem.hifoevaluatevalue.R;


/**
 * 文件名： ListViewItemDecotion
 * 功能：
 * 作者： wanny
 * 时间： 9:49 2016/7/19
 */
public class ListViewItemDecotion extends RecyclerView.ItemDecoration {


    public static final int ORIVATION_VERCAL = LinearLayoutManager.VERTICAL;
    public static final int ORIVATION_HONRNAL = LinearLayoutManager.HORIZONTAL;
    private Drawable mDrawable;
    public ListViewItemDecotion() {
        super();
    }

    private int orientation;

    public ListViewItemDecotion(Context context, int orientation) {
        mDrawable = ContextCompat.getDrawable(context, R.drawable.listview_itemdec_drawable);
        setOrvation(orientation);
    }

    public ListViewItemDecotion(Context context, int orientation, int resdId) {
        mDrawable = ContextCompat.getDrawable(context, resdId);
        setOrvation(orientation);
    }

    public void setOrvation(int orivation) {
        if (orientation != ORIVATION_HONRNAL && orientation != ORIVATION_VERCAL) {
            throw new IllegalArgumentException("invalid orientation");
        }
        this.orientation = orivation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if(orientation == ORIVATION_VERCAL){
            drawVercal(c,parent);
        }else if(orientation == ORIVATION_HONRNAL){
            drawHonral(c,parent);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }
    //竖向的绘制
    private void drawVercal(Canvas c , RecyclerView parent){
        int left = parent.getPaddingLeft() ;
        int right = parent.getWidth() - parent.getPaddingRight();
        for(int i = 0 ; i < parent.getChildCount() ; i ++){
            View childView = parent.getChildAt(i);
            if(childView.getVisibility() != View.GONE){
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childView
                        .getLayoutParams();
                final int top = childView.getBottom() + params.bottomMargin;
                final int bottom = top + mDrawable.getIntrinsicHeight();
                mDrawable.setBounds(left, top, right, bottom);
                mDrawable.draw(c);
            }
        }

    }
    //横向的绘制
    public void drawHonral(Canvas c , RecyclerView parent){
       int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        for (int i = 0 ; i < parent.getChildCount() ; i ++){
            View childView = parent.getChildAt(i);
            if(childView.getVisibility() != View.GONE){
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)childView.getLayoutParams();
                final int left = params.rightMargin + childView.getRight();
                final int right = left + mDrawable.getIntrinsicHeight();
                mDrawable.setBounds(left,top,right,bottom);
                mDrawable.draw(c);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if(orientation == ORIVATION_HONRNAL){
            outRect.set(0,0,mDrawable.getIntrinsicHeight(),0);
        }else if(orientation == ORIVATION_VERCAL){
            outRect.set(0,0,0,mDrawable.getIntrinsicHeight());
        }
    }
}

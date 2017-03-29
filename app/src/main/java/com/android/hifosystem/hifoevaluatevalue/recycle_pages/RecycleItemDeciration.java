package com.android.hifosystem.hifoevaluatevalue.recycle_pages;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.hifosystem.hifoevaluatevalue.R;


/**
 * 文件名： RecycleItemDeciration
 * 功能：
 * 作者： wanny
 * 时间： 15:35 2016/7/12
 */
public class RecycleItemDeciration extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    private Drawable mDivider;
    private int mOrientation;

    public RecycleItemDeciration(Context context, int orientation) {
        //获取到这个Drawable对象。注意：此处我们可以用自己的Drawable
        setOrientation(orientation);
        mDivider = ContextCompat.getDrawable(context, R.drawable.gridview_itemdecoration_bg);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }
    //真正的绘制Divider

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin +
                    Math.round(ViewCompat.getTranslationY(child));
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin +
                    Math.round(ViewCompat.getTranslationX(child));
            final int right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
    // outRect限制了我们的分割线的外形尺寸


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
            drawHorizontal(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, mDivider.getIntrinsicHeight(), mDivider.getIntrinsicHeight());
//             if(isLastColum(parent,view)){
//                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
//            }else{
//
//            }
        }
    }

//    /**
//     *
//     * 判断是不是最后一行，竖直方向的走势
//     *
//     * @param parent     RecyclerView 对象
//     * @param view       当前的View的对象
//     * @return
//     */
//    private boolean isLastRaw(RecyclerView parent, View view) {
//        int spanCount = getSpanCountByParent(parent);
//        int childCount = parent.getChildCount();
//        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
//        //当前View的position; 调用父类的方法。
//        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
//        if (layoutManager instanceof GridLayoutManager) {
//            //取余数
//            childCount = childCount - childCount % spanCount;
//            //如何判断当前的position是最后一个position>??
//            //大于是不可能的但是如果当
//            if (position >= childCount) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//
//    /**
//     * 通过RecyclerView来获取
//     *
//     * @param parent
//     * @return
//     */
//    private int getSpanCountByParent(RecyclerView parent) {
//        int spanCount = -1;
//        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
//        if (layoutManager != null) {
//            if (layoutManager instanceof GridLayoutManager) {
//                spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
//            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
//                spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
//            }
//        }
//        return spanCount;
//    }
//
//
//    //
//    private boolean isLastColum(RecyclerView parent, View view) {
//        int spanCount = getSpanCountByParent(parent);
//        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
//        if (layoutManager instanceof GridLayoutManager) {
//            int position = ((GridLayoutManager.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
//            if ((position + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
//            {
//                return true;
//            }
//        }
//        return false;
//    }

}

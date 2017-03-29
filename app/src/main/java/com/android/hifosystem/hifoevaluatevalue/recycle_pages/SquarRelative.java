/**
 *
 */
package com.android.hifosystem.hifoevaluatevalue.recycle_pages;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 类名：  @SquarRelative.java
 * 作者：  wanny
 * 功能： 正方形的布局文件
 * 时间：  2014年8月26日 下午4:05:07
 */
public class SquarRelative extends RelativeLayout {

    public SquarRelative(Context context) {
        super(context);
    }

    public SquarRelative(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SquarRelative(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //设置为正方形
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int childWidthSize = getMeasuredWidth();
        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}

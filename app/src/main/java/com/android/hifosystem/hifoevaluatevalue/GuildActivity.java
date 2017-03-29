package com.android.hifosystem.hifoevaluatevalue;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.hifosystem.hifoevaluatevalue.Utils.AppUtils;
import com.android.hifosystem.hifoevaluatevalue.Utils.PreferenceUtil;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;
/**
 * 文件名： GuildActivity
 * 功能：
 * 作者： wanny
 * 时间： 10:59 2016/11/28
 */
public class GuildActivity extends AppCompatActivity {

    //进入app页面
    @BindView(R.id.guid_viewpage)
    ViewPager guidViewpage;
    //进入按钮
    @BindView(R.id.start_guild)
    TextView startGuild;
    //下面指示效果
    @BindView(R.id.guild_indicator)
    CircleIndicator guildIndicator;
    private ArrayList<View> views;
    private GuildPageAdapter adapter;
    private ArrayList<Integer> sourceId;
    //发生滑动的临界距离
    private int touchSlop ;
    //当前选中的位置
    private int currentPosition = -1;
    //手势监听
    private GestureDetector gestureDetector;

    //初始化数据
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        }else{
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        setContentView(R.layout.guild_activity_view);
        ButterKnife.bind(this);
        gestureDetector = new GestureDetector(this,new GuideViewTouch());
        setData();
    }
    private void setData() {
        touchSlop = AppUtils.getScreenWidthPiex(this) / 7 ;
        if (views == null) {
            views = new ArrayList<>();
        } else {
            views.clear();
        }
        if(sourceId == null){
            sourceId = new ArrayList<>();
        }else{
            sourceId.clear();
        }
        sourceId.add(R.mipmap.icon_guide_one);
        sourceId.add(R.mipmap.icon_guide_two);
        sourceId.add(R.mipmap.icon_guide_three);
        sourceId.add(R.mipmap.icon_guide_four);
        for(int i = 0 ; i < sourceId.size() ; i++){
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            views.add(imageView);
        }
        GuildImp<Integer> guildImp = new LocalImageLoad();
        guildImp.setData(views,sourceId);
        adapter = new GuildPageAdapter(views);
        guidViewpage.setAdapter(adapter);
        guildIndicator.setViewPager(guidViewpage);
        guidViewpage.addOnPageChangeListener(pageChangeListener);
        currentPosition = 0;
    }


    private class GuideViewTouch extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            //当前的页面是第3，滚动的距离。
            if (currentPosition == 3) {
                if (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY() - e2.getY())&& (e2.getX() - e1.getX() <= (- touchSlop) || e1
                        .getX() - e2.getX() >= touchSlop)) {
                    if (e1.getX() - e2.getX() >= touchSlop) {
                        startBackActivity();
                        return true;
                    }
                }
            }
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(currentPosition != -1){
            colorChange(currentPosition);
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            event.setAction(MotionEvent.ACTION_CANCEL);
        }
        return super.dispatchTouchEvent(event);
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentPosition = position;
            colorChange(position);
            if (position == sourceId.size() - 1) {
                startGuild.setVisibility(View.VISIBLE);
            } else {
                startGuild.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };





    private void colorChange(int position) {
        // 用来提取颜色的Bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                sourceId.get(position));
        // Palette的部分
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener(){
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant = palette.getVibrantSwatch();
				/* 界面颜色UI统一性处理,看起来更Material一些 */
                if(vibrant == null){
                    vibrant = palette.getLightVibrantSwatch();
                }
                if(vibrant == null){
                    return;
                }
//                mPagerSlidingTabStrip.setBackgroundColor(vibrant.getRgb());
//                mPagerSlidingTabStrip.setTextColor(vibrant.getTitleTextColor());
//                // 其中状态栏、游标、底部导航栏的颜色需要加深一下，也可以不加，具体情况在代码之后说明
//                mPagerSlidingTabStrip.setIndicatorColor(colorBurn(vibrant.getRgb()));
//                mToolbar.setBackgroundColor(vibrant.getRgb());
                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    Window window = getWindow();
                    // 很明显，这两货是新API才有的。
                    window.setStatusBarColor(colorBurn(vibrant.getRgb()));
                    window.setNavigationBarColor(colorBurn(vibrant.getRgb()));
                }
            }
        });
        //建造者模式替代原始的代码操作
    }

    /**
     * 颜色加深处理
     *
     * @param RGBValues
     *            RGB的值，由alpha（透明度）、red（红）、green（绿）、blue（蓝）构成，
     *            Android中我们一般使用它的16进制，
     *            例如："#FFAABBCC",最左边到最右每两个字母就是代表alpha（透明度）、
     *            red（红）、green（绿）、blue（蓝）。每种颜色值占一个字节(8位)，值域0~255
     *            所以下面使用移位的方法可以得到每种颜色的值，然后每种颜色值减小一下，在合成RGB颜色，颜色就会看起来深一些了
     * @return
     */
    private int colorBurn(int RGBValues) {
        int alpha = RGBValues >> 24;
        int red = RGBValues >> 16 & 0xFF;
        int green = RGBValues >> 8 & 0xFF;
        int blue = RGBValues & 0xFF;
        red = (int) Math.floor(red * (1 - 0.1));
        green = (int) Math.floor(green * (1 - 0.1));
        blue = (int) Math.floor(blue * (1 - 0.1));
        return Color.rgb(red, green, blue);
    }

    @OnClick(R.id.start_guild)
    void startActivity(View view){

        startBackActivity();
    }

    private void startBackActivity(){
//        Intent intent = new Intent(GuildActivity.this, GongPingOldActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PreferenceUtil.getInstance(getApplicationContext()).saveBoolean("isFirst", false);
//        PreferenceUtil.getInstance(getApplicationContext()).saveBoolean("locationFirst",true);
//        PreferenceUtil.getInstance(getApplicationContext()).saveBoolean("historyFirst",true);
//        startActivity(intent);
        finish();
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            startBackActivity();
            return  true;
        }
        return super.onKeyDown(keyCode, event);
    }



}

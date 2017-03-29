package com.android.hifosystem.hifoevaluatevalue.camera_view.gallery_mvp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;


import com.android.hifosystem.hifoevaluatevalue.DBFileImage.FileDB.LocalFileEntity;
import com.android.hifosystem.hifoevaluatevalue.R;
import com.android.hifosystem.hifoevaluatevalue.camera_view.PhotoView;
import com.android.hifosystem.hifoevaluatevalue.camera_view.PhotoViewAttacher;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

/**
 * 类名： GalleryPageAdapter
 * 工鞥： 适配器viewpager
 * 作者： wanny
 * 时间：${date} ${time}
 */

public class GalleryPageAdapter<T> extends PagerAdapter {
    private ArrayList<T> data;
    private Context context;
    private Activity activity;
    private String flag;

    /**
     * @param context
     * @param activity
     * @param data
     * @param flag
     */
    public GalleryPageAdapter(Context context, Activity activity, ArrayList<T> data, String flag) {
        this.context = context;
        this.activity = activity;
        this.data = data;
        this.flag = flag;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        PhotoView img = new PhotoView(context);
        img.setBackgroundColor(ContextCompat.getColor(context, R.color.trans_bg1));

        if (data.get(position) instanceof LocalFileEntity) {
            Glide.with(activity)
                    .load(Uri.fromFile(new File(((LocalFileEntity) data.get(position)).getLocalFilePath())))
                    .dontAnimate()
                    .into(img);
        }
        if (flag.equals("network")) {
            if (data.get(position) instanceof PhotoInfoEntity) {
                Glide.with(activity)
                        .load(((PhotoInfoEntity) data.get(position)).getPath_local())
                        .dontAnimate()
                        .into(img);

            } else if (data.get(position) instanceof FileEntity) {
                Glide.with(activity)
                        .load(((FileEntity) data.get(position)).getFastPath())
                        .dontAnimate()
                        .into(img);
            }
        } else {
            if (data.get(position) instanceof LocalFileEntity) {
                Glide.with(activity)
                        .load(Uri.fromFile(new File(((LocalFileEntity) data.get(position)).getLocalFilePath())))
                        .dontAnimate()
                        .into(img);
            } else if (data.get(position) instanceof PhotoInfoEntity) {
                Glide.with(activity)
                        .load(Uri.fromFile(new File(((PhotoInfoEntity) data.get(position)).getPath_local())))
                        .dontAnimate()
                        .into(img);
            }
        }
        img.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));

        img.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (onImageClickListener != null) {
                    onImageClickListener.imageClick(position);
                }
            }
        });
        container.addView(img);
        return img;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((PhotoView) object);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((PhotoView) object);
    }

    private OnImageClickListener onImageClickListener;

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    public interface OnImageClickListener {
        void imageClick(int position);

    }

}

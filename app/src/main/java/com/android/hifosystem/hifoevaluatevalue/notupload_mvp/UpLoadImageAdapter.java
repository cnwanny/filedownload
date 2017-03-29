package com.android.hifosystem.hifoevaluatevalue.notupload_mvp;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.hifosystem.hifoevaluatevalue.DBFileImage.FileDB.LocalFileEntity;
import com.android.hifosystem.hifoevaluatevalue.R;
import com.android.hifosystem.hifoevaluatevalue.Utils.AppUtils;
import com.android.hifosystem.hifoevaluatevalue.framework_fileoperate.SDCardImageLoader;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 类名： UpLoadImageAdapter
 * 工鞥：  图片上传进度数据更新
 * 作者： wanny
 * 时间：${date} ${time}
 */

public class UpLoadImageAdapter extends BaseAdapter {

    private ArrayList<LocalFileEntity> data;
    private Context context;
    private Activity activity;
    private SDCardImageLoader sdCardImageLoader;

    public UpLoadImageAdapter(ArrayList<LocalFileEntity> data, Context context, Activity activity) {
        this.data = data;
        this.context = context;
        this.activity = activity;
        sdCardImageLoader = new SDCardImageLoader(AppUtils.getScreenWidthPiex(activity), AppUtils.getScreenHeightPiex(activity));
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public LocalFileEntity getItem(int position) {
        return data.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.image_upload_item_view, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position != 0) {
            viewHolder.imageUploadProgress.setProgress(0);
        }
        if (data != null && data.size() > 0) {
            LocalFileEntity entity = data.get(position);
            if (entity != null) {
                if (!TextUtils.isEmpty(entity.getLocalFilePath())) {
                    Glide.with(activity)
                            .load(Uri.fromFile(new File(entity.getLocalFilePath())))
                            .dontAnimate()
                            .into(  viewHolder.imageUploadImageview);
                } else {
                    viewHolder.imageUploadImageview.setImageResource(R.drawable.icon_empty);
                }
                if (!TextUtils.isEmpty(entity.getFileName())) {
                    viewHolder.imageUploadText.setText(entity.getFileName());
                } else {
                    viewHolder.imageUploadText.setText("");
                }
                viewHolder.imageUploadProgressText.setText("队列中");
            }
        }
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    //更新数据
    public void updataView(int position, ListView listView, int progress, String progress_text) {
        int visibleFirstPosi = listView.getFirstVisiblePosition();
        int visibleLastPosi = listView.getLastVisiblePosition();
        if (position >= visibleFirstPosi && position <= visibleLastPosi) {
            View view = listView.getChildAt(position);
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.imageUploadProgressText.setText(progress_text);
            holder.imageUploadProgressText.setTextColor(ContextCompat.getColor(context, R.color.blue));
            holder.imageUploadProgress.setProgress(progress);
        }
    }

    static class ViewHolder {
        @BindView(R.id.image_upload_imageview)
        ImageView imageUploadImageview;
        @BindView(R.id.image_upload_text)
        TextView imageUploadText;
        @BindView(R.id.image_upload_progress_text)
        TextView imageUploadProgressText;
        @BindView(R.id.image_upload_progress)
        ProgressBar imageUploadProgress;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

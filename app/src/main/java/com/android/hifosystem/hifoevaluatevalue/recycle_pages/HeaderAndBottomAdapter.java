package com.android.hifosystem.hifoevaluatevalue.recycle_pages;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.android.hifosystem.hifoevaluatevalue.DBFileImage.FileDB.LocalFileEntity;
import com.android.hifosystem.hifoevaluatevalue.R;
import com.android.hifosystem.hifoevaluatevalue.Utils.AppUtils;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文件名： HeaderAndBottomAdapter
 * 功能：
 * 作者： wanny
 * 时间： 15:55 2016/8/29
 */

public class HeaderAndBottomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //item类型
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_BOTTOM = 2;
    //模拟数据
    private int mHeaderCount = 0;//头部View个数
    private int mBottomCount = 1;//底部View个数
    private ArrayList<LocalFileEntity> dataList;
    private Activity activity;

    //操作方式 添加房间，
    public static final int MODE_ROOM = 0x0001;
    //操作方式 添加权属状况
    public static final int MODE_AUTHORITY = 0x0002;
    //操作方式 只显示
    public static final int MODE_SHOW = 0x0003;

    private AdapterListener adapterListener;
    //长按或者全部删除
    public static final int OPETATE_EDIT = 0x0006;
    public static final int OPERATE_NORMAl = 0x0009;
    private int operateMode = OPERATE_NORMAl;
    private Context context;

    public HeaderAndBottomAdapter(ArrayList<LocalFileEntity> dataList, Activity activity) {
      this.dataList = dataList;
        this.activity = activity;
    }

    //内容长度
    public int getContentItemCount() {
        return dataList.size();
    }

    //判断当前item是否是HeadView
    public boolean isHeaderView(int position) {
        return mHeaderCount != 0 && position < mHeaderCount;
    }

    //判断当前item是否是FooterView
    public boolean isBottomView(int position) {
        return mBottomCount != 0 && position >= (mHeaderCount + getContentItemCount());
    }


    public void setOperateMode(int mode){
        this.operateMode = mode;
        notifyDataSetChanged();
    }



    public void setAdapterListener(AdapterListener adapterListener) {
        this.adapterListener = adapterListener;
    }

    //判断当前item类型
    @Override
    public int getItemViewType(int position) {
        int dataItemCount = getContentItemCount();
        if (mHeaderCount != 0 && position < mHeaderCount) {
            //头部View
            return ITEM_TYPE_HEADER;
        } else if (mBottomCount != 0 && position >= (mHeaderCount + dataItemCount)) {
            //底部View
            return ITEM_TYPE_BOTTOM;
        } else {
            //内容View
            return ITEM_TYPE_CONTENT;
        }
    }

    //内容 ViewHolder
    public static class ContentViewHolder extends RecyclerView.ViewHolder {

        //坐标的图标
        @BindView(R.id.image_item_main)
        LinearLayout addSurvey_image_linear;
        //图片
        @BindView(R.id.image_item_image)
        SquareImageView addSurvey_image_bg;
        //分类
        @BindView(R.id.image_item_type)
        TextView addSurvey_image_type;

        public ContentViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
//            private TextView textView;
//            public ContentViewHolder(View itemView) {
//                super(itemView);
//                textView=(TextView)itemView.findViewById(R.id.tv_item_text);
//            }
    }

    //头部 ViewHolder
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    //底部 ViewHolder
    public static class BottomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ordinal_bottom_operate_tv)
        TextView applyReplyAddsure;

        public BottomViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == ITEM_TYPE_HEADER) {
            return null;
        } else if (viewType == ITEM_TYPE_CONTENT) {
            return new ContentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item_view, parent, false));
        } else if (viewType == ITEM_TYPE_BOTTOM) {
            return new BottomViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ordinal_bottomstyle_view, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
           //拍完照之后
        } else if (holder instanceof ContentViewHolder) {
            operateItem(holder,position);
        } else if (holder instanceof BottomViewHolder) {
            operateFootView(holder,position);
        }
    }

    private void operateItem(RecyclerView.ViewHolder holder, final int position) {

        if (dataList.size() <= 1) {
            ((ContentViewHolder) holder).addSurvey_image_bg.setImageResource(R.mipmap.icon_addroompic_bg);
            AppUtils.notShowView(((ContentViewHolder) holder).addSurvey_image_type);
        } else {
            if (position == dataList.size() -  1) {
                ((ContentViewHolder) holder).addSurvey_image_bg.setImageResource(R.mipmap.icon_addroompic_bg);
                 AppUtils.notShowView(((ContentViewHolder) holder).addSurvey_image_type);
            } else {
                final LocalFileEntity entity = dataList.get(position);
                AppUtils.showView(((ContentViewHolder) holder).addSurvey_image_type);
                if (entity != null) {
                    if(!TextUtils.isEmpty(entity.getLocalFilePath())){
                        Glide.with(activity)
                                .load(Uri.fromFile(new File(entity.getLocalFilePath())))
                                .dontAnimate()
                                .into(((ContentViewHolder) holder).addSurvey_image_bg);
                    }
                    if (operateMode == OPETATE_EDIT) {
                        ((ContentViewHolder) holder).addSurvey_image_type.setText("删除");
                        ((ContentViewHolder) holder).addSurvey_image_type.setTextColor(ContextCompat.getColor(context, R.color.red));
                        ((ContentViewHolder) holder).addSurvey_image_type.setTextSize(18);
                    } else {
                        ((ContentViewHolder) holder).addSurvey_image_type.setTextColor(ContextCompat.getColor(context, R.color.blue));
                        ((ContentViewHolder) holder).addSurvey_image_type.setTextSize(16);
                        if (!TextUtils.isEmpty(entity.getCategoryName())) {
                            ((ContentViewHolder) holder).addSurvey_image_type.setText(entity.getCategoryName());
                        } else {
                            ((ContentViewHolder) holder).addSurvey_image_type.setText("");
                        }
                    }

//                    if(!TextUtils.isEmpty(entity.getCategoryName())){
//                        ((ContentViewHolder) holder).addSurvey_image_type.setText(entity.getCategoryName());
//                    }else{
//                        ((ContentViewHolder) holder).addSurvey_image_type.setText("请选择");
//                    }
                }
            }
        }
        if (position == dataList.size() - 1) {
            ((ContentViewHolder) holder).addSurvey_image_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (adapterListener != null) {
                        //添加数据
                        adapterListener.addImage(1);
                    }
                }
            });
        } else {

            ((ContentViewHolder) holder).addSurvey_image_bg.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(addSurveyImgeListener != null){
                        addSurveyImgeListener.onLongClick();
                    }
                    return false;
                }
            });
            ((ContentViewHolder) holder).addSurvey_image_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(addSurveyImgeListener != null){
                        addSurveyImgeListener.skanImage(operateMode,position);
                    }
                }
            });

            ((ContentViewHolder) holder).addSurvey_image_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        if(operateMode == OPETATE_EDIT){
                            if (addSurveyImgeListener != null) {
                                //删除
                                addSurveyImgeListener.delete(position);
                            }
                        }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mHeaderCount + getContentItemCount() + mBottomCount;
    }


    private void operateFootView(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BottomViewHolder) {
            ((BottomViewHolder) holder).applyReplyAddsure.setText("提交");
            ((BottomViewHolder) holder).applyReplyAddsure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (addSurveyImgeListener != null) {
                        //提交查勘
                        addSurveyImgeListener.submit();
                    }
                }
            });
        }
    }

    public void setAddSurveyImgeListener(AddAndDeleteSurveyImgeListener addSurveyImgeListener) {
        this.addSurveyImgeListener = addSurveyImgeListener;
    }

    private AddAndDeleteSurveyImgeListener addSurveyImgeListener;

    public interface AddAndDeleteSurveyImgeListener {
        void submit();
        void delete(int position);
        void onLongClick();
        void skanImage(int mode, int position);
    }
}

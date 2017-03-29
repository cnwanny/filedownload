package com.android.hifosystem.hifoevaluatevalue.netimage_mvp;

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


import com.android.hifosystem.hifoevaluatevalue.R;
import com.android.hifosystem.hifoevaluatevalue.Utils.AppUtils;
import com.android.hifosystem.hifoevaluatevalue.Utils.LogUtil;
import com.android.hifosystem.hifoevaluatevalue.camera_view.gallery_mvp.PhotoInfoEntity;
import com.android.hifosystem.hifoevaluatevalue.recycle_pages.AdapterListener;
import com.android.hifosystem.hifoevaluatevalue.recycle_pages.SquareImageView;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文件名： ImageOperateAdapter
 * 功能：   图片操作适配器，包括图片的展示和添加的操作
 * 作者： wanny
 * 时间： 16:18 2016/8/18
 */
public class ImageOperateAdapter extends RecyclerView.Adapter<ImageOperateAdapter.ImageOperateViewHolder> {

    private int mode;
    //操作方式 添加房间，
    public static final int MODE_ROOM = 0x0001;
    //操作方式 添加权属状况
    public static final int MODE_AUTHORITY = 0x0002;
    //操作方式 只显示
    public static final int MODE_SHOW = 0x0003;
    private ArrayList<PhotoInfoEntity> datas;
    private AdapterListener adapterListener;
    private Activity activity;
    //长按或者全部删除
    public static final int OPETATE_EDIT = 0x0006;
    public static final int OPERATE_NORMAl = 0x0009;
    private int operateMode = OPERATE_NORMAl;


    public ImageOperateAdapter(ArrayList<PhotoInfoEntity> datas, Activity activity, int mode) {
        this.datas = datas;
        this.activity = activity;
        this.mode = mode;
    }

    //设置数据监听
    public void setAdapterListener(AdapterListener adapterListener) {
        this.adapterListener = adapterListener;
    }

    private Context context;

    @Override
    public ImageOperateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item_view, parent, false);
        ImageOperateViewHolder viewHolder = new ImageOperateViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ImageOperateViewHolder holder, final int position) {
        //分三种情况第一种只能拍照
        //第二种只能从相册选择
        // 第三种 只显示,不做任何操作
        //第四种 添加补录信息
        if (position != -1) {
            //只要一条数据的时候
            //分类型
            if (mode == MODE_ROOM) {
                if (datas.size() <= 1) {
                    holder.imageItemImage.setImageResource(R.mipmap.icon_addroompic_bg);
                    AppUtils.notShowView(holder.imageItemType);
                } else {
                    if (position == datas.size() - 1) {
                        holder.imageItemImage.setImageResource(R.mipmap.icon_addroompic_bg);
                        AppUtils.notShowView(holder.imageItemType);
                    } else {
                        PhotoInfoEntity entity = datas.get(position);
                        AppUtils.showView(holder.imageItemType);
                        if (entity != null) {
                            if (!TextUtils.isEmpty(entity.getPath_local())) {
                                Glide.with(activity)
                                        .load(Uri.fromFile(new File(entity.getPath_local())))
                                        .dontAnimate()
                                        .into(holder.imageItemImage);
                            }
                            if (operateMode == OPETATE_EDIT) {
                                holder.imageItemType.setText("删除");
                                holder.imageItemType.setTextColor(ContextCompat.getColor(context, R.color.red));
                                holder.imageItemType.setTextSize(18);
                            } else {
                                holder.imageItemType.setTextColor(ContextCompat.getColor(context, R.color.blue));
                                holder.imageItemType.setTextSize(16);
                                if (!TextUtils.isEmpty(entity.getCategoryName())) {
                                    holder.imageItemType.setText(entity.getCategoryName());
                                } else {
                                    holder.imageItemType.setText("");
                                }
                            }
                        }
                    }
                }
            } else if (mode == MODE_AUTHORITY) {
                if (datas.size() <= 1) {
                    holder.imageItemImage.setImageResource(R.mipmap.icon_addauthorith_bg);
                    AppUtils.notShowView(holder.imageItemType);
                } else {
                    if (position == datas.size() - 1) {
                        holder.imageItemImage.setImageResource(R.mipmap.icon_addauthorith_bg);
                        AppUtils.notShowView(holder.imageItemType);
                    } else {
                        AppUtils.showView(holder.imageItemType);
                        PhotoInfoEntity entity = datas.get(position);
                        if (entity != null) {
                            AppUtils.showView(holder.imageItemType);
                            if (!TextUtils.isEmpty(entity.getPath_local())) {
                                Glide.with(activity)
                                        .load(Uri.fromFile(new File(entity.getPath_local())))
                                        .dontAnimate()
                                        .into(holder.imageItemImage);
                            } else {
                                holder.imageItemImage.setImageResource(R.mipmap.icon_addauthorith_bg);
                            }
                            if (operateMode == OPETATE_EDIT) {
                                holder.imageItemType.setText("删除");
                                holder.imageItemType.setTextColor(ContextCompat.getColor(context, R.color.red));
                                holder.imageItemType.setTextSize(18);
                            } else {
                                holder.imageItemType.setTextColor(ContextCompat.getColor(context, R.color.blue));
                                holder.imageItemType.setTextSize(16);
                                if (!TextUtils.isEmpty(entity.getCategoryName())) {
                                    holder.imageItemType.setText(entity.getCategoryName());
                                } else {
                                    holder.imageItemType.setText("");
                                }
                            }
                        }
                    }
                }
            } else if (mode == MODE_SHOW) {
                //判断是本地图片还是网路图片
                PhotoInfoEntity entity = datas.get(position);
                if (entity != null) {
                    if (!TextUtils.isEmpty(entity.getPath_local())) {
                        if (entity.getPath_local().contains("http")) {
                            Glide.with(activity)
                                    .load(entity.getPath_local())
                                    .dontAnimate()
                                    .into(holder.imageItemImage);
                        } else {
                            Glide.with(activity)
                                    .load(Uri.fromFile(new File(entity.getPath_local())))
                                    .dontAnimate()
                                    .into(holder.imageItemImage);
                        }
                    }
                    if (!TextUtils.isEmpty(entity.getCategoryName())) {
                        holder.imageItemType.setText(entity.getCategoryName());
                    } else {
                        holder.imageItemType.setText("");
                    }
                }
            }
        }

        if (position == datas.size() - 1) {
            holder.imageItemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mode == ImageOperateAdapter.MODE_SHOW){
                        if (onItemClickListener != null) {
                            onItemClickListener.skanBigImage(mode, position);
                        }
                    }else{
                        if (adapterListener != null) {
                            adapterListener.addImage(mode);
                        }
                    }
                }
            });
            if (mode == MODE_ROOM || mode == MODE_AUTHORITY) {
                AppUtils.notShowView(holder.imageItemType);
            }
        } else {
            holder.imageItemType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((mode == MODE_ROOM || mode == MODE_AUTHORITY)) {
                        if (operateMode == OPETATE_EDIT) {
                            if (onItemClickListener != null) {
                                LogUtil.log("删除操作", "删除操作");
                                onItemClickListener.deletItem(position);
                            }
                        }
                    }
                }
            });
            holder.imageItemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.skanBigImage(mode, position);
                    }
                }
            });

            if (mode == MODE_ROOM || mode == MODE_AUTHORITY) {
                holder.imageItemImage.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemLongClick(mode, position);
                        }
                        return false;
                    }
                });
            }
        }
    }

    public void setModeOperate(int operateMode) {
        this.operateMode = operateMode;
        //同时将所有的数据更新一下
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ImageOperateViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_item_main)
        LinearLayout imageItemMain;
        //图片
        @BindView(R.id.image_item_image)
        SquareImageView imageItemImage;
        //分类
        @BindView(R.id.image_item_type)
        TextView imageItemType;

        public ImageOperateViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }


    private OnItemLongClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemLongClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemLongClickListener {
        void deletItem(int position);

        void onItemLongClick(int mode, int position);

        void skanBigImage(int mode, int position);
    }
}

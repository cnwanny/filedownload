package com.android.hifosystem.hifoevaluatevalue.sharepackage;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.hifosystem.hifoevaluatevalue.R;

import java.util.ArrayList;

/**
 * 文件名： ShareDialogAdapter
 * 功能：
 * 作者： wanny
 * 时间： 14:26 2016/12/14
 */
public class ShareDialogAdapter extends BaseAdapter {



    private ArrayList<ShareEntity> data;
    private Context context;

    public ShareDialogAdapter(ArrayList<ShareEntity> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ShareEntity getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.share_dialog_item_view,null,false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        if(data != null && data.size() > 0){
            ShareEntity entity = data.get(position);
            if(entity != null){
                viewHolder.shareTypeIcon.setImageResource(entity.getTypeBgId());
                if(!TextUtils.isEmpty(entity.getType())){
                    viewHolder.shareTypeName.setText(entity.getType());
                }
            }
        }
        return view;
    }

    public  class ViewHolder {
        public TextView shareTypeName;
        public ImageView shareTypeIcon;

        public ViewHolder(View view) {
            shareTypeName = (TextView)view.findViewById(R.id.share_item_name);
            shareTypeIcon = (ImageView)view.findViewById(R.id.share_item_image);
        }
    }

}

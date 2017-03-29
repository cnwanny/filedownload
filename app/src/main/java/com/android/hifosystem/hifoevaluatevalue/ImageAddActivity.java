package com.android.hifosystem.hifoevaluatevalue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.android.hifosystem.hifoevaluatevalue.Utils.AppUtils;
import com.android.hifosystem.hifoevaluatevalue.Utils.FileOpt;
import com.android.hifosystem.hifoevaluatevalue.Utils.LogUtil;
import com.android.hifosystem.hifoevaluatevalue.camera_view.gallery_mvp.PhotoInfoEntity;
import com.android.hifosystem.hifoevaluatevalue.framework_care.ActivityStackManager;
import com.android.hifosystem.hifoevaluatevalue.framework_mvcbasic.MvpActivity;
import com.android.hifosystem.hifoevaluatevalue.netimage_mvp.ImageOperateAdapter;
import com.android.hifosystem.hifoevaluatevalue.netimage_mvp.NetImagePresenter;
import com.android.hifosystem.hifoevaluatevalue.netimage_mvp.NetImageView;
import com.android.hifosystem.hifoevaluatevalue.recycle_pages.AdapterListener;
import com.android.hifosystem.hifoevaluatevalue.recycle_pages.RecycleItemDeciration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 文件名： ImageAddActivity
 * 功能：
 * 作者： wanny
 * 时间： 10:26 2016/8/18
 */
public class ImageAddActivity extends MvpActivity<NetImagePresenter> implements NetImageView,AdapterListener, ImageOperateAdapter.OnItemLongClickListener {

    //返回
    @BindView(R.id.title_left_image)
    ImageView titleLeftImage;
    //标题
    @BindView(R.id.title_title_text)
    TextView titleTitleText;
    //RecyclerView，
    @BindView(R.id.image_operate_gridview)
    RecyclerView imageOperateGridview;

    @BindView(R.id.title_right_text)
    TextView titleRightText;

    private ImageOperateAdapter adapter;
    private ArrayList<PhotoInfoEntity> datalist;
    private GridLayoutManager gridLayoutManager;
    private int mode;
    private String classity = "";

    private ArrayList<AttchmentEntity> hous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_operate_activity_view);
        ButterKnife.bind(this);
        initData();
        initValue();
    }


    @Override
    protected NetImagePresenter createPresenter() {
        return null;
    }

    private void initValue() {
        gridLayoutManager = new GridLayoutManager(getBaseContext(), 4);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        gridLayoutManager.setReverseLayout(false);
        imageOperateGridview.setLayoutManager(gridLayoutManager);
        imageOperateGridview.addItemDecoration(new RecycleItemDeciration(mActivity, RecycleItemDeciration.VERTICAL_LIST));
        adapter = new ImageOperateAdapter(datalist, mActivity, mode);
        if (adapter != null) {
            imageOperateGridview.setAdapter(adapter);
            adapter.setAdapterListener(this);
            adapter.setOnItemClickListener(this);
        }
        if (datalist.size() > 0 && adapter != null) {
            adapter.notifyDataSetChanged();
        }
        //获取加载的附件
//         mvpPresenter.loadImage();
    }


    private void initData() {
        if (datalist == null) {
            datalist = new ArrayList<>();
        } else {
            datalist.clear();
        }
        if (getIntent() != null) {
            if (getIntent().hasExtra("mode")) {
                mode = getIntent().getIntExtra("mode", 0);
            }
            if (getIntent().hasExtra("classify")) {
                classity = getIntent().getStringExtra("classify");
            }
            if (getIntent().hasExtra("images")) {
                ArrayList<PhotoInfoEntity> images = getIntent().getParcelableArrayListExtra("images");
                if (images != null && images.size() > 0) {
                    datalist.addAll(images);
                }
            }
            if (getIntent().hasExtra("fileType")) {
                hous = getIntent().getParcelableArrayListExtra("fileType");
            }
        }
        if (mode == ImageOperateAdapter.MODE_AUTHORITY || mode == ImageOperateAdapter.MODE_ROOM) {
            PhotoInfoEntity entity = new PhotoInfoEntity();
            datalist.add(datalist.size(), entity);
            AppUtils.showView(titleRightText);
            titleRightText.setText("编辑");
        }

        if (!TextUtils.isEmpty(classity)) {
            titleTitleText.setText(classity);
        }
    }


    @OnClick(R.id.title_right_text)
    void setDelete(View view) {
        if (operateMode == ImageOperateAdapter.OPERATE_NORMAl) {
            if (adapter != null) {
                operateMode = ImageOperateAdapter.OPETATE_EDIT;
                adapter.setModeOperate(ImageOperateAdapter.OPETATE_EDIT);
                titleRightText.setText("完成");
            }
        } else {
            if (adapter != null) {
                adapter.setModeOperate(ImageOperateAdapter.OPERATE_NORMAl);
                operateMode = ImageOperateAdapter.OPERATE_NORMAl;
                titleRightText.setText("编辑");
            }
        }
    }

    private int operateMode = ImageOperateAdapter.OPERATE_NORMAl;




    @Override
    public void onItemLongClick(int mode, int position) {
        if (mode != ImageOperateAdapter.MODE_SHOW) {
            //更新操作
            if (adapter != null) {
                operateMode = ImageOperateAdapter.OPETATE_EDIT;
                adapter.setModeOperate(operateMode);
                titleRightText.setText("完成");
            }
        }
    }

    //添加附件
    @Override
    public void addImage(int mode) {
//        if (mode == ImageOperateAdapter.MODE_AUTHORITY) {
//            Intent intent = new Intent(ImageAddActivity.this, sssss.class);
//            if (datalist.size() > 0) {
//                //去掉最后一条空数据
//                datalist.remove(datalist.size() - 1);
//                intent.putParcelableArrayListExtra("select", datalist);
//            }
//            if (hous.size() > 0) {
//                intent.putExtra("filetype", hous.get(0));
//            }
//            startActivityForResult(intent, 0x0002);
//        } else if (mode == ImageOperateAdapter.MODE_ROOM) {
//            startCamera();
//        }
    }

//    private String roomFileName = "";
//    private String roomFilePath = "";

    protected void startCamera() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Intent intent = new Intent(ImageAddActivity.this, AutoCamera2Activity.class);
//            intent.putParcelableArrayListExtra("typelist", hous);
//            intent.putExtra("mode", "takePicture");
//            startActivityForResult(intent, ApplyPriceActivity.FIVE);
//        } else {
//            Intent intent = new Intent(ImageAddActivity.this, AutoCameraActivity.class);
//            intent.putParcelableArrayListExtra("typelist", hous);
//            intent.putExtra("mode", "takePicture");
//            startActivityForResult(intent, ApplyPriceActivity.FIVE);
//        }
//        roomFileName = new SimpleDateFormat("yyMMdd_HHmmss").format(new Date()) + ".jpg";
//        if (!StoragePath.isPhotoDir() || TextUtils.isEmpty(StoragePath.photoDir)) {
//            StoragePath.createDirs();
//        }
//        roomFilePath = StoragePath.photoDir + "/" + roomFileName;
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                Uri.fromFile(new File(StoragePath.photoDir, roomFileName)));
//        startActivityForResult(intent, ApplyPriceActivity.FIVE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x0002 && resultCode == 0x0002) {
            getAuthorityImage(data);
        } else if (requestCode == 0x0005 && resultCode == 0x0005) {
            getCameraImage(data);
        }
    }


    private void getAuthorityImage(Intent data) {
        if (data != null) {
            if (data.hasExtra("data")) {
                ArrayList<PhotoInfoEntity> value = data.getParcelableArrayListExtra("data");
                if (value != null && value.size() > 0) {
                    datalist.clear();
                    datalist.addAll(0, value);
                    PhotoInfoEntity entity = new PhotoInfoEntity();
                    datalist.add(datalist.size(), entity);
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                }
            } else {
                datalist.clear();
                PhotoInfoEntity entity = new PhotoInfoEntity();
                datalist.add(datalist.size(), entity);
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }


    private void getCameraImage(Intent data) {
        if (data.hasExtra("pictures")) {
            ArrayList<PhotoInfoEntity> result = data.getParcelableArrayListExtra("pictures");
            if (result != null && result.size() > 0) {
                datalist.addAll(0, result);
                if (adapter != null) {
                    adapter.notifyItemRangeInserted(0, result.size());
                }
            }
        }
    }



    @Override
    public void skanBigImage(int mode, int position) {
        //妹子就是好
        if (mode == ImageOperateAdapter.MODE_SHOW) {
            if (position != -1 && position < datalist.size()) {
                Intent intent = new Intent(ImageAddActivity.this, GallaryBigMapActivity.class);
                intent.putExtra("network", "network");
                intent.putParcelableArrayListExtra("imagelist", datalist);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        } else {
            if (position != -1 && position != datalist.size() - 1) {
                Intent intent = new Intent(ImageAddActivity.this, GallaryBigMapActivity.class);
                intent.putExtra("local_path", "local_path");
                ArrayList<PhotoInfoEntity> values = new ArrayList<>();
                values.addAll(datalist);
                values.remove(values.size()-1);
                intent.putExtra("position", position);
                intent.putParcelableArrayListExtra("imagelist", values);
                startActivity(intent);
            }
        }
    }




    //删除数据
    @Override
    public void deletItem(int position) {
        //remove date
        LogUtil.log("开始回调删除", "启动删除");
        if (!isRunning) {
            isRunning = true;
            deleteFile(position);
        }
    }

    private boolean isRunning = false;

    private void deleteFile(int position) {
        if (position < datalist.size()) {
            FileOpt.deleteFile(datalist.get(position).getPath_local());
            datalist.remove(position);
            adapter.notifyDataSetChanged();
            if (datalist.size() == 1) {
                operateMode = ImageOperateAdapter.OPERATE_NORMAl;
                adapter.setModeOperate(ImageOperateAdapter.OPERATE_NORMAl);
//                titleRightText.setText("");
            }
            isRunning = false;
        }
    }

    @OnClick(R.id.title_left_image)
    void backActivity(View view) {
        saveIamges();
    }

    private void saveIamges() {
        //保存数据
        Intent intent = new Intent();
        datalist.remove(datalist.size() - 1);
        intent.putParcelableArrayListExtra("image", datalist);
        if (mode == ImageOperateAdapter.MODE_AUTHORITY) {
            setResult(0x0003, intent);
        } else if (mode == ImageOperateAdapter.MODE_ROOM) {
            setResult(0x0004, intent);
        }
        ActivityStackManager.getInstance().exitActivity(mActivity);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            saveIamges();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}

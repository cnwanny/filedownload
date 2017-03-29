package com.android.hifosystem.hifoevaluatevalue.location_service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.android.hifosystem.hifoevaluatevalue.Utils.LogUtil;
import com.android.hifosystem.hifoevaluatevalue.Utils.PreferenceUtil;
import com.android.hifosystem.hifoevaluatevalue.framework_mvcbasic.BaseBean;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;



/**
 * 类名： ${type_name}
 * 工鞥：获取经纬度，然后通过经纬度来上传数据
 * 作者： wanny
 * 时间：${date} ${time}
 */

public class LocationService extends IntentService  implements LocationUploadImpl{
    public LocationService() {
        super("LocationService");
    }

//    public static boolean isRunning = true;
    public LocationClient mLocationClient = null;

    public BDLocationListener myListener = new MyLocationListener();
    private LocationServicePresenter presenter;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.log("在服务中执行onCreate()", "onCreate");
        //启动服务的时候调用数据相关的东西
        presenter = new LocationServicePresenter(this);
//        启动定位，然后上传操作
        if(mLocationClient == null){
            mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
        }
        mLocationClient.registerLocationListener(myListener);// 注册监听函数
        initLocation();
//        if (!TextUtils.isEmpty(token)) {
//        } else {
//            Intent intent = new Intent(this, SendLocationRecever.class);
//            intent.setAction(AppConstant.LOCATIONREVEIVER);
//            PendingIntent sender = PendingIntent.getBroadcast(this, 0x0001, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
//            am.cancel(sender);
//            return;
//        }
    }



    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
//        option.setPriority(LocationClientOption.GpsFirst);
        option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);// 可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }


    @Override
    public void success(BaseBean<String> result) {

        ///

    }

    private void startLoction() {

//        getData();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        getData();
//            }
//        }).start();

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LogUtil.log("在服务中执行onHandleIntent(intetn)", "onHandleIntent");
        startLoction();
    }

    private void getData() {
        if (mLocationClient != null) {
            mLocationClient.start();
            isSuccess = false;
        } else {
            mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
            mLocationClient.registerLocationListener(myListener);// 注册监听函数
            initLocation();
            mLocationClient.start();
            isSuccess = false;
        }
    }

    private boolean isSuccess = false;

    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(final BDLocation bdLocation) {
            if (mLocationClient != null) {
                mLocationClient.stop();
            }
            if (bdLocation == null) {
                return;
            } else {
                //经度
//                String longitude = Double.toString(bdLocation.getLongitude());
//                LogUtil.log("定位的位置信息",bdLocation.getAddrStr());
//                //纬度
//                String latitude = Double.toString(bdLocation.getLatitude());
                if (mLocationClient != null) {
                    mLocationClient.stop();
                }
                if (isSuccess) {
                    return;
                } else {
                    isSuccess = true;
                    if (bdLocation.getLongitude() != 0.0f && bdLocation.getLatitude() != 0.0f) {
                        //如果经纬度都是对应的值后
                       String token = PreferenceUtil.getInstance(getApplicationContext()).getString("token", "");
                        if(!TextUtils.isEmpty(token)){
                            presenter.saveLocation(token ,bdLocation.getLongitude(),bdLocation.getLatitude());
                        }
//                        startOperateData(bdLocation.getLatitude(), bdLocation.getLongitude());
                    } else {
                        return;
                    }
                }
//              执行上传的附件；
            }
        }
    }


//    private void startOperateData(final double latitude, final double longtitude) {
//        synchronized (this) {
//            new Thread() {
//                @Override
//                public void run() {
//                    super.run();
//                    //提交经纬度
//                    upLoadData(latitude, longtitude);
//                }
//            }.start();
//        }
//    }
//
//    private void upLoadData(double lat, double lon) {
//        LinkedList<Map<String, Object>> param = new LinkedList<>();
//        Map<String, Object> map = new HashMap<>();
//        map.put("name", "user");
//        map.put("value", username);
//        param.add(map);
//        map = new HashMap<>();
//        map.put("name", "lng");
////        BigDecimal biglon = new BigDecimal(lon);
//        map.put("value", lon + "");
//        param.add(map);
//        map = new HashMap<>();
//        map.put("name", "lat");
////        BigDecimal biglat = new BigDecimal(lat);
//        map.put("value", lat+ "");
//        param.add(map);
//        String reslut = TaskSendWebService.getWebServiceData(NetWordInterface.END_POINT,  NetWordInterface.AddGpsRecord, param, false);
//        LogUtil.log(reslut);
//    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.log("在服务中执行onStartCommand()", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void setIntentRedelivery(boolean enabled) {
        LogUtil.log("setIntentRedelivery()" + enabled);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //   @Override
    public IBinder onBind(Intent intent) {
        System.out.println("onBind");
        return super.onBind(intent);
    }
}

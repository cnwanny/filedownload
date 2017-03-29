package com.android.hifosystem.hifoevaluatevalue.framework_net.retrofit;


import com.android.hifosystem.hifoevaluatevalue.camera_view.camera_mvp.SurveyFileModel;
import com.android.hifosystem.hifoevaluatevalue.camera_view.gallery_mvp.FileEntity;
import com.android.hifosystem.hifoevaluatevalue.framework_fileoperate.FilePostModel;
import com.android.hifosystem.hifoevaluatevalue.location_service.ResultEntity;
import com.squareup.okhttp.ResponseBody;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 文件名： ApiStores
 * 功能：
 * 作者： wanny
 * 时间： 15:58 2016/8/5
 */
public interface ApiStores {
    //勇哥
//    String API_SERVER_URL = "http://192.168.5.244:10524/api/";//http://192.168.5.244:10524/api/
    //测试外网
//    String API_SERVER_URL = "http://183.230.7.248:10555/api/";
    //正式的
//    String API_SERVER_URL = "http://183.230.7.248:6502/api/";
    //正式环境域名
    String API_SERVER_URL = "http://zj.17gp.com:6502/api/";
    //上传附件,测试
//    String UpLoadFilePath = "http://183.230.7.248:10086/Api/UpLoad?";
    //获取app获取最新版本接口
    String GETNEWESTVERSION = "GetNewestVersion";
    //app更新地址
    String appUpdate = "http://www.17gp.com:8089/AuthenProxy.asmx";//115.28.10.123:8089
    //上传附件正式
    String UpLoadFilePath = "http://yk.17gp.com:10086/Api/UpLoad?";
    //附件路径
//   String URL = "http://zj.17gp.com";
    //正式环境
    String HTML_URL = "http://m.17gp.com";
//    测试外网
//    String HTML_URL = "http://183.230.7.248:8097/";
    //白强的电脑
//    String HTML_URL = "http://192.168.5.110:2520/";
    //良勇
//     String HTML_URL = "http://192.168.5.244:10569/";
    //谭雄文
//    String HTML_URL =  "http://192.168.5.227:8081/";
    //彩云春
//    String HTML_URL = "http://192.168.5.247:9056/";

    @POST("Survery/PostSurveryFiles")
    Observable<FilePostModel> submitFileInfo(@Body ArrayList<FileEntity> json);

    //获取附件
//    @GET("Survery/GetDetails")
//    Observable<SurveyFileModel> getSurveyFileList(@Query("rid") String rid);

//    获取附件信息
    @GET("Survery/GetSurveryFiles")
    Observable<SurveyFileModel> getSurveyFileList(@Query("rid") String rid);

    @GET("Login/SaveGps")
    Observable<ResultEntity> saveLocation(@Query("token") String token,@Query("lng") double lng ,@Query("lat") double lat );

}

package com.android.hifosystem.hifoevaluatevalue;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Message;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.android.hifosystem.hifoevaluatevalue.Utils.LogUtil;

/**
 * MyWebChomeClient
 */
public class MyWebChomeClient extends WebChromeClient {

    private OpenFileChooserCallBack mOpenFileChooserCallBack;

    public MyWebChomeClient(OpenFileChooserCallBack openFileChooserCallBack) {
        mOpenFileChooserCallBack = openFileChooserCallBack;
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        mOpenFileChooserCallBack.openFileChooserCallBack(uploadMsg, acceptType);
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        openFileChooser(uploadMsg, "");
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        openFileChooser(uploadMsg, acceptType);
    }

    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                     FileChooserParams fileChooserParams) {
        return mOpenFileChooserCallBack.openFileChooserCallBackAndroid5(webView, filePathCallback, fileChooserParams);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        mOpenFileChooserCallBack.onProgressChanged(view , newProgress);
    }

    public interface OpenFileChooserCallBack {
        // for API - Version below 5.0.
        void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType);

        // for API - Version above 5.0 (contais 5.0).
        boolean openFileChooserCallBackAndroid5(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                                FileChooserParams fileChooserParams);

        void onProgressChanged(WebView view, int progress);
    }

    public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
        LogUtil.log(origin);
        callback.invoke(origin, true, false);
//        AlertDialog.Builder builder = new AlertDialog.Builder();
//        builder.setMessage("Allow to access location information?");
//        DialogInterface.OnClickListener dialogButtonOnClickListener = new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int clickedButton) {
//                if (DialogInterface.BUTTON_POSITIVE == clickedButton) {
//                    callback.invoke(origin, true, true);
//                } else if (DialogInterface.BUTTON_NEGATIVE == clickedButton) {
//                    callback.invoke(origin, false, false);
//                }
//            }
//        };
//        builder.setPositiveButton("Allow", dialogButtonOnClickListener);
//        builder.setNegativeButton("Deny", dialogButtonOnClickListener);
//        builder.show();
        super.onGeolocationPermissionsShowPrompt(origin, callback);
    }

    @Override
    public void onPermissionRequest(PermissionRequest request) {
        LogUtil.log("请求权限");
        super.onPermissionRequest(request);
    }

    @Override
    public void onGeolocationPermissionsHidePrompt() {
        super.onGeolocationPermissionsHidePrompt();
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
        transport.setWebView(view);
        resultMsg.sendToTarget();
        return  true;
    }

}

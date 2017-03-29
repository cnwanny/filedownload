package com.a17gp.filedownlibs.operate;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 文件名： DownFileInof
 * 功能： 要下载的文件对象，包括下载进度，文件属性封装
 * 作者： wanny
 * 时间： 14:41 2017/2/22
 */
public class DownFileInfo implements Parcelable {

    //网络地址
    private String url;
    //文件大小
    private String length;
    //文件名称
    private String fileName;
    //文件类型
    private String fileType;
    //下载下来后保存的路径
    private String savePath;
    //下载的进度
    private int progress;


    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public DownFileInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.length);
        dest.writeString(this.fileName);
        dest.writeString(this.fileType);
        dest.writeString(this.savePath);
        dest.writeInt(this.progress);
    }

    protected DownFileInfo(Parcel in) {
        this.url = in.readString();
        this.length = in.readString();
        this.fileName = in.readString();
        this.fileType = in.readString();
        this.savePath = in.readString();
        this.progress = in.readInt();
    }

    public static final Creator<DownFileInfo> CREATOR = new Creator<DownFileInfo>() {
        @Override
        public DownFileInfo createFromParcel(Parcel source) {
            return new DownFileInfo(source);
        }

        @Override
        public DownFileInfo[] newArray(int size) {
            return new DownFileInfo[size];
        }
    };
}

package com.android.hifosystem.hifoevaluatevalue.DBFileImage.FileDB;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 文件名： LocalFileEntity
 * 功能：
 * 作者： wanny
 * 时间： 16:57 2016/8/25
 */
public class LocalFileEntity implements Parcelable {
    private String FileName;// = "FileName";
    private String LocalFilePath;// = "LocalFilePath";
    private int CategoryId;//= "CategoryId";
    private String CategoryName;// = "CategoryName";
    private String FID;// = "FID";
    private String UserAccount;// = "UserAccount";
    private String UserName;//= "UserName";
    private String Extension;//= "Extension";//":"string"

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getLocalFilePath() {
        return LocalFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        LocalFilePath = localFilePath;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getFID() {
        return FID;
    }

    public void setFID(String FID) {
        this.FID = FID;
    }

    public String getUserAccount() {
        return UserAccount;
    }

    public void setUserAccount(String userAccount) {
        UserAccount = userAccount;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getExtension() {
        return Extension;
    }

    public void setExtension(String extension) {
        Extension = extension;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.FileName);
        dest.writeString(this.LocalFilePath);
        dest.writeInt(this.CategoryId);
        dest.writeString(this.CategoryName);
        dest.writeString(this.FID);
        dest.writeString(this.UserAccount);
        dest.writeString(this.UserName);
        dest.writeString(this.Extension);
    }

    public LocalFileEntity() {
    }

    protected LocalFileEntity(Parcel in) {
        this.FileName = in.readString();
        this.LocalFilePath = in.readString();
        this.CategoryId = in.readInt();
        this.CategoryName = in.readString();
        this.FID = in.readString();
        this.UserAccount = in.readString();
        this.UserName = in.readString();
        this.Extension = in.readString();
    }

    public static final Creator<LocalFileEntity> CREATOR = new Creator<LocalFileEntity>() {
        @Override
        public LocalFileEntity createFromParcel(Parcel source) {
            return new LocalFileEntity(source);
        }

        @Override
        public LocalFileEntity[] newArray(int size) {
            return new LocalFileEntity[size];
        }
    };
}

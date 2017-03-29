package com.android.hifosystem.hifoevaluatevalue.sharepackage;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 文件名： ShareEntity
 * 功能：
 * 作者： wanny
 * 时间： 14:16 2016/12/14
 */
public class ShareEntity implements Parcelable {

    //
    private int typeBgId;
    private String type;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTypeBgId() {
        return typeBgId;
    }

    public void setTypeBgId(int typeBgId) {
        this.typeBgId = typeBgId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.typeBgId);
        dest.writeString(this.type);
    }

    public ShareEntity() {
    }

    protected ShareEntity(Parcel in) {
        this.typeBgId = in.readInt();
        this.type = in.readString();
    }

    public static final Parcelable.Creator<ShareEntity> CREATOR = new Parcelable.Creator<ShareEntity>() {
        @Override
        public ShareEntity createFromParcel(Parcel source) {
            return new ShareEntity(source);
        }

        @Override
        public ShareEntity[] newArray(int size) {
            return new ShareEntity[size];
        }
    };
}

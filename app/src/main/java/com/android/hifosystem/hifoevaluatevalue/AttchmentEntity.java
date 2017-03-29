package com.android.hifosystem.hifoevaluatevalue;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 文件名： AttchmentEntity
 * 功能：
 * 作者： wanny
 * 时间： 14:59 2016/8/25
 */
public class AttchmentEntity implements Parcelable {
    private int Id;//": 408,
    private String Name;//": "建筑物外观"

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.Id);
        dest.writeString(this.Name);
    }

    public AttchmentEntity() {

    }

    protected AttchmentEntity(Parcel in) {
        this.Id = in.readInt();
        this.Name = in.readString();
    }

    public static final Creator<AttchmentEntity> CREATOR = new Creator<AttchmentEntity>() {
        @Override
        public AttchmentEntity createFromParcel(Parcel source) {
            return new AttchmentEntity(source);
        }

        @Override
        public AttchmentEntity[] newArray(int size) {
            return new AttchmentEntity[size];
        }
    };
}

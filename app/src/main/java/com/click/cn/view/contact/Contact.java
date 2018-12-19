package com.click.cn.view.contact;

import android.os.Parcel;
import android.os.Parcelable;

public class Contact implements Comparable<Contact> ,Parcelable {

    private String nickname;
    private String user_phone;
    private String user_mail;
    private String user_uid;
    private int sex;
    private String pinyin;

    public Contact() {
    }

    protected Contact(Parcel in) {
        nickname = in.readString();
        user_phone = in.readString();
        user_mail = in.readString();
        user_uid = in.readString();
        sex = in.readInt();
        pinyin=in.readString();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nickname);
        parcel.writeString(user_phone);
        parcel.writeString(user_mail);
        parcel.writeString(user_uid);
        parcel.writeInt(sex);
        parcel.writeString(pinyin);
    }

    @Override
    public int compareTo(Contact contact) {
        if (pinyin.startsWith("#")) {
            return 1;
        } else if (contact.getPinyin().startsWith("#")) {
            return -1;
        } else {
            return pinyin.compareTo(contact.getPinyin());
        }
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_mail() {
        return user_mail;
    }

    public void setUser_mail(String user_mail) {
        this.user_mail = user_mail;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
}

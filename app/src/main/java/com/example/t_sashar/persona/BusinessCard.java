package com.example.t_sashar.persona;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by JSoyinka on 7/25/17.
 */

public class BusinessCard implements Parcelable{
    public String user_name;
    public String user_email;
    public String user_number;
    public String user_organization;
    public String Id;
    public HashMap<String, ArrayList<String>> tagMap;

    public BusinessCard(String name, String email, String number, String org) {
        this.user_name = name;
        this.user_email = email;
        this.user_number = number;
        this.user_organization = org;

        HashMap<String, ArrayList<String>> map = new HashMap<>();
        ArrayList<String> test_tags = new ArrayList<>();
        test_tags.add("Tag1");
        test_tags.add("Tag2");
        map.put("Id1", test_tags);
        tagMap = map;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(this.user_name);
        dest.writeString(this.user_email);
        dest.writeString(this.user_number);
        dest.writeString(this.user_organization);
    }

    private BusinessCard(Parcel input){
        this.user_name = input.readString();
        this.user_email = input.readString();
        this.user_number = input.readString();
        this.user_organization = input.readString();
    }

    public static final Parcelable.Creator<BusinessCard> CREATOR = new Parcelable.Creator<BusinessCard>() {
        @Override
        public BusinessCard createFromParcel(Parcel source) {
            return new BusinessCard(source);
        }

        @Override
        public BusinessCard[] newArray(int size) {
            return new BusinessCard[size];
        }
    };

}

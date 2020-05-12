package com.example.ceasepandemic;

import android.webkit.GeolocationPermissions;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;

import java.io.Serializable;

public class UserObject implements Serializable {
    String name,phone,uid,status;
    //status /// 1 = Positive // 0 = Neutral // -1 Safe
    GeoLocation geoLocation;

    public UserObject (String uid){
        this.uid = uid;
    }

    public UserObject(String uid,String name,String phone,String status){
        this.uid = uid;
        this.name = name;
        this.phone = phone;
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setGeoFire(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    public String getUid() {
        return uid;
    }

    public String getStatus() {
        return status;
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }
}

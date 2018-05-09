package com.sunday.tianshehuoji.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fanyy on 2018/4/13.
 */

public class TiansheMarket implements Parcelable {
    private Map<String,Object> map;

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(map);
    }

    public TiansheMarket(Map<String,Object> objectMap){
        map = objectMap;
    }

    public TiansheMarket(){

    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public static final Parcelable.Creator<TiansheMarket> CREATOR = new Parcelable.Creator<TiansheMarket>() {
        public TiansheMarket createFromParcel(Parcel source) {
            TiansheMarket model = new TiansheMarket();
            // 必须实例化
            model.map = new HashMap<String,Object>();
            source.readMap(model.map, getClass().getClassLoader());

            return model;
        }

        public TiansheMarket[] newArray(int size) {
            return new TiansheMarket[size];
        }
    };
}

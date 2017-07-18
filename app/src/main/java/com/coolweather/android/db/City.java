package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 我的 on 2017/7/18.
 */

public class City extends DataSupport {

    private int id;
    private String cityName;
    private int cityCode;
    private int provinceId;

    public int getId(){
        return id;
    }

    public int getProvinceId(){
        return provinceId;
    }

    public int getCityCode(){
        return cityCode;
    }

    public String getCityName(){
        return cityName;
    }

    public void setId( int id ){
        this.id = id;
    }

    public void setCityName( String cityName ){
        this.cityName = cityName;
    }

    public void setCityCode( int cityCode ){
        this.cityCode = cityCode;
    }

    public void setProvinceId( int provinceId ){
        this.provinceId = provinceId;
    }

}

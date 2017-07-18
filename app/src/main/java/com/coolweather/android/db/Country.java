package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 我的 on 2017/7/18.
 */

public class Country extends DataSupport {

    private int id;
    private String CountryName;
    private String wetherId;
    private int CityId;

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWetherId() {
        return wetherId;
    }

    public void setWetherId(String wetherId) {
        this.wetherId = wetherId;
    }

    public int getCityId() {
        return CityId;
    }

    public void setCityId(int cityId) {
        CityId = cityId;
    }
}

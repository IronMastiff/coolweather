package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 我的 on 2017/7/18.
 */

public class Country extends DataSupport {

    private int id;
    private String CountryName;
    private String weatherId;
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

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return CityId;
    }

    public void setCityId(int cityId) {
        CityId = cityId;
    }
}

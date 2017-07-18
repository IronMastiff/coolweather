package com.coolweather.android.util;

import android.text.TextUtils;

import com.coolweather.android.db.City;
import com.coolweather.android.db.Country;
import com.coolweather.android.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 我的 on 2017/7/18.
 */

public class Utility {
    /**
      *解析和处理服务器返回的省的数据
      */
    public static boolean handleProvinceResponse( String response ){
        if( !TextUtils.isEmpty( response) ){
            try {
                JSONArray allProvinces = new JSONArray( response );
                for( int i = 0; i < allProvinces.length(); i++ ){
                    JSONObject provinceObject = allProvinces.getJSONObject( i );
                    Province province = new Province();
                    province.setProvinceName( provinceObject.getString( "name" ) );
                    province.setProvinceCode( provinceObject.getInt( "id" ) );
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析数据库返回的市级信息
     */
    public static boolean handleCityResponse( String response, int provinceId ){
        if( !TextUtils.isEmpty( response ) ){
            try {
                JSONArray allCities = new JSONArray( response );
                for( int i = 0; i < allCities.length(); i++ ){
                    JSONObject cityObject = allCities.getJSONObject( i );
                    City city = new City();
                    city.setCityName( cityObject.getString( "Name" ) );
                    city.setCityCode( cityObject.getInt( "id" ) );
                    city.setProvinceId( provinceId );
                    city.save();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    /**
     * 解析数据库返回的县级数据
     */
    public static boolean handleCountryResponse( String response, int cityId ){
        if( !TextUtils.isEmpty( response ) ){
            try {
                JSONArray allCountry = new JSONArray( response );
                for( int i = 0; i < allCountry.length(); i++ ){
                    JSONObject countryObject = allCountry.getJSONObject( i );
                    Country country = new Country();
                    country.setCountryName( countryObject.getString( "Name" ) );
                    country.setWetherId( countryObject.getString( "wether_id" ) );
                    country.save();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }


}

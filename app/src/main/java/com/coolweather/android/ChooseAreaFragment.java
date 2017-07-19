package com.coolweather.android;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.android.R;
import com.coolweather.android.db.City;
import com.coolweather.android.db.Country;
import com.coolweather.android.db.Province;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 我的 on 2017/7/18.
 */

public class ChooseAreaFragment extends Fragment {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTRY = 2;
    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();

    /**
     * 省列表
     */
    private List<Province> provinceList;

    /**
     *城市列表
     */
    private List<City> cityList;

    /**
     * 县列表
     */
    private List<Country> countryList;

    /**
     * 选中的省份
     */
    private Province selectedProvince;

    /**
     * 选中的城市
     */
    private City selectedCity;

    /**
     * 当前选中的级别
     */
    private int currentLevel;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState ){
        View view = inflater.inflate( R.layout.choose_area, container, false );
        titleText = ( TextView )view.findViewById( R.id.title_text );
        backButton = ( Button )view.findViewById( R.id.back_button );
        listView = ( ListView )view.findViewById( R.id.list_view );
        adapter = new ArrayAdapter<>( getContext(), android.R.layout.simple_list_item_1, dataList );
        listView.setAdapter( adapter );
        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState ){
        super.onActivityCreated( savedInstanceState );
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick( AdapterView<?> parent, View view, int position, long id ){
                if( currentLevel == LEVEL_PROVINCE ){
                    selectedProvince = provinceList.get( position );
                    queryCity();
                }
                else if( currentLevel == LEVEL_CITY ){
                    selectedCity = cityList.get( position );
                    queryCountry();
                }
            }
        });
        backButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick( View v ){
                if( currentLevel == LEVEL_COUNTRY ){
                    queryCity();
                }
                else if ( currentLevel == LEVEL_CITY ){
                    queryProvince();
                }
            }
        });
        queryProvince();
    }

    /**
     * 查询全国所有的省，优先从数据库查询，没有查询到就去服务器上查询
     */
    private void queryProvince(){
        titleText.setText( "中国" );
        backButton.setVisibility( View.GONE );
        provinceList = DataSupport.findAll( Province.class );
        if( provinceList.size() > 0 ){
            dataList.clear();
            for( Province province : provinceList ){
                dataList.add( province.getProvinceName() );
            }
            adapter.notifyDataSetChanged();
            listView.setSelection( 0 );
            currentLevel = LEVEL_PROVINCE;
        }
        else{
            String address = "http://guolin.tech/api/china";
            queryFromServer( address, "province" );
        }
    }

    /**
     * 查询选中的省内所有的市，优先从数据库查询，如果没有查询到再去服务器查询
     */
    private void queryCity(){
        titleText.setText( selectedProvince.getProvinceName() );
        backButton.setVisibility( View.VISIBLE );
        cityList = DataSupport.where( "provinceId = ?", String.valueOf( selectedProvince.getId() ) ).find( City.class );
        if( cityList.size() > 0 ){
            dataList.clear();
            for( City city : cityList ){
                dataList.add( city.getCityName() );
            }
            adapter.notifyDataSetChanged();
            listView.setSelection( 0 );
            currentLevel = LEVEL_CITY;
        }
        else{
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer( address, "city" );
        }


    }
    /**
     * 查询选中市内的所有县，首先从数据库查找，如果没有就从服务器查找
     */
    private void queryCountry(){
        titleText.setText( selectedCity.getCityName() );
        backButton.setVisibility( View.VISIBLE );
        countryList = DataSupport.where( "cityId = ?", String.valueOf( selectedCity.getId() ) ).find( Country.class );
        if( countryList.size() > 0 ){
            dataList.clear();
            for( Country country : countryList ){
                dataList.add( country.getCountryName() );
            }
            adapter.notifyDataSetChanged();
            listView.setSelection( 0 );
            currentLevel = LEVEL_COUNTRY;
        }
        else{
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer( address, "country" );
        }
    }

    /**
     * 根据传入的数据类型从服务器上查询市县数据
     */
    private void queryFromServer( String address, final String type ){
        showProgressDialog();
        HttpUtil.sendOkHttpRequest( address, new Callback(){
            @Override
            public void onResponse( Call call, Response response )throws IOException{
                String responseText = response.body().string();
                boolean result = false;
                if( "province".equals( type ) ){
                    result = Utility.handleProvinceResponse( responseText );
                }
                else if( "city".equals( type ) ){
                    result = Utility.handleCityResponse( responseText, selectedProvince.getId() );
//                    Log.d( "Message", Integer.toString( selectedProvince.getId() ) );
                }
                else if( "country".equals( type ) ){
                    result = Utility.handleCountryResponse( responseText, selectedCity.getId() );
                }
                if( result ){
//                    Log.d( "Message", "I am here check" );
                    getActivity().runOnUiThread( new Runnable(){
                        @Override
                        public void run(){
                            closeProgressDialog();
                            if( "province".equals( type ) ){
                                queryProvince();
                            }
                            else if( "city".equals( type ) ){
                                queryCity();
                            }
                            else if( "country".equals( type ) ){
                                queryCountry();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure( Call call, IOException e ){
                //通过runOnUiThread方法回到主线程处理逻辑
                getActivity().runOnUiThread( new Runnable(){
                    @Override
                    public void run(){
                        closeProgressDialog();
                        Toast.makeText( getContext(), "加载失败", Toast.LENGTH_SHORT ).show();
                    }
                });
            }
        });
    }

    /**
     *显示进度对话框
     */
    private void showProgressDialog(){
        if( progressDialog == null ){
            progressDialog = new ProgressDialog( getActivity() );
            progressDialog.setMessage( "正在加载。。。。" );
            progressDialog.setCanceledOnTouchOutside( false );
        }
        progressDialog.show();
    }

    /**
     *关闭对话框
     */
    private void closeProgressDialog(){
        if( progressDialog != null ){
            progressDialog.dismiss();
        }
    }
}

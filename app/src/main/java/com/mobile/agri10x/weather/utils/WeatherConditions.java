package com.mobile.agri10x.weather.utils;

import android.content.Context;
import android.widget.Toast;

import com.mobile.agri10x.weather.interfaces.IWeatherApi;
import com.mobile.agri10x.weather.interfaces.IWeatherCallbackListener;
import com.mobile.agri10x.weather.models.AccuWeather5DayModel;
import com.mobile.agri10x.weather.models.AccuWeatherModel;
import com.mobile.agri10x.weather.models.OpenWeather5DayModel;
import com.mobile.agri10x.weather.models.OpenWeatherModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mobile.agri10x.weather.constants.ProjectConstants.BASE_DEV_URL_ACCU_WEATHER;
import static com.mobile.agri10x.weather.constants.ProjectConstants.BASE_URL_ACCU_WEATHER;
import static com.mobile.agri10x.weather.constants.ProjectConstants.BASE_URL_OPEN_WEATHER;

/**
 * Created by akash on 27/10/17.
 */

public class WeatherConditions {

    private static IWeatherApi mWeatherApi;
   private static Context context;
    /**
     * @param city
     * @param appId
     * @param listener
     */
    public static void getOpenWeatherData(String city, String appId, final IWeatherCallbackListener listener) {

        mWeatherApi = ApiService.getRetrofitInstance(BASE_URL_OPEN_WEATHER).create(IWeatherApi.class);
        Call<OpenWeatherModel> resForgotPasswordCall = mWeatherApi.getOpenWeatherData(appId, city);
        resForgotPasswordCall.enqueue(new Callback<OpenWeatherModel>() {
            @Override
            public void onResponse(Call<OpenWeatherModel> call, Response<OpenWeatherModel> response) {

                if(response.code() == 404){
                    listener.getWeatherData(null, false, "");
                }
                if( response.code() == 400){
                    listener.getWeatherData(null, false, "");
                }
                if (response.body() != null) {
                    if (listener != null)
                        listener.getWeatherData(response.body(), true, "");
                }
            }

            @Override
            public void onFailure(Call<OpenWeatherModel> call, Throwable t) {
                if (listener != null)
                listener.getWeatherData(null, false, t.getMessage());

            }
        });
    }


    public static void getOpenWeatherData5Days(String city, String appId, final IWeatherCallbackListener listener) {

        mWeatherApi = ApiService.getRetrofitInstance(BASE_URL_OPEN_WEATHER).create(IWeatherApi.class);
        Call<OpenWeather5DayModel> call = mWeatherApi.getOpenWeatherData5days(appId, city);
        call.enqueue(new Callback<OpenWeather5DayModel>() {
            @Override
            public void onResponse(Call<OpenWeather5DayModel> call, Response<OpenWeather5DayModel> response) {
                if (response.body() != null) {
                    if (listener != null)
                    listener.getWeatherData(response.body(), true, "");
                }
            }

            @Override
            public void onFailure(Call<OpenWeather5DayModel> call, Throwable t) {
                if (listener != null)
                listener.getWeatherData(null, false, t.getMessage());
            }
        });
    }

    public static void getAccuWeatherData(String city, String appId, final IWeatherCallbackListener listener, Boolean isProductionUrl) {

        if (isProductionUrl)
            mWeatherApi = ApiService.getRetrofitInstance(BASE_URL_ACCU_WEATHER).create(IWeatherApi.class);
        else
            mWeatherApi = ApiService.getRetrofitInstance(BASE_DEV_URL_ACCU_WEATHER).create(IWeatherApi.class);
        Call<List<AccuWeatherModel>> call = mWeatherApi.getAccuWeatherData(city, appId);
        call.enqueue(new Callback<List<AccuWeatherModel>>() {
            @Override
            public void onResponse(Call<List<AccuWeatherModel>> call, Response<List<AccuWeatherModel>> response) {
                if (response.body() != null) {
                    if (listener != null)
                    listener.getWeatherData(response.body().get(0), true, "Error");
                }
            }

            @Override
            public void onFailure(Call<List<AccuWeatherModel>> call, Throwable t) {
                listener.getWeatherData(null, false, t.getMessage());
            }
        });
    }

    public static void getAccuWeatherData5Days(String city, String appId, final IWeatherCallbackListener listener, Boolean isProductionUrl) {

        if (isProductionUrl)
            mWeatherApi = ApiService.getRetrofitInstance(BASE_URL_ACCU_WEATHER).create(IWeatherApi.class);
        else
            mWeatherApi = ApiService.getRetrofitInstance(BASE_DEV_URL_ACCU_WEATHER).create(IWeatherApi.class);
        Call<AccuWeather5DayModel> call = mWeatherApi.getAccuWeatherData5days(city, appId);
        call.enqueue(new Callback<AccuWeather5DayModel>() {
            @Override
            public void onResponse(Call<AccuWeather5DayModel> call, Response<AccuWeather5DayModel> response) {
                if (response.body() != null) {
                    if (listener != null)
                    listener.getWeatherData(response.body(), true, "");
                }
            }

            @Override
            public void onFailure(Call<AccuWeather5DayModel> call, Throwable t) {
                if (listener != null)
                listener.getWeatherData(null, false, t.getMessage());
            }
        });
    }
}


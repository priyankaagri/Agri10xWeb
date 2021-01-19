package com.mobile.agri10x.weather.interfaces;


public interface IWeatherCallbackListener<T> {
    <Y> void getWeatherData(Y weatherModel, Boolean success, String errorMsg);
}

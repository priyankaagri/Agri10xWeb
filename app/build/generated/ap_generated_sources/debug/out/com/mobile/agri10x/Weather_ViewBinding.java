// Generated code from Butter Knife. Do not modify!
package com.mobile.agri10x;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Weather_ViewBinding implements Unbinder {
  private Weather target;

  @UiThread
  public Weather_ViewBinding(Weather target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public Weather_ViewBinding(Weather target, View source) {
    this.target = target;

    target.tvCity = Utils.findRequiredViewAsType(source, R.id.tv_city, "field 'tvCity'", TextView.class);
    target.tvCountry = Utils.findRequiredViewAsType(source, R.id.tv_country, "field 'tvCountry'", TextView.class);
    target.btnGet5DaysWeather = Utils.findRequiredViewAsType(source, R.id.btn_get_5_days_weather, "field 'btnGet5DaysWeather'", Button.class);
    target.ivWeatherIcon = Utils.findRequiredViewAsType(source, R.id.iv_weather_icon, "field 'ivWeatherIcon'", ImageView.class);
    target.rvWeatherData = Utils.findRequiredViewAsType(source, R.id.rv_weather_data, "field 'rvWeatherData'", RecyclerView.class);
    target.etCityName = Utils.findRequiredViewAsType(source, R.id.et_city_name, "field 'etCityName'", AutoCompleteTextView.class);
    target.btnGetCurrentWeather = Utils.findRequiredViewAsType(source, R.id.btn_get_weather, "field 'btnGetCurrentWeather'", Button.class);
    target.tvWeather = Utils.findRequiredViewAsType(source, R.id.tv_info, "field 'tvWeather'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    Weather target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvCity = null;
    target.tvCountry = null;
    target.btnGet5DaysWeather = null;
    target.ivWeatherIcon = null;
    target.rvWeatherData = null;
    target.etCityName = null;
    target.btnGetCurrentWeather = null;
    target.tvWeather = null;
  }
}

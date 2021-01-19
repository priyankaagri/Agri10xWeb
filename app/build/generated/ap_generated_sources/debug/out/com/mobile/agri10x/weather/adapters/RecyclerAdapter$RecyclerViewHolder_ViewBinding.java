// Generated code from Butter Knife. Do not modify!
package com.mobile.agri10x.weather.adapters;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.mobile.agri10x.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RecyclerAdapter$RecyclerViewHolder_ViewBinding implements Unbinder {
  private RecyclerAdapter.RecyclerViewHolder target;

  @UiThread
  public RecyclerAdapter$RecyclerViewHolder_ViewBinding(RecyclerAdapter.RecyclerViewHolder target,
      View source) {
    this.target = target;

    target.tvTempMax = Utils.findRequiredViewAsType(source, R.id.tv_temp_max, "field 'tvTempMax'", TextView.class);
    target.tvWeatherDate = Utils.findRequiredViewAsType(source, R.id.tv_weather_date, "field 'tvWeatherDate'", TextView.class);
    target.tvTempMin = Utils.findRequiredViewAsType(source, R.id.tv_temp_min, "field 'tvTempMin'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    RecyclerAdapter.RecyclerViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvTempMax = null;
    target.tvWeatherDate = null;
    target.tvTempMin = null;
  }
}

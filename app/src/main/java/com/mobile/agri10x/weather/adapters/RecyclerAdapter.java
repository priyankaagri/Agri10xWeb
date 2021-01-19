package com.mobile.agri10x.weather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobile.agri10x.R;
import com.mobile.agri10x.weather.models.OpenWeather5DayModel;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    Context context;
    HashMap<String, ArrayList<OpenWeather5DayModel.Main>> weatherList = new HashMap<>();

    public RecyclerAdapter(Context context, HashMap<String, ArrayList<OpenWeather5DayModel.Main>> weatherList) {
        this.context = context;
        this.weatherList = weatherList;
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_recycler, null);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        Object[] keyset = weatherList.keySet().toArray();
        holder.tvWeatherDate.setText("-- "+ keyset[position]);
        holder.tvTempMin.setText("Min Temp - " + weatherList.get(String.valueOf(keyset[position])).get(0).getTempMin());
        holder.tvTempMax.setText("Max Temp - " + weatherList.get(String.valueOf(keyset[position])).get(weatherList.get(String.valueOf(keyset[position])).size() - 1).getTempMax());
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_temp_max)
        TextView tvTempMax;
        @BindView(R.id.tv_weather_date)
        TextView tvWeatherDate;
        @BindView(R.id.tv_temp_min)
        TextView tvTempMin;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

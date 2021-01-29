package com.mobile.agri10x;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.mobile.agri10x.Connection.GETRequest;
import com.mobile.agri10x.Connection.POSTRequest;
import com.mobile.agri10x.Model.Main;
import com.mobile.agri10x.Model.User;
import com.mobile.agri10x.Model.UserId;
import com.mobile.agri10x.weather.adapters.AutoCompleteAdapter;
import com.mobile.agri10x.weather.adapters.RecyclerAdapter;
import com.mobile.agri10x.weather.adapters.RecyclerAdapterAccuWeather;
import com.mobile.agri10x.weather.interfaces.IWeatherCallbackListener;
import com.mobile.agri10x.weather.models.AccuWeather5DayModel;
import com.mobile.agri10x.weather.models.AccuWeatherModel;
import com.mobile.agri10x.weather.models.LocationSearchModel;
import com.mobile.agri10x.weather.models.OpenWeather5DayModel;
import com.mobile.agri10x.weather.models.OpenWeatherModel;
import com.mobile.agri10x.weather.utils.WeatherConditions;

import java.text.ParseException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Weather extends AppCompatActivity implements IWeatherCallbackListener {
    String city_name=" ";
    UserId userId;
    private Button back;

    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_country)
    TextView tvCountry;
    @BindView(R.id.btn_get_5_days_weather)
    Button btnGet5DaysWeather;
    @BindView(R.id.iv_weather_icon)
    ImageView ivWeatherIcon;
    @BindView(R.id.rv_weather_data)
    RecyclerView rvWeatherData;
    @BindView(R.id.et_city_name)
    AutoCompleteTextView etCityName;
    @BindView(R.id.btn_get_weather)
    Button btnGetCurrentWeather;

    String OPEN_WEATHER_APP_ID = "3fe5296f1498ee903d17d04f79188235";
    String ACCU_WEATHER_APP_ID = "87ad516d1d4842838fcfebe843d933b1";

    LocationSearchModel mLocationSearchModel;
    @BindView(R.id.tv_info)
    TextView tvWeather;

    //Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        /*
        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_name);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/


        city_name = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            city_name = extras.getString("cityname");

            userId = new UserId();
            userId.setUserid(extras.getString("Userid"));
        }
        //Toast.makeText(this, "City name : "+city_name, Toast.LENGTH_SHORT).show();

        ButterKnife.bind(this);
        etCityName.setThreshold(2);
        etCityName.setAdapter(new AutoCompleteAdapter(Weather.this, ACCU_WEATHER_APP_ID));
        rvWeatherData.setLayoutManager(new LinearLayoutManager(this));

        //Toast.makeText(this, city_name, Toast.LENGTH_SHORT).show();

        back=findViewById(R.id.wet_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Weather.this.finish();
            }
        });


        etCityName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mLocationSearchModel = (LocationSearchModel) adapterView.getAdapter().getItem(i);

                etCityName.setText(mLocationSearchModel.getLocalizedName());
                Log.d("checkcity",mLocationSearchModel.getKey());
                //Toast.makeText(Weather.this,mLocationSearchModel.toString(),Toast.LENGTH_LONG).show();
                WeatherConditions.getAccuWeatherData(mLocationSearchModel.getKey(), ACCU_WEATHER_APP_ID, Weather.this, true);
                WeatherConditions.getAccuWeatherData5Days(mLocationSearchModel.getKey(), ACCU_WEATHER_APP_ID, Weather.this, true);

                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }

        });
        if(city_name==null || city_name.equalsIgnoreCase(" ")) {
            //Toast.makeText(this, "Enter city name", Toast.LENGTH_SHORT).show();
            new Weather.LoadEntitiyData().execute(Main.getIp()+"/UserInfo",new Gson().toJson(userId));
        }
        else {
            WeatherConditions.getAccuWeatherData(city_name, ACCU_WEATHER_APP_ID, Weather.this, true);
            WeatherConditions.getAccuWeatherData5Days(city_name, ACCU_WEATHER_APP_ID, Weather.this, true);
        }
    }

    class LoadEntitiyData extends AsyncTask<String, Integer, String> {
        AlertDialog dialog;

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Weather.Alert().pleaseWait();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            dialog.dismiss();
            if(s!=null) {
                if (s.equals("network")) {
                    new Weather.Alert().alert("Network !!!", getResources().getString(R.string.network_error_message));
                }else {
                    User u = User.extractFeatureFromJson(s);
                    if(u!=null) {
                        String address="";
                        if(u.getAddress().getAddress1()!=null){
                            address += u.getAddress().getAddress1();
                            if(u.getCity()!=null)
                            {
                                address+="\n"+u.getCity();
                                if(u.getCountry()!=null)
                                    address+="\n"+u.getCountry();
                            }
                            city_name = u.getCity();
                            Log.d("getcityfromload",city_name);
                           // Toast.makeText(Weather.this,"Your city not found. Please select city", Toast.LENGTH_SHORT).show();

                            WeatherConditions.getOpenWeatherData(city_name, OPEN_WEATHER_APP_ID, Weather.this);
                            WeatherConditions.getOpenWeatherData5Days(city_name, OPEN_WEATHER_APP_ID, Weather.this);
                        }
                        else {
                            Toast.makeText(Weather.this, "Please enter location.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String str;
            try {
                str = POSTRequest.fetchUserData(strings[0],strings[1], Weather.this);
            } catch (Exception e) {
                return "network";
            }
            return str;
        }
    }


    @Override
    public void getWeatherData(Object weatherModel, Boolean success, String errorMsg) {
        if (success) {
            Log.d("getsuccess", String.valueOf(success));
            if (weatherModel instanceof OpenWeatherModel) {

                OpenWeatherModel openWeatherModel = (OpenWeatherModel) weatherModel;
                Log.d("checkcomcity",openWeatherModel.getName());

                    tvCountry.setText("Country -- " + openWeatherModel.getSys().getCountry());
                    tvCity.setText("City -- " + openWeatherModel.getName());
                    tvWeather.setText("Temperature -- " + openWeatherModel.getMain().getTemp());
                    Glide.with(Weather.this)
                            .load("http://openweathermap.org/img/w/" + openWeatherModel.getWeather().get(0).getIcon() + ".png")
                            .into(ivWeatherIcon);



            } else if (weatherModel instanceof OpenWeather5DayModel) {

                OpenWeather5DayModel weatherBean = (OpenWeather5DayModel) weatherModel;
                try {
                    rvWeatherData.setAdapter(new RecyclerAdapter(Weather.this, weatherBean.getMinMaxTemp()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } else if (weatherModel instanceof AccuWeatherModel) {

                AccuWeatherModel accuWeatherModel = (AccuWeatherModel) weatherModel;
                tvWeather.setText("Current Temperature - " + accuWeatherModel.getTemperature().getMetric().getValue());
                tvCity.setText("City - " + mLocationSearchModel.getLocalizedName());
                tvCountry.setText("Country - " + mLocationSearchModel.getCountry().getLocalizedName());

                Glide.with(Weather.this)
                        .load("http://apidev.accuweather.com/developers/Media/Default/WeatherIcons/" + String.format("%02d", accuWeatherModel.getWeatherIcon()) + "-s" + ".png")
                        .into(ivWeatherIcon);

            } else if (weatherModel instanceof AccuWeather5DayModel) {

                AccuWeather5DayModel accuWeather5DayModel = (AccuWeather5DayModel) weatherModel;
                rvWeatherData.setAdapter(new RecyclerAdapterAccuWeather(Weather.this, accuWeather5DayModel));

            }
        }
        else {
            Log.d("getfailure", String.valueOf(success));
            Toast.makeText(Weather.this,"Your city not found. Please select city",Toast.LENGTH_SHORT).show();
        }
    }

    public class Alert {
        public void alert( String title, String body) {
            final AlertDialog.Builder Alert = new AlertDialog.Builder(Weather.this);
            Alert.setCancelable(false)
                    .setTitle(title)
                    .setMessage(body);
            Alert.setNegativeButton("Okey", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            Alert.create().show();
        }

        public AlertDialog pleaseWait() {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(Weather.this);
            View mView = getLayoutInflater().inflate(R.layout.alert_progress_spinning, null);
            ProgressBar pb = mView.findViewById(R.id.progressBar);
            mBuilder.setView(mView);
            mBuilder.setCancelable(false);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();
            return dialog;
        }
    }
}

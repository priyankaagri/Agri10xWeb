package com.mobile.agri10x;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.mobile.agri10x.Connection.POSTRequest;
import com.mobile.agri10x.Model.DBHelper;
import com.mobile.agri10x.Model.Main;
import com.mobile.agri10x.Model.Track;
import com.mobile.agri10x.Model.User;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service implements LocationListener {

    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    double latitude, longitude;
    LocationManager locationManager;
    Location location;
    private final Handler mHandler = new Handler();
    private Timer mTimer = null;
    long notify_interval = 10000;
    public static String str_receiver = "servicetutorial.service.receiver";
    Intent intent;
    DBHelper mydb;
    public static String name = "Employee";
    Track track;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mydb = new DBHelper(this);
        mTimer = new Timer();
        mTimer.schedule(new TimerTaskToGetLocation(), 10000, notify_interval);
        intent = new Intent(str_receiver);
        mTimer.schedule(new TimerTaskToShowLocation(), 15000, notify_interval);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void fn_getlocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //isGPSEnable = false;
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        //isNetworkEnable = true;
        boolean isPassiveEnabled = locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable) {

        } else {

            if (isNetworkEnable) {
                location = null;
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {

                        Log.e("latitude", location.getLatitude() + "");
                        Log.e("longitude", location.getLongitude() + "");

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Toast.makeText(this,"1"+ latitude, Toast.LENGTH_SHORT).show();
                        fn_update(location);
                        Toast.makeText(this,"2"+ latitude, Toast.LENGTH_SHORT).show();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                        track = new Track(sdf.format(new Date()),Double.toString(latitude),Double.toString(longitude),"123456");
                        new MyService.networkPOST().execute(Main.getIp()+"/SetPosition", new Gson().toJson(track));
                    }
                }

            }

            if (isGPSEnable) {
                location = null;
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
                if (locationManager != null) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    }
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location!=null){
                        Log.e("latitude",location.getLatitude()+"");
                        Log.e("longitude",location.getLongitude()+"");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        fn_update(location);
                        Toast.makeText(this, "L"+ latitude, Toast.LENGTH_SHORT).show();

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                        track = new Track(sdf.format(new Date()),Double.toString(latitude),Double.toString(longitude),"123456");
                        new MyService.networkPOST().execute(Main.getIp()+"/SetPosition", new Gson().toJson(track));
                    }
                }
            }
        }

    }

    private class TimerTaskToGetLocation extends TimerTask {
        @Override
        public void run() {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    fn_getlocation();
                }
            });

        }
    }

    private class TimerTaskToShowLocation extends TimerTask {
        @Override
        public void run() {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    fn_showlocation();
                }
            });

        }
    }

    public void fn_showlocation(){
        Cursor rs = mydb.getData(1);

        rs.moveToFirst();
        String nam = rs.getString(rs.getColumnIndex(DBHelper.LOCATION_COLUMN_NAME));
        Toast.makeText(this,"L" +nam, Toast.LENGTH_SHORT).show();
        if (!rs.isClosed())  {
            rs.close();
        }
    }

    private void fn_update(Location location){

        if(mydb.insertLocation(name,
                location.getLatitude(), location.getLongitude())){
            Toast.makeText(getApplicationContext(), "done",
                    Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(getApplicationContext(), "not done",
                    Toast.LENGTH_SHORT).show();
        }
        //intent.putExtra("latutide",location.getLatitude()+"");
        //intent.putExtra("longitude",location.getLongitude()+"");
        //sendBroadcast(intent);
    }

    class networkPOST extends AsyncTask<String, Integer, String> {

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

                Log.i("ok",s);


        }

        @Override
        protected String doInBackground(String... strings) {
            String str;
            try {
                str = POSTRequest.fetchUserData(strings[0],strings[1],MyService.this);
            } catch (Exception e) {
                return "network";
            }
            return str;
        }
    }
}
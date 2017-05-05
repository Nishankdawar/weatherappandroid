package com.example.weather;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import zh.wang.android.yweathergetter4a.WeatherInfo;
import zh.wang.android.yweathergetter4a.YahooWeather;
import zh.wang.android.yweathergetter4a.YahooWeatherInfoListener;

/**
 * Created by mukul on 1/5/17.
 */

public class Receiver extends BroadcastReceiver {
    Context mContext;
    WeatherInfo mWeatherInfo;
    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        if (intent.getAction() == Intent.ACTION_TIME_TICK || intent.getAction() == Intent.ACTION_TIME_CHANGED) {
            Toast.makeText(context, "Changed", Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
            Log.i("timeeee", sharedPreferences.getString("hour", "7") + "," + sharedPreferences.getString("min", "10"));
            String hourS = sharedPreferences.getString("hour", "7");
            String minS = sharedPreferences.getString("min", "10");
            int hour = Integer.parseInt(hourS);
            int min = Integer.parseInt(minS);
            DateFormat df = new SimpleDateFormat("HH:mm");
            String time = df.format(Calendar.getInstance().getTime());
            Log.i("timeme", time);
            getWeather();
        }
    }

    private void notifyUser() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setSmallIcon(R.drawable.icon);
        builder.setContentTitle("Today's Weather");
        builder.setContentText(mWeatherInfo.getForecastInfo1().toString());
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1000,builder.build());

    }

    private void getWeather() {
        Log.i("aaaaaa","Aaaaaa");
        Log.i("weatin","weatin");
        YahooWeather yahooWeather = YahooWeather.getInstance();
        yahooWeather.queryYahooWeatherByLatLon(mContext, 40.723, -74.0742, new YahooWeatherInfoListener() {
            @Override
            public void gotWeatherInfo(WeatherInfo weatherInfo, YahooWeather.ErrorType errorType) {
                Log.i("infoweather",weatherInfo+"");
                Log.i("infoweather",weatherInfo.getAtmosphereHumidity()+"");
                mWeatherInfo = weatherInfo;
                notifyUser();
            }
        });
    }
    private void goToCurrentLocation() {
        Log.i("ingotoCurrentLocation", "ingotoCurrentLocation");
        LocationManager locationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.PASSIVE_PROVIDER);
        Log.i("location", location + "");
        Toast.makeText(mContext, "Location fetched", Toast.LENGTH_SHORT).show();
    }
}

package com.mahathisol.salesapp.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.google.gson.reflect.TypeToken;
import com.mahathisol.salesapp.activities.DatabaseHelper;
import com.mahathisol.salesapp.app.AppController;
import com.mahathisol.salesapp.pojos.CreateCustomer;
import com.mahathisol.salesapp.utils.ApiEndPoint;
import com.mahathisol.salesapp.utils.SharedPrefKey;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.Response;

/**
 * Created by HP on 07/18/2017.
 */

public class GPS_Service extends Service {
    private LocationListener listener;
    private LocationManager locationManager;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    public final static int MINUTE = 1000 * 60;
    String TripId = "";
    String SalesmenId = "";
    private SharedPreferences mSecurePrefs;
    Location location; // location
    double latitude = 0; // latitude
    double longitude = 0; // longitude
    String provider;
    private DatabaseHelper myDb;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES =0;

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1 * MINUTE;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        myDb = new DatabaseHelper(this);
        Toast.makeText(getApplicationContext(), "GPS Service Started", Toast.LENGTH_SHORT).show();

        mSecurePrefs = getSharedPref();
        TripId = mSecurePrefs.getString(SharedPrefKey.TRIP_ID, "");
        SalesmenId = mSecurePrefs.getString(SharedPrefKey.CUSTOMER_ID, "");
        System.out.println("CUSTMORE_ID" + TripId);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Intent i = new Intent("location_updates");
                i.putExtra("coordinates",location.getLongitude() + "," +location.getLatitude());
                sendBroadcast(i);
                double longitude = location.getLongitude();
                double lattitude = location.getLatitude();
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String DateToStr = df.format(c.getTime());
//                Date curDate = new Date();
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.US);
//                String DateToStr = format.format(curDate);
              myDb.insertToTripGSPS(TripId,longitude,lattitude,DateToStr,SalesmenId);
               // updateCoordinates(TripId,longitude,lattitude,DateToStr);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        // getting GPS status
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isGPSEnabled && !isNetworkEnabled) {
            // notify user

        } else {
            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 60 * 10, 0, listener);
                Log.d("Network", "n/w");
//            if (locationManager != null) {
//                location = locationManager
//                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                if (location != null) {
//                    latitude = location.getLatitude();
//                    longitude = location.getLongitude();
//                }
//            }
            }
            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled) {
                if (location == null) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 60 * 10, 0, listener);
                    Log.d("GPS Enabled", "GPS Enabled");
//                if (locationManager != null) {
//                    location = locationManager
//                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                    if (location != null) {
//                        latitude = location.getLatitude();
//                        longitude = location.getLongitude();
//                    }
//                }
                }
            }
        }
      //  locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, listener);
    }
    private SharedPreferences getSharedPref() {
        if (mSecurePrefs == null) {
            mSecurePrefs = AppController.getInstance().getSharedPreferences();
        }
        return mSecurePrefs;
    }

    public void updateCoordinates(String TripId,Double longitude,Double lattitude,String DateToStr) {

        AndroidNetworking.post(ApiEndPoint.BASE_URL + ApiEndPoint.INSERT_TRIP)
                .addUrlEncodeFormBodyParameter("txttripid", TripId)
                .addUrlEncodeFormBodyParameter("txtlattitude", String.valueOf(lattitude))
                .addUrlEncodeFormBodyParameter("txtlongitude", String.valueOf(longitude))
                .addUrlEncodeFormBodyParameter("txtdateTime", DateToStr)



                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()

                .getAsOkHttpResponseAndParsed(new TypeToken<CreateCustomer>() {
                }, new OkHttpResponseAndParsedRequestListener<CreateCustomer>() {
                    @Override
                    public void onResponse(Response okHttpResponse, CreateCustomer cal) {


                        if (!cal.getCreate().getType().equalsIgnoreCase("success")) {
                            // Log.d("failure", user.getBlogList().toString());


                        } else {



                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        //progressActivity.showContent();




                        //Toast.makeText(getApplicationContext(), anError.getErrorDetail(), Toast.LENGTH_SHORT).show();

                    }
                });


    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(listener);
        }
    }

}

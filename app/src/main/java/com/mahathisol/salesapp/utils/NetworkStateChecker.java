package com.mahathisol.salesapp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.google.gson.reflect.TypeToken;
import com.mahathisol.salesapp.activities.DatabaseHelper;
import com.mahathisol.salesapp.activities.HomeActivity;
import com.mahathisol.salesapp.pojos.CreateCustomer;

import okhttp3.Response;

/**
 * Created by Belal on 1/27/2017.
 */

public class NetworkStateChecker extends BroadcastReceiver {

    //context and database helper object
    private Context context;
    private DatabaseHelper db;


    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        db = new DatabaseHelper(context);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        //if there is a network
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                Cursor cursor = db.getAllTripGPS();
                if (cursor.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        saveToTripGPS(
                                cursor.getString(cursor.getColumnIndex("id")),
                                cursor.getString(cursor.getColumnIndex("tripid")),
                                cursor.getString(cursor.getColumnIndex("lattitude")),
                                cursor.getString(cursor.getColumnIndex("longitude")),
                                cursor.getString(cursor.getColumnIndex("dateTime")),
                                cursor.getString(cursor.getColumnIndex("salesmenId"))

                        );
                    } while (cursor.moveToNext());
                }
            }
        }
    }


    public void saveToTripGPS(final String trid, String tripid, String lattitude, String longitude, String dateTime, String salesmenId) {

        AndroidNetworking.post(ApiEndPoint.BASE_URL + ApiEndPoint.INSERT_TRIP)

                .addUrlEncodeFormBodyParameter("txttripid", tripid)
                .addUrlEncodeFormBodyParameter("txtlattitude", lattitude)
                .addUrlEncodeFormBodyParameter("txtlongitude", longitude)
                .addUrlEncodeFormBodyParameter("txtdateTime", dateTime)
                .addUrlEncodeFormBodyParameter("txtsalesmenId", salesmenId)
                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()

                .getAsOkHttpResponseAndParsed(new TypeToken<CreateCustomer>() {
                }, new OkHttpResponseAndParsedRequestListener<CreateCustomer>() {
                    @Override
                    public void onResponse(Response okHttpResponse, CreateCustomer cal) {


                        if (!cal.getCreate().getType().equalsIgnoreCase("success")) {
                           // Toast.makeText(getApplicationContext(),"uploaded",Toast.LENGTH_LONG).show();

                        } else {
                            db.updateTripGPSStatus(trid,1);


                            //sending the broadcast to refresh the list
                            context.sendBroadcast(new Intent(HomeActivity.DATA_SAVED_BROADCAST));
                        }


                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error


                    }
                });


    }




}

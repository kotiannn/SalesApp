package com.mahathisol.salesapp.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.google.gson.reflect.TypeToken;
import com.mahathisol.salesapp.R;
import com.mahathisol.salesapp.activities.DatabaseHelper;
import com.mahathisol.salesapp.app.AppController;
import com.mahathisol.salesapp.pojos.CreateCustomer;
import com.mahathisol.salesapp.pojos.EventADForm;
import com.mahathisol.salesapp.pojos.GETTripId;
import com.mahathisol.salesapp.service.GPS_Service;
import com.mahathisol.salesapp.utils.ApiEndPoint;
import com.mahathisol.salesapp.utils.KeyboardUtil;
import com.mahathisol.salesapp.utils.SharedPrefKey;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.vlonjatg.progressactivity.ProgressActivity;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Response;


public class StopTripFormFragment extends BottomSheetDialogFragment  {


    public static final String TAG = StopTripFormFragment.class.getSimpleName();
    private final int REQ_PERMISSION = 999;
    private final int UPDATE_INTERVAL = 1000;
    private final int FASTEST_INTERVAL = 900;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.identry)
    EditText identry;
    @BindView(R.id.btn_save)
    AppCompatButton btnSave;
    @BindView(R.id.progress)
    ProgressActivity progress;
    @BindView(R.id.bottom_sheet)
    FrameLayout bottomSheet;
    Unbinder unbinder1;
    String stopdate = "";
    private DatabaseHelper db;
    private Unbinder unbinder;

    Drawable emptyDrawable;
    Drawable succDrawable;
    Drawable errorDrawable;
    String customer_id = "";
    String invoicectr = "";
    private SharedPreferences mSecurePrefs;
    private BottomSheetBehavior mBehavior;

    private boolean camflag = true;
    String TripId = "";
    String status = "3";
    InputMethodManager imm;
    String vendor_id = "";

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {

            if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }

//            else if(newState == BottomSheetBehavior.STATE_HIDDEN){
//                EventBus.getDefault().post(new EventADForm());
//            }
//            else if(newState == BottomSheetBehavior.STATE_COLLAPSED){
//                EventBus.getDefault().post(new EventADForm());
//
//            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSecurePrefs = getSharedPref();
        customer_id = mSecurePrefs.getString(SharedPrefKey.CUSTOMER_ID, "");
        invoicectr = mSecurePrefs.getString(SharedPrefKey.INVOICE_CTR, "");
        TripId = mSecurePrefs.getString(SharedPrefKey.TRIP_ID, "");
        System.out.println("CUSTMORE_ID" + customer_id);
        db = new DatabaseHelper(this.getActivity());
        //MapsInitializer.initialize(getContext());
    }
    private SharedPreferences getSharedPref() {
        if (mSecurePrefs == null) {
            mSecurePrefs = AppController.getInstance().getSharedPreferences();
        }
        return mSecurePrefs;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = View.inflate(getContext(), R.layout.fragment_stop_trip, null);

        unbinder = ButterKnife.bind(this, view);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            new KeyboardUtil(getActivity(), view);

        //fab = (FloatingActionButton) view.findViewById(R.id.fab);
        //circleDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.circle_shape);


        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(identry.getWindowToken(), 0);

        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |     WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        identry.setOnEditorActionListener(new DoneOnEditorActionListener());
        errorDrawable = new IconDrawable(getActivity(), Iconify.IconValue.zmdi_wifi_off)
                .colorRes(R.color.colorAccent);
       // getTripId(customer_id);
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        //if there is a network
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan

            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                intiateLoading();
            }
        }else {
            progress.showError(errorDrawable, "No Connection",
                    "We could not establish a connection with our servers. Try again when you are connected to the internet.",
                    "Try Again", errorClickListener);
        }
        setupToolbar();


        if (!runtime_permissions())

            enable_buttons();

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        stopdate = df.format(c.getTime());
      //  Toast.makeText(this.getActivity(),"dt"+stopdate,Toast.LENGTH_LONG).show();

        Log.d(TAG, "onCreateDialog()");


        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());

        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (true) {
//                    // EventBus.getDefault().post(new LatLongCarPojo(latLngselect));
//                    mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//                    // super.onBackPressed();
//
//                } else {
//                    Snackbar.make(view, "Select Wallet Location on the map!", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//
//                }
//            }
//        });


//==========FUll Screen BS=========
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
        // CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        View parent = (View) view.getParent();
        parent.setFitsSystemWindows(true);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(parent);
        view.measure(0, 0);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;
        bottomSheetBehavior.setPeekHeight(screenHeight);

        if (params.getBehavior() instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) params.getBehavior()).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        params.height = screenHeight;
        parent.setLayoutParams(params);

                //  getTripId(customer_id);

        return dialog;
    }


    void intiateLoading() {
        //loading
        emptyDrawable = new IconDrawable(getActivity(), Iconify.IconValue.zmdi_alert_circle)
                .colorRes(R.color.colorAccent);

        succDrawable = new IconDrawable(getActivity(), Iconify.IconValue.zmdi_cloud_done)
                .colorRes(R.color.colorAccent);

        errorDrawable = new IconDrawable(getActivity(), Iconify.IconValue.zmdi_wifi_off)
                .colorRes(R.color.colorAccent);
        //progress.showLoading();

        // progress.showLoading();

        //loading

    }

    public void getTripId(String cust_id) {

        AndroidNetworking.get(ApiEndPoint.BASE_URL + ApiEndPoint.BIND_STARTED_TRIPID)
                // AndroidNetworking.get("http://www.json-generator.com/api/json/get/ceMXczcMpu?indent=2")
                .addQueryParameter("sid",cust_id)
                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()

                .getAsOkHttpResponseAndParsed(new TypeToken<GETTripId>() {
                }, new OkHttpResponseAndParsedRequestListener<GETTripId>() {
                    @Override
                    public void onResponse(Response okHttpResponse, GETTripId adel) {

                            TripId = mSecurePrefs.getString(SharedPrefKey.TRIP_ID, "");
                            progress.showContent();


                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        //progressActivity.showContent();

                        progress.showError(errorDrawable, "No Connection",
                                "We could not establish a connection with our servers. Try again when you are connected to the internet.",
                                "Try Again", errorClickListener);

                        //Toast.makeText(getApplicationContext(), anError.getErrorDetail(), Toast.LENGTH_SHORT).show();

                    }
                });


    }

    private void enable_buttons() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(!TripId.isEmpty()) {
                    getActivity().stopService(new Intent(getActivity(), GPS_Service.class));
                    save();
                }else{

                    progress.showEmpty(emptyDrawable,
                            "No Trip Is Open At the Moment",
                            "Please wait");
                }
            }
        });

    }
    public void UpdateInvoiceCtr(String invcectr, String loginid) {

        AndroidNetworking.post(ApiEndPoint.BASE_URL + ApiEndPoint.UPDATE_INVOICECTR)
                .addQueryParameter("id",loginid)
                .addUrlEncodeFormBodyParameter("txtinvoiceCtr", invcectr)


                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()

                .getAsOkHttpResponseAndParsed(new TypeToken<CreateCustomer>() {
                }, new OkHttpResponseAndParsedRequestListener<CreateCustomer>() {
                    @Override
                    public void onResponse(Response okHttpResponse, CreateCustomer cal) {


                        if (!cal.getCreate().getType().equalsIgnoreCase("success")) {
                            // Log.d("failure", user.getBlogList().toString());

//                            progress.showEmpty(emptyDrawable,
//                                    "Failed To Add stop km",
//                                    "Try Again");
                        } else {



                        }


                    }

                    @Override
                    public void onError(ANError anError) {


                    }
                });


    }
    public void UpdateTripEntry(String startdt, String txtdate) {

        AndroidNetworking.post(ApiEndPoint.BASE_URL + ApiEndPoint.UPDATE_TRIP)
                .addQueryParameter("id",TripId)
                .addUrlEncodeFormBodyParameter("txtkmreadingend", startdt)
                .addUrlEncodeFormBodyParameter("txtenddatetime", txtdate)
                .addUrlEncodeFormBodyParameter("txtstatus", status)

                //  .addUrlEncodeFormBodyParameter("txtactive", txtactive)


                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()

                .getAsOkHttpResponseAndParsed(new TypeToken<CreateCustomer>() {
                }, new OkHttpResponseAndParsedRequestListener<CreateCustomer>() {
                    @Override
                    public void onResponse(Response okHttpResponse, CreateCustomer cal) {


                        if (!cal.getCreate().getType().equalsIgnoreCase("success")) {
                            // Log.d("failure", user.getBlogList().toString());

                            progress.showEmpty(emptyDrawable,
                                    "Failed To Add stop km",
                                    "Try Again");
                        } else {

//                            progressActivity.showEmpty(emptyDrawable,
//                                    "Routes not Available",
//                                    "Please Assign Routes");

                            //progressActivity.showContent();
                            progress.showEmpty(succDrawable,
                                    "Details Saved Successfully",
                                    "");

                            imm.hideSoftInputFromWindow(identry.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            mSecurePrefs.edit().putString(SharedPrefKey.TRIP_STATUS, "3").apply();
                            //getting all the unsynced names
                            Cursor cursor = db.getUnsyncedTripProduct();
                            if (cursor.moveToFirst()) {
                                do {
                                    //calling the method to save the unsynced name to MySQL
                                    saveName(
                                            cursor.getString(cursor.getColumnIndex("id")),
                                            cursor.getString(cursor.getColumnIndex("tripId")),
                                            cursor.getString(cursor.getColumnIndex("productId")),
                                            cursor.getString(cursor.getColumnIndex("openingStock")),
                                            cursor.getString(cursor.getColumnIndex("inwardQty")),
                                            cursor.getString(cursor.getColumnIndex("closingStock")),
                                            cursor.getString(cursor.getColumnIndex("loaddate")),
                                            cursor.getString(cursor.getColumnIndex("returnquantity")),
                                            cursor.getString(cursor.getColumnIndex("soldquantity"))
                                    );
                                } while (cursor.moveToNext());
                            }

                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        //progressActivity.showContent();


                        progress.showError(errorDrawable, "No Connection",
                                "We could not establish a connection with our servers. Try again when you are connected to the internet.",
                                "Try Again", errorClickListener);

                        //Toast.makeText(getApplicationContext(), anError.getErrorDetail(), Toast.LENGTH_SHORT).show();

                    }
                });


    }
    public void saveName(final String tid,final String tripid, final String productid, final String openingstock, final String inwardqty, final String closingstock, final String loaddate, final String returnqty,final String soldquant) {

        AndroidNetworking.post(ApiEndPoint.BASE_URL + ApiEndPoint.DOWNLOAD_FROM_SQLITE)

                .addQueryParameter("id", tid)

//                .addUrlEncodeFormBodyParameter("txttripId", tripid)
//
//                .addUrlEncodeFormBodyParameter("txtproductId", productid)
//
//                .addUrlEncodeFormBodyParameter("txtopeningStock", openingstock)
//                .addUrlEncodeFormBodyParameter("txtinwardQty", inwardqty)
//                .addUrlEncodeFormBodyParameter("txtclosingStock", closingstock)
//              //  .addUrlEncodeFormBodyParameter("txtloaddate", loaddate)
//                .addUrlEncodeFormBodyParameter("txtreturnquantity", returnqty)
                .addUrlEncodeFormBodyParameter("txtsoldquantity", soldquant)
                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()

                .getAsOkHttpResponseAndParsed(new TypeToken<CreateCustomer>() {
                }, new OkHttpResponseAndParsedRequestListener<CreateCustomer>() {
                    @Override
                    public void onResponse(Response okHttpResponse, CreateCustomer cal) {


                        if (!cal.getCreate().getType().equalsIgnoreCase("success")) {
//                            progress.showEmpty(emptyDrawable,
//                                    "Routes not Available",
//                                    "Please Assign Routes");
//
                        } else {



                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error


                    }
                });


    }
    private View.OnClickListener errorClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (true) {
                progress.showLoading();
                //  callTimeLineFreshRestApi(customer_id);
            }
        }
    };

    public void save() {

        if (!validate()) {
            onLoginFailed();
            return;
        }


        // Log.d(getFormatedDate(dateview.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd") + quantity.getText().toString() + delboySelid + routeSelid, "tst");
        UpdateTripEntry(identry.getText().toString(), getFormatedDate(stopdate, "dd-MM-yyyy HH:mm:ss", "yyyy-MM-dd HH:mm:ss"));
        UpdateInvoiceCtr(invoicectr,customer_id);
        progress.showLoading();
    }

    public void onLoginFailed() {
        //  Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        //btn.setEnabled(true);
    }


    public boolean validate() {
        boolean valid = true;


        String strname = identry.getText().toString();
        // String stridroutevia = idroutevia.getText().toString();


        if (strname.isEmpty()) {
            identry.setError("Please enter stop km");
            valid = false;
        } else {
            identry.setError(null);
        }


        return valid;
    }


    public void setupFlag() {


    }

    private boolean runtime_permissions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                enable_buttons();

            } else {
                runtime_permissions();
            }
        }
    }
    private void setupToolbar() {
        toolbar.setTitle("Add Ending km details");

        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                getDialog().dismiss();
                EventBus.getDefault().post(new EventADForm());
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated()");


    }


    // Create GoogleApiClient instance


    // Check for permission to access Location
    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    // Asks for permission
    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQ_PERMISSION
        );
    }

    // Verify user's response of the permission requested



    // App cannot work without the permissions
    private void permissionsDenied() {
        Log.w(TAG, "permissionsDenied()");
        // TODO close app and warn user
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG, "onStart()");
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume()");
//        if (mapView != null) {
//            mapView.onResume();
//        }
        super.onResume();


    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");
//        if (mapView != null) {
//            mapView.onDestroy();
//        }
        super.onDestroy();

        unbinder.unbind();


    }

    @Override
    public void onLowMemory() {
        Log.w(TAG, "onLowMemory()");
//        if (mapView != null) {
//            mapView.onLowMemory();
//        }
        super.onLowMemory();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        // getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        // getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

//        ButterKnife.bind(this, rootView);
//        ButterKnife.bind(this, rootView);
//        ButterKnife.bind(this, rootView);
        return rootView;
    }

//    @Override
//    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//        String strdate = "" + dayOfMonth + "-" + (++monthOfYear) + "-" + year;
//
//        dateview.setText(strdate);
//    }


    public static String getFormatedDate(String strDate, String sourceFormate,
                                         String destinyFormate) {
        SimpleDateFormat df;
        df = new SimpleDateFormat(sourceFormate);
        Date date = null;
        try {
            date = df.parse(strDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        df = new SimpleDateFormat(destinyFormate);
        return df.format(date);

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        EventBus.getDefault().post(new EventADForm());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);;

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
      //  unbinder1.unbind();
    }


    class DoneOnEditorActionListener implements TextView.OnEditorActionListener {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            // TODO Auto-generated method stub
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                save();
                return true;
            }
            return false;
        }
    }


//    @Override
//    public void onItemClick(Item item) {
//        mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//    }

}

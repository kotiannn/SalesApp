package com.mahathisol.salesapp.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
import android.widget.Toast;

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
import com.mahathisol.salesapp.pojos.TripProductUpld;
import com.mahathisol.salesapp.pojos.TripUpload;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Response;


public class StartTripFormFragment extends BottomSheetDialogFragment {


    public static final String TAG = StartTripFormFragment.class.getSimpleName();
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
    String customer_id = "";
    @BindView(R.id.idesugamano)
    EditText idesugamano;
    Unbinder unbinder2;
    @BindView(R.id.totalvalue)
    TextView totalvalue;
    private DatabaseHelper db;
    private SharedPreferences mSecurePrefs;
    String startdate = "";
    String textview = "";
    String lattitude = "";
    String longitude = "";
    private BroadcastReceiver broadcastReceiver;
    private Unbinder unbinder;
    String status = "2";
    Drawable emptyDrawable;
    Drawable succDrawable;
    Drawable errorDrawable;
    TripProductUpld.Tripproductlist addr;
    TripUpload.Triplist addrs;
    String TripId = "";
    String RouteId = "";
    String Routecode = "";
    String Invoicectr = "";
String TStatus = "";
    List<GETTripId> adlist;
    private LocationManager locationManager;
    boolean isGPSEnabled = false;
    private BottomSheetBehavior mBehavior;

    private boolean camflag = true;


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
        db = new DatabaseHelper(this.getActivity());
        mSecurePrefs = getSharedPref();
        customer_id = mSecurePrefs.getString(SharedPrefKey.CUSTOMER_ID, "");
        System.out.println("CUSTMORE_ID" + customer_id);
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

        View view = View.inflate(getContext(), R.layout.fragment_start_trip, null);

        unbinder = ButterKnife.bind(this, view);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            new KeyboardUtil(getActivity(), view);

        //fab = (FloatingActionButton) view.findViewById(R.id.fab);
        //circleDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.circle_shape);


        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(identry.getWindowToken(), 0);

        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |     WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        identry.setOnEditorActionListener(new DoneOnEditorActionListener());

        getTripId(customer_id);

        setupToolbar();

        intiateLoading();


        if (!runtime_permissions())
            enable_buttons();

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        startdate = df.format(c.getTime());
        //  Toast.makeText(this.getActivity(),"dt"+startdate,Toast.LENGTH_LONG).show();

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
        progress.showLoading();

        //loading

    }

    private void enable_buttons() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!TripId.isEmpty()) {
                    getActivity().startService(new Intent(getActivity(), GPS_Service.class));
                    save();
                } else {

                    progress.showEmpty(emptyDrawable,
                            "No Trip Is Open At the Moment",
                            "Please wait");
                }
            }
        });

    }

    public void getTripId(String cust_id) {

        AndroidNetworking.get(ApiEndPoint.BASE_URL + ApiEndPoint.BIND_TRIPID)
                // AndroidNetworking.get("http://www.json-generator.com/api/json/get/ceMXczcMpu?indent=2")
                .addQueryParameter("sid", cust_id)
                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()

                .getAsOkHttpResponseAndParsed(new TypeToken<GETTripId>() {
                }, new OkHttpResponseAndParsedRequestListener<GETTripId>() {
                    @Override
                    public void onResponse(Response okHttpResponse, GETTripId adel) {
                        if (!((adel == null) || adel.getId().isEmpty())) {
                            // Log.d("failure", user.getBlogList().toString());
                            final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                // Call your Alert message
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                            TripId = (adel.getId());
                            RouteId = (adel.getRouteid());
                            Routecode = (adel.getCode());
                            Invoicectr = (adel.getInvoiceCtr());
                            TStatus = (adel.getStatus());
                            totalvalue.setText(" Total Value : " + adel.getTotalValue());
                            progress.showContent();
                            mSecurePrefs.edit().putString(SharedPrefKey.TRIP_NO, adel.getTripno()).apply();
                            mSecurePrefs.edit().putString(SharedPrefKey.TRIP_ID, TripId).apply();
                            mSecurePrefs.edit().putString(SharedPrefKey.ROUTE_ID, RouteId).apply();
                            mSecurePrefs.edit().putString(SharedPrefKey.ROUTE_CODE, Routecode).apply();
                            mSecurePrefs.edit().putString(SharedPrefKey.INVOICE_CTR, Invoicectr).apply();
                            mSecurePrefs.edit().putString(SharedPrefKey.TRIP_STATUS, TStatus).apply();
                            mSecurePrefs.edit().putString(SharedPrefKey.TOT_VAL, adel.getTotalValue()).apply();
                        } else {
                            progress.showEmpty(emptyDrawable,
                                    "No Trip Is Open At the Moment",
                                    "Please wait");

                        }
                        //

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

    private View.OnClickListener errorClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (true) {
                progress.showLoading();
                //  callTimeLineFreshRestApi(customer_id);
            }
        }
    };

    // sendCreateCustomer(idname.getText().toString(),idpassword.getText().toString(),idphone.getText().toString(),idemail.getText().toString(),iddoorNo.getText().toString(),idadd1.getText().toString(),idadd2.getText().toString(),idadd3.getText().toString(),citySelid,idpin.getText().toString(),iddist.getText().toString(),idpost.getText().toString(),idstate.getText().toString(),routeSelid,isactive);
    public void UpdateTripEntry(String startdt, String sugamano, String txtdate) {

        AndroidNetworking.post(ApiEndPoint.BASE_URL + ApiEndPoint.UPDATE_TRIP)
                .addQueryParameter("id", TripId)
                .addUrlEncodeFormBodyParameter("txtkmreadingstart", startdt)
                .addUrlEncodeFormBodyParameter("txtstartdatetime", txtdate)
                .addUrlEncodeFormBodyParameter("txteSugamaNo", sugamano)
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
                                    "Failed To Add start km",
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
                            mSecurePrefs.edit().putString(SharedPrefKey.TRIP_STATUS, "2").apply();
                            uploadTripProductToSqlite(TripId);
                            //  uploadTripToSqlite(TripId);


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

    public void uploadTripProductToSqlite(String cust_id) {

        AndroidNetworking.get(ApiEndPoint.BASE_URL + ApiEndPoint.GET_TRIP_PRODUCT_TABLETOSQLITE)
                // AndroidNetworking.get("http://www.json-generator.com/api/json/get/ceMXczcMpu?indent=2")
                .addQueryParameter("cid", cust_id)
                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()
                .getAsOkHttpResponseAndParsed(new TypeToken<TripProductUpld>() {
                }, new OkHttpResponseAndParsedRequestListener<TripProductUpld>() {
                    @Override
                    public void onResponse(Response okHttpResponse, TripProductUpld adel) {

                        List<TripProductUpld.Tripproductlist> custdatalist = adel.getTripproductlist();


                        if (!custdatalist.isEmpty()) {

                            int lengthData = adel.getTripproductlist().size();
                            for (int i = 0; i <= lengthData - 1; i++) {
                                addr = custdatalist.get(i);
                                saveToLocalStorage(addr.getId(), addr.getTripId(), addr.getProductId(), addr.getOpeningStock(), addr.getInwardQty(), addr.getClosingStock(),addr.getLoaddate(),addr.getReturnquantity(),addr.getSoldquantity());
                            }
                            //   addr = custdatalist.get(0);


                        } else {
                            progress.showEmpty(emptyDrawable,
                                    "Trip Product is empty",
                                    "Please wait");
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

    private void saveToLocalStorage(String Id, String tripId, String productid, String openingstock, String inwardqty, String closingstock, String loaddate, String returnqty,String soldquant) {

        db.addTripProduct(Id, tripId, productid, openingstock, inwardqty, closingstock, loaddate ,returnqty,soldquant);

    }



    public void save() {

        if (!validate()) {
            onLoginFailed();
            return;
        }


        // Log.d(getFormatedDate(dateview.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd") + quantity.getText().toString() + delboySelid + routeSelid, "tst");
        UpdateTripEntry(identry.getText().toString(), idesugamano.getText().toString(), getFormatedDate(startdate, "dd-MM-yyyy HH:mm:ss", "yyyy-MM-dd HH:mm:ss"));
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
            identry.setError("Please enter starting km");
            valid = false;
        } else {
            identry.setError(null);
        }


        return valid;
    }


    public void setupFlag() {


    }


    private void setupToolbar() {
        toolbar.setTitle("Add Starting km details");

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
    // Verify user's response of the permission requested
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        Log.d(TAG, "onRequestPermissionsResult()");
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case REQ_PERMISSION: {
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // Permission granted
//                    if (checkPermission()) {
//
//
//                    }
//
//                    // map.setMyLocationEnabled(true);
//                    // getLastKnownLocation();
//
//                } else {
//                    // Permission denied
//                    permissionsDenied();
//                }
//                break;
//            }
//        }
//    }


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
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    textview = (String) intent.getExtras().get("coordinates");

                    Date curDate = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                    String DateToStr = format.format(curDate);
                    System.out.println(DateToStr);
                    Toast.makeText(getActivity(), "coordinates" + textview, Toast.LENGTH_LONG).show();

                }
            };

        }
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("location_updates"));

    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");
//        if (mapView != null) {
//            mapView.onDestroy();
//        }
        super.onDestroy();
        if (broadcastReceiver != null) {

            getActivity().unregisterReceiver(broadcastReceiver);
        }
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

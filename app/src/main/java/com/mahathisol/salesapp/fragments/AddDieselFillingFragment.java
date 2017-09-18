package com.mahathisol.salesapp.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.google.gson.reflect.TypeToken;
import com.mahathisol.salesapp.R;
import com.mahathisol.salesapp.pojos.CreateDiesel;
import com.mahathisol.salesapp.pojos.EventADForm;
import com.mahathisol.salesapp.pojos.EventRefresh;
import com.mahathisol.salesapp.pojos.Vehicleli;
import com.mahathisol.salesapp.utils.ApiEndPoint;
import com.mahathisol.salesapp.utils.KeyboardUtil;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.vlonjatg.progressactivity.ProgressActivity;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Response;

/**
 * Created by Admin on 02-08-2017.
 */

public class AddDieselFillingFragment extends BottomSheetDialogFragment implements DatePickerDialog.OnDateSetListener {
    public static final String TAG = AddCustomerFormFragment.class.getSimpleName();
    private final int REQ_PERMISSION = 999;
    private final int UPDATE_INTERVAL = 1000;
    private final int FASTEST_INTERVAL = 900;
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;

    Drawable emptyDrawable;
    Drawable succDrawable;
    Drawable errorDrawable;
    boolean isDboyloaded = false;
    boolean isRoutloaded = false;
    String citySelid = null;
    String routeSelid = null;
    String isactive = "1";
    //Spinner
    List<Vehicleli.Vehiclelist> rl;

    String selectedid = null;
    String selectedDboy = null;
    InputMethodManager imm;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.idvehicle)
    AppCompatSpinner idvehicle;
    @BindView(R.id.idrate)
    EditText idrate;
    @BindView(R.id.idlitre)
    EditText idlitre;
    @BindView(R.id.idamount)
    EditText idamount;
    @BindView(R.id.idbunk)
    EditText idbunk;
    @BindView(R.id.idkm)
    EditText idkm;
    @BindView(R.id.idremarks)
    EditText idremarks;
    @BindView(R.id.btn_exit)
    AppCompatButton btnExit;
    @BindView(R.id.btn_save)
    AppCompatButton btnSave;
    @BindView(R.id.progress)
    ProgressActivity progressActivity;
    @BindView(R.id.bottom_sheet)
    FrameLayout bottomSheet;
    Unbinder unbinder1;
    @BindView(R.id.date)
    EditText dateview;
    Unbinder unbinder2;


    private Unbinder unbinder;
    private BottomSheetBehavior mBehavior;
    private boolean camflag = true;
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
    private View.OnClickListener errorClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (true) {
                progressActivity.showLoading();
                //  callTimeLineFreshRestApi(customer_id);
            }
        }
    };

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //MapsInitializer.initialize(getContext());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = View.inflate(getContext(), R.layout.fragment_add_diesel, null);

        unbinder = ButterKnife.bind(this, view);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            new KeyboardUtil(getActivity(), view);

        //fab = (FloatingActionButton) view.findViewById(R.id.fab);
        //circleDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.circle_shape);


        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(idrate.getWindowToken(), 0);

        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |     WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        idrate.setOnEditorActionListener(new DoneOnEditorActionListener());


        setupToolbar();

        intiateLoading();
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());

        dateview.setText(formattedDate);
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

        //================
//        Calendar c = Calendar.getInstance();
//        System.out.println("Current time => " + c.getTime());
//
//        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//        String formattedDate = df.format(c.getTime());


//=======
//        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//
//        builder
//                .setTitle("My Title")
//                .setView(view)
//                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        if(true) // disable positive button if this is empty
//                        {
//                            Toast.makeText(getActivity(), "enter something!", Toast.LENGTH_SHORT).show();
//                        }
//                        else { //myListener.onSet(myEditText.getText().toString());
//                            //
//                            // }
//
//
//                    }
//                })
//                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // do nothing
//                    }
//                });
//
//        dialog = builder.create();
        //===
        callForVehicles("");
        return dialog;
    }

    @OnClick(R.id.date)
    public void datecheckoutsubmit() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getActivity().getFragmentManager(), "checkout");
    }
    void intiateLoading() {
        //loading
        emptyDrawable = new IconDrawable(getActivity(), Iconify.IconValue.zmdi_alert_circle)
                .colorRes(R.color.colorAccent);

        succDrawable = new IconDrawable(getActivity(), Iconify.IconValue.zmdi_cloud_done)
                .colorRes(R.color.colorAccent);

        errorDrawable = new IconDrawable(getActivity(), Iconify.IconValue.zmdi_wifi_off)
                .colorRes(R.color.colorAccent);
 progressActivity.showLoading();

        //loading

    }

    public void callForVehicles(String cust_id) {

        AndroidNetworking.get(ApiEndPoint.BASE_URL + ApiEndPoint.GET_VEHICLES_LIST)
                // AndroidNetworking.get("http://www.json-generator.com/api/json/get/ceMXczcMpu?indent=2")

                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()

                .getAsOkHttpResponseAndParsed(new TypeToken<Vehicleli>() {
                }, new OkHttpResponseAndParsedRequestListener<Vehicleli>() {
                    @Override
                    public void onResponse(Response okHttpResponse, Vehicleli adel) {


                        if (adel.getVehiclelist().isEmpty()) {
                            // Log.d("failure", user.getBlogList().toString());

                            progressActivity.showEmpty(emptyDrawable,
                                    "Vehicles not Available",
                                    "HemsFood");
                        } else {

                            Log.d("TIME_LINE_REQUEST_TAG", adel.getVehiclelist().toString());


                            if (!adel.getVehiclelist().isEmpty()) {

                                rl = adel.getVehiclelist();

                                routeSelid = rl.get(0).getId();

                                List<String> sl = new ArrayList<String>();

                                for (Vehicleli.Vehiclelist rv : rl) {

                                    sl.add(rv.getRegno());

                                }

//                                route.setItems("Ice Cream Sandwich", "Jelly Bean", "KitKat", "Lollipop", "Marshmallow");
                                ArrayAdapter adapteradapterroute = new ArrayAdapter<String>(getActivity(),
                                        android.R.layout.simple_spinner_item, sl);

                                idvehicle.setAdapter(adapteradapterroute);
                                //  route.setItems(sl);
                                //  route.set(0);
//                                route.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
//
//                                    @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
//                                       // Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
//
//                                        routeSelid=dbl.get(position).getId();
//
//                                    }
//                                });


                                idvehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                        routeSelid = rl.get(pos).getId(); //check

                                    }

                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });

                                //route.setSelectedIndex(0);


                                isRoutloaded = true;
                                if (isRoutloaded) {
                                    progressActivity.showContent();
                                } else {

                                }


                            } else {
                                progressActivity.showEmpty(emptyDrawable,
                                        "Vehicles not Available",
                                        "HemsFood");

                            }


                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        //progressActivity.showContent();

                        progressActivity.showError(errorDrawable, "No Connection",
                                "We could not establish a connection with our servers. Try again when you are connected to the internet.",
                                "Try Again", errorClickListener);

                        //Toast.makeText(getApplicationContext(), anError.getErrorDetail(), Toast.LENGTH_SHORT).show();

                    }
                });


    }


    //sendCreateCustomer(routeSelid, dateview.getText().toString(), idrate.getText().toString(), idlitre.getText().toString(), idamount.getText().toString(), idbunk.getText().toString(), idkm.getText().toString());
    public void sendCreateCustomer(final String txtvehicle, final String txtfilldate, final String txtlitre,final String txtrate,  final String txtamount, final String txtbunk, final String txtremarks, final String txtkm ) {

        AndroidNetworking.post(ApiEndPoint.BASE_URL + ApiEndPoint.CREATE_DIESEL)
                .addUrlEncodeFormBodyParameter("txtvehicleid", txtvehicle)
                .addUrlEncodeFormBodyParameter("txtfilldate", txtfilldate)
                .addUrlEncodeFormBodyParameter("txtlitre", txtlitre)
                .addUrlEncodeFormBodyParameter("txtrate", txtrate)
                .addUrlEncodeFormBodyParameter("txtamount", txtamount)
                .addUrlEncodeFormBodyParameter("txtbunk", txtbunk)
                .addUrlEncodeFormBodyParameter("txtremarks", txtremarks)
                .addUrlEncodeFormBodyParameter("txtkmreading", txtkm)

                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()

                .getAsOkHttpResponseAndParsed(new TypeToken<CreateDiesel>() {
                }, new OkHttpResponseAndParsedRequestListener<CreateDiesel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, CreateDiesel cal) {


                        if (!cal.getCreate().getType().equalsIgnoreCase("success")) {
                            // Log.d("failure", user.getBlogList().toString());
                         //   saveNameToLocalStorage(txtname, txtphoneNo, txtemailId, txtaddress1, txtaddress2, txtaddress3, txtpin, txtcity, txtstateid, txtgstinno, txtactive, NAME_SYNCED_WITH_SERVER);
                            progressActivity.showEmpty(emptyDrawable,
                                    "Failed To Add Diesel Filling Details",
                                    "Server Error");
                        } else {

//                            progressActivity.showEmpty(emptyDrawable,
//                                    "Routes not Available",
//                                    "Please Assign Routes");
                            // saveNameToLocalStorage(txtname, NAME_SYNCED_WITH_SERVER);
                            //progressActivity.showContent();
                            progressActivity.showEmpty(succDrawable,
                                    "Diesel Filling Details Added Successfully",
                                    "");

                            imm.hideSoftInputFromWindow(idvehicle.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                            //  VendorActivity.hideKeyboard(getActivity());


//                            getActivity().getWindow().setSoftInputMode(
//                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


//                            Handler handler = new Handler();
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//
//
//                                }
//                            }, 5000);

                            EventBus.getDefault().post(new EventADForm());
                            // mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            EventBus.getDefault().post(new EventRefresh());
                            new CountDownTimer(1000, 1000) {
                                public void onFinish() {
                                    // When timer is finished


                                    getDialog().dismiss();
                                    // Execute your code here
                                }

                                //
                                public void onTick(long millisUntilFinished) {


                                    // millisUntilFinished    The amount of time until finished.
                                }
                            }.start();

//                            EventBus.getDefault().post(new EventADForm());
//                            mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            //progressActivity.show


                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        //progressActivity.showContent();


                        progressActivity.showError(errorDrawable, "No Connection",
                                "We could not establish a connection with our servers. Try again when you are connected to the internet.",
                                "Try Again", errorClickListener);

                        //Toast.makeText(getApplicationContext(), anError.getErrorDetail(), Toast.LENGTH_SHORT).show();

                    }
                });


    }


    @OnClick(R.id.btn_save)
    public void onclicksave() {


//        if (!validate()) {
//            onLoginFailed();
//            return;
//        }
//
//        Log.d(getFormatedDate(dateview.getText().toString(),"dd-MM-yyyy","yyyy-MM-dd")+quantity.getText().toString()+delboySelid+routeSelid,"tst");
//        sendAssignDelivery(getFormatedDate(dateview.getText().toString(),"dd-MM-yyyy","yyyy-MM-dd"),quantity.getText().toString(),delboySelid,routeSelid);
//        progressActivity.showLoading();

        save();


    }

    @OnClick(R.id.btn_exit)
    public void onclickexit() {
        //  getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        EventBus.getDefault().post(new EventADForm());

        getDialog().dismiss();
//            this.getActivity().setResult(RESULT_CANCELED);
        //getActivity().finish();
    }

    public void save() {

        if (!validate()) {
            onLoginFailed();
            return;
        }

        // Log.d(getFormatedDate(dateview.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd") + quantity.getText().toString() + delboySelid + routeSelid, "tst");
        sendCreateCustomer(routeSelid, getFormatedDate(dateview.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd"), idlitre.getText().toString(), idrate.getText().toString(), idamount.getText().toString(), idbunk.getText().toString(), idremarks.getText().toString(), idkm.getText().toString());
        progressActivity.showLoading();
    }


    public void onLoginFailed() {
        //  Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        //btn.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;


        String strrate = idrate.getText().toString();
        String strlitre = idlitre.getText().toString();

        String stramount = idamount.getText().toString();

        String strbunk = idbunk.getText().toString();
        String strkm = idkm.getText().toString();

        boolean isDboyloaded = false;

        boolean isRoutloaded = false;


//        String citySelid = null;
//
//        String routeSelid = null;


        if (strrate.isEmpty()) {
            idrate.setError("Please Enter Rate");
            valid = false;
        } else {
            idrate.setError(null);
        }

        if (strlitre.isEmpty()) {
            idlitre.setError("Please Enter Litres");
            valid = false;
        } else {
            idlitre.setError(null);
        }


        if (stramount.isEmpty()) {
            idamount.setError("Amount cant be Blank !");
            valid = false;
        } else {
            idamount.setError(null);
        }
        if (strbunk.isEmpty()) {
            idbunk.setError("Please Enter Bunk");
            valid = false;
        } else {
            idbunk.setError(null);
        }
        if (strkm.isEmpty()) {
            idbunk.setError("Please Enter KM");
            valid = false;
        } else {
            idbunk.setError(null);
        }


        if (routeSelid.isEmpty() || routeSelid == null) {
            Snackbar.make(getView(), "Please Select Vehicle", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            valid = false;
        } else {
            // route.setError(null);
        }
        return valid;
    }

    public void setupFlag() {


    }

    private void setupToolbar() {
        toolbar.setTitle("Add Diesel Filling");

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


    // Create GoogleApiClient instance

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated()");


    }

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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    if (checkPermission()) {
                    }
                    // map.setMyLocationEnabled(true);
                    // getLastKnownLocation();

                } else {
                    // Permission denied
                    permissionsDenied();
                }
                break;
            }
        }
    }

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

//    @Override
//    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//        String strdate = "" + dayOfMonth + "-" + (++monthOfYear) + "-" + year;
//
//        dateview.setText(strdate);
//    }

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
        //  ButterKnife.bind(this, rootView);
//        unbinder1 = ButterKnife.bind(this, rootView);
//        unbinder1 = ButterKnife.bind(this, rootView);
//        unbinder2 = ButterKnife.bind(this, rootView);
        return rootView;
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
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String strdate = "" + dayOfMonth + "-" + (++monthOfYear) + "-" + year;

        dateview.setText(strdate);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder1.unbind();
//        unbinder1.unbind();
    }

    class DoneOnEditorActionListener implements TextView.OnEditorActionListener {


        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            // TODO Auto-generated method stub
            if (actionId == EditorInfo.IME_ACTION_DONE) {InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                save();
                return true;
            }
            return false;
        }
    }

}

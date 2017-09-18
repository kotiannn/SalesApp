package com.mahathisol.salesapp.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import com.mahathisol.salesapp.app.AppController;
import com.mahathisol.salesapp.pojos.CreateCustomer;
import com.mahathisol.salesapp.pojos.EventADForm;
import com.mahathisol.salesapp.pojos.EventRefresh;
import com.mahathisol.salesapp.pojos.GetRemarks;
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
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Response;


public class EditRemarksFormFragment extends BottomSheetDialogFragment {


    public static final String TAG = EditRemarksFormFragment.class.getSimpleName();
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

    Unbinder unbinder2;
    @BindView(R.id.dateview)
    EditText dateview;
    Unbinder unbinder3;


    private Unbinder unbinder;

    Drawable emptyDrawable;
    Drawable succDrawable;
    Drawable errorDrawable;
    String customer_id = "";
    private SharedPreferences mSecurePrefs;
    private BottomSheetBehavior mBehavior;
    String TripId = "";
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
        mSecurePrefs = getSharedPref();
        customer_id = mSecurePrefs.getString(SharedPrefKey.CUSTOMER_ID, "");
        TripId = mSecurePrefs.getString(SharedPrefKey.TRIP_ID, "");
        System.out.println("CUSTMORE_ID" + TripId);

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

        View view = View.inflate(getContext(), R.layout.fragment_edit_remarks, null);

        unbinder = ButterKnife.bind(this, view);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            new KeyboardUtil(getActivity(), view);

        //fab = (FloatingActionButton) view.findViewById(R.id.fab);
        //circleDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.circle_shape);


        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(identry.getWindowToken(), 0);

        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |     WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        identry.setOnEditorActionListener(new DoneOnEditorActionListener());

        setupToolbar();

        intiateLoading();
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

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
        bindRemarks(TripId,customer_id);

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

    public void bindRemarks(String tid, String cid) {

        AndroidNetworking.get(ApiEndPoint.BASE_URL + ApiEndPoint.BIND_REMARKS)
                // AndroidNetworking.get("http://www.json-generator.com/api/json/get/ceMXczcMpu?indent=2")
                .addQueryParameter("sid", cid)
                .addQueryParameter("stid", tid)
                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()

                .getAsOkHttpResponseAndParsed(new TypeToken<GetRemarks>() {
                }, new OkHttpResponseAndParsedRequestListener<GetRemarks>() {
                    @Override
                    public void onResponse(Response okHttpResponse, GetRemarks adel) {
                        if (!(adel == null) || !(adel.getRemarks().isEmpty())) {

                            identry.setText(adel.getRemarks());
                            progress.showContent();


                        } else {
                          //  identry.setText("");
                            progress.showContent();

                        }
                        //

                    }

                    @Override
                    public void onError(ANError anError) {
                        progress.showError(errorDrawable, "No Connection",
                                "We could not establish a connection with our servers. Try again when you are connected to the internet.",
                                "Try Again", errorClickListener);


                    }
                });


    }

    // sendUpdateRemarks(idname.getText().toString(),idpassword.getText().toString(),idphone.getText().toString(),idemail.getText().toString(),iddoorNo.getText().toString(),idadd1.getText().toString(),idadd2.getText().toString(),idadd3.getText().toString(),citySelid,idpin.getText().toString(),iddist.getText().toString(),idpost.getText().toString(),idstate.getText().toString(),routeSelid,isactive);
    public void sendUpdateRemarks(String remrks,String stid) {

        AndroidNetworking.post(ApiEndPoint.BASE_URL + ApiEndPoint.UPDATE_REMARKS)
             .addQueryParameter("id",stid)
                .addUrlEncodeFormBodyParameter("txtremarks", remrks)


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
                                    "Failed To Edit Your Remarks!",
                                    "Try Again");
                        } else {

//                            progressActivity.showEmpty(emptyDrawable,
//                                    "Routes not Available",
//                                    "Please Assign Routes");

                            //progressActivity.showContent();
                            progress.showEmpty(succDrawable,
                                    "Remarks Details Edited Successfully",
                                    "");

                            imm.hideSoftInputFromWindow(identry.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

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
                            EventBus.getDefault().post(new EventRefresh());
                            new CountDownTimer(1000, 1000) {
                                public void onFinish() {
                                    // When timer is finished

                                    // mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

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

    @OnClick(R.id.btn_save)
    public void onclicksave() {


//        if (!validate()) {
//            onLoginFailed();
//            return;
//        }
//
//        Log.d(getFormatedDate(dateview.getText().toString(),"dd-MM-yyyy","yyyy-MM-dd")+quantity.getText().toString()+delboySelid+routeSelid,"tst");
        sendUpdateRemarks(identry.getText().toString(),TripId);
        progress.showLoading();

        save();


    }


    public void save() {

        if (!validate()) {
            onLoginFailed();
            return;
        }


        // Log.d(getFormatedDate(dateview.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd") + quantity.getText().toString() + delboySelid + routeSelid, "tst");
        //   sendCreateRate(idroutename.getText().toString(), getFormatedDate(dateview.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd"), isactive);
       // progress.showLoading();
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
            identry.setError("Please enter your remarks");
            valid = false;
        } else {
            identry.setError(null);
        }


        return valid;
    }


    public void setupFlag() {


    }


    private void setupToolbar() {
        toolbar.setTitle("Edit Remarks");

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

        // unbinder.unbind();


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
//
//        unbinder3 = ButterKnife.bind(this, rootView);
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
//        unbinder1.unbind();
        //unbinder2.unbind();
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

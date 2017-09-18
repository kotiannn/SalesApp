package com.mahathisol.salesapp.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.mahathisol.salesapp.R;
import com.mahathisol.salesapp.activities.DatabaseHelper;
import com.mahathisol.salesapp.app.AppController;
import com.mahathisol.salesapp.pojos.EventADForm;
import com.mahathisol.salesapp.pojos.EventRefresh;
import com.mahathisol.salesapp.pojos.ProductDetails;
import com.mahathisol.salesapp.pojos.Statesli;
import com.mahathisol.salesapp.utils.KeyboardUtil;
import com.mahathisol.salesapp.utils.SharedPrefKey;
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

public class AddPaymentFragment extends BottomSheetDialogFragment implements OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    int id = 0;
    public static final String TAG = AddPaymentFragment.class.getSimpleName();
    private final int REQ_PERMISSION = 999;
    private final int UPDATE_INTERVAL = 1000;
    private final int FASTEST_INTERVAL = 900;
    List<ProductDetails> productList = new ArrayList<>();
    @BindView(R.id.invoiceid)
    TextView invoiceid;
    @BindView(R.id.idpaymentmode)
    AppCompatSpinner idpaymentmode;
    @BindView(R.id.date)
    EditText date;
    @BindView(R.id.idamount)
    EditText idamount;
    @BindView(R.id.idinstrno)
    EditText idinstrno;
    @BindView(R.id.idremarks)
    EditText idremarks;
    @BindView(R.id.btn_exit)
    AppCompatButton btnExit;
    Unbinder unbinder2;
    @BindView(R.id.linear1)
    LinearLayout linear1;
    Unbinder unbinder3;


    private DatabaseHelper db;
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private BroadcastReceiver broadcastReceiver;
    String cid = "";
    String cname = "";
    String cstate = "";
    String cmobile = "";
    String ccity = "";
    String cgst = "";

    String cinvoicedate = "";
    @BindView(R.id.btn_save)
    AppCompatButton btnSave;

    @BindView(R.id.progress)
    ProgressActivity progressActivity;
    String active = "1";
    Drawable emptyDrawable;
    Drawable succDrawable;
    Drawable errorDrawable;
    boolean isDboyloaded = false;
    boolean isRoutloaded = false;
    String citySelid = null;
    String in_payment = null;
    String pr_categoryid = null;
    String pr_typeid = null;
    String pr_variantid = null;
    String isactive = "1";
    String invoice = "";
    String isMRP = "MRP";
    //Spinner
    List<Statesli.Statelist> rl;
    String selectedid = null;
    String selectedDboy = null;
    InputMethodManager imm;

    @BindView(R.id.bottom_sheet)
    FrameLayout bottomSheet;
    String Invoiceno = "";
    String route_counter = "";
    int invoicectr = 0;
    String route_code = "";
    String RetailOrMrp = "";
    String pay_amount = "";
    String invid = "";
    Unbinder unbinder1;
    private SharedPreferences mSecurePrefs;
    private Unbinder unbinder;
    private BottomSheetBehavior mBehavior;
    private boolean camflag = true;
    String pid = "";
    String tripid = "";
    String ghsncode = "";
    String gsgstrate = "";
    String gcgstrate = "";
    String gmrp = "";
    String gretail = "";
    String gsgstamount = "";
    String gcgstamount = "";
    String productcode = "";
    String gigstrate = "";
    String gigstamt = "";
    private IntentIntegrator qrScan;
    String InStock = "";
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {

            if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }

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
//        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        db = new DatabaseHelper(this.getActivity());

        mSecurePrefs = getSharedPref();
        route_code = mSecurePrefs.getString(SharedPrefKey.ROUTE_CODE, "");
        route_counter = mSecurePrefs.getString(SharedPrefKey.INVOICEDEATILS_CTR, "");
        pay_amount = mSecurePrefs.getString(SharedPrefKey.PAY_AMOUNT, "");
        RetailOrMrp = mSecurePrefs.getString(SharedPrefKey.RETAIL_MRP, "");
        tripid = mSecurePrefs.getString(SharedPrefKey.TRIP_ID, "");
invid = mSecurePrefs.getString(SharedPrefKey.INVOICEID_TO_PRINT,"");
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

        View view = View.inflate(getContext(), R.layout.fragment_add_payment, null);

        unbinder = ButterKnife.bind(this, view);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            new KeyboardUtil(getActivity(), view);

        //fab = (FloatingActionButton) view.findViewById(R.id.fab);
        //circleDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.circle_shape);


        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(idamount.getWindowToken(), 0);
        db = new DatabaseHelper(this.getActivity());
        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |     WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        idpaymentmode.setOnItemSelectedListener(new PaymentModeSelect());

        setupToolbar();

        intiateLoading();
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());

        date.setText(formattedDate);
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
        idamount.setText(pay_amount);
        //invoice = route_counter + "-" + route_code;
       getInvoiceNo(invid);
      //  invoiceid.setText("Invoice No : " + route_counter + "-" + route_code);


        loadSpinnerData();//for Pr company

        return dialog;
    }

public void getInvoiceNo(String invid){
    invoice = db.getINVOICENOONID(invid);
    if(invoice.equals("")){}else {
        invoiceid.setText("Invoice No : " + invoice);
        mSecurePrefs.edit().putString(SharedPrefKey.INVOICE_DETAILS_NO, invoice).apply();
    }
}
    void intiateLoading() {
        //loading
        emptyDrawable = new IconDrawable(getActivity(), Iconify.IconValue.zmdi_alert_circle)
                .colorRes(R.color.colorAccent);

        succDrawable = new IconDrawable(getActivity(), Iconify.IconValue.zmdi_cloud_done)
                .colorRes(R.color.colorAccent);

        errorDrawable = new IconDrawable(getActivity(), Iconify.IconValue.zmdi_wifi_off)
                .colorRes(R.color.colorAccent);
        // idquantity.addTextChangedListener(new EditTextListener());
        // progressActivity.showLoading();

        //loading

    }


    private void loadSpinnerData() {
        List<String> lables = db.getAllLabels("PaymentMode");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, lables);
        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        idpaymentmode.setAdapter(dataAdapter);
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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String strdate = "" + dayOfMonth + "-" + (++monthOfYear) + "-" + year;

        date.setText(strdate);
    }

    class PaymentModeSelect implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            String label = parent.getItemAtPosition(position).toString();
            int sid = db.getPaymentId(label, "PaymentMode");
            in_payment = String.valueOf(sid);
            if (in_payment.equals("1") || in_payment.equals("2")) {
                linear1.setVisibility(View.VISIBLE);

            } else {
                linear1.setVisibility(View.GONE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
    @OnClick(R.id.btn_exit)
    public void onclickexit() {

        getDialog().dismiss();
    }

    @OnClick(R.id.btn_save)
    public void onclicksave() {


        db.updatePaymentDetails(invoice, in_payment, idamount.getText().toString(), idinstrno.getText().toString(), date.getText().toString(), idremarks.getText().toString());
        progressActivity.showEmpty(succDrawable,
                "Payment Details Added Successfully",
                "");

       // mSecurePrefs.edit().putString(SharedPrefKey.PAY_AMOUNT, "").apply();
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

        new AddInvoiceFragment().show(getActivity().getSupportFragmentManager(), AddInvoiceFragment.TAG);
    }

    public void save() {

        if (!validate()) {
            onLoginFailed();
            return;
        }


    }


    public void onLoginFailed() {
        //  Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        //btn.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;


//
//
        return valid;
    }

    public void setupFlag() {


    }

    private void setupToolbar() {
        toolbar.setTitle("Payment Details");

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

        //  unbinder.unbind();


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
        setHasOptionsMenu(true);

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
        setHasOptionsMenu(true);
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

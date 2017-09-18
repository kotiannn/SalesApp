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
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mahathisol.salesapp.R;
import com.mahathisol.salesapp.activities.DatabaseHelper;
import com.mahathisol.salesapp.app.AppController;
import com.mahathisol.salesapp.pojos.EventADForm;
import com.mahathisol.salesapp.pojos.InvoiceDetailsList;
import com.mahathisol.salesapp.utils.KeyboardUtil;
import com.mahathisol.salesapp.utils.SharedPrefKey;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.vlonjatg.progressactivity.ProgressActivity;

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

public class EditInvoice extends BottomSheetDialogFragment implements OnItemSelectedListener {


    public static final String TAG = EditInvoice.class.getSimpleName();
    private final int REQ_PERMISSION = 999;
    String customer_id = "";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
//    @BindView(R.id.idinvoiceno)
//    EditText idinvoiceno;
    @BindView(R.id.idinv)
    AppCompatSpinner idinv;
    @BindView(R.id.btn_save)
    AppCompatButton btnSave;
    @BindView(R.id.progress)
    ProgressActivity progress;
    @BindView(R.id.bottom_sheet)
    FrameLayout bottomSheet;
    Unbinder unbinder1;
    private BottomSheetBehavior mBehavior;
    private DatabaseHelper db;
    private boolean camflag = true;
    private SharedPreferences mSecurePrefs;
    String routeSelid = null;
    Drawable emptyDrawable;
    Drawable succDrawable;
    Drawable errorDrawable;
    boolean isRoutloaded = false;
    InputMethodManager imm;
    String vendor_id = "";
    int id = 0;
    private Unbinder unbinder;
    List<InvoiceDetailsList> productList = new ArrayList<>();
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

        db = new DatabaseHelper(this.getActivity());
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

        View view = View.inflate(getContext(), R.layout.fragment_edit_invoice_search, null);

        unbinder = ButterKnife.bind(this, view);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            new KeyboardUtil(getActivity(), view);

        //fab = (FloatingActionButton) view.findViewById(R.id.fab);
        //circleDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.circle_shape);


        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.hideSoftInputFromWindow(idinvoiceno.getWindowToken(), 0);

        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |     WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //idinvoiceno.setOnEditorActionListener(new DoneOnEditorActionListener());
        idinv.setOnItemSelectedListener(this);
        setupToolbar();

        intiateLoading();


        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        //  startdate = df.format(c.getTime());

        // dateview.setText(startdate);
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
      loadSpinnerData();
        // callForCustomers("");
        //  GenerateInvoiceNo(trip_id);

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
        // progress.showLoading();

        //loading

    }


    private void loadSpinnerData() {
        List<String> lables = db.getInvoiceNo("Invoice");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, lables);
        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        idinv.setAdapter(dataAdapter);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String label = parent.getItemAtPosition(position).toString();
        mSecurePrefs.edit().putString(SharedPrefKey.INVOICE_DETAILS_NO,label).apply();
        int sid = db.getINvoiceId(label, "Invoice");
        routeSelid = String.valueOf(sid);
        //idinvoiceno.setText(routeSelid);
      //  productList = db.getAllInvoiceForEdit(routeSelid);
    }



    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }



    @OnClick(R.id.btn_save)
    public void onclickNext() {
        EditInvoiceDetailsList cf = EditInvoiceDetailsList.newInstance(id);
        cf.show(getActivity().getSupportFragmentManager(), EditInvoiceDetailsList.TAG);



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

        //  db.addInvoice(idinvoiceno.getText().toString(), getFormatedDate(dateview.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd"),routeSelid, custList.get(0).getName(),  custList.get(0).getMobile(),  custList.get(0).getEmailid(),  custList.get(0).getAddress1(),  custList.get(0).getAddress2(),  custList.get(0).getAddress3(),  custList.get(0).getCity(), custList.get(0).getStateid(), custList.get(0).getGstinno(), "1", compList.get(0).getName(), compList.get(0).getMobile(), compList.get(0).getEmailid(),compList.get(0).getAddress1(),compList.get(0).getAddress2(),compList.get(0).getAddress3(),compList.get(0).getCity(),compList.get(0).getPincode(),compList.get(0).getStateid(),compList.get(0).getGstinno(),compList.get(0).getTaxincludedamt(), compList.get(0).getReportfooter(),"1",compList.get(0).getShowbankdetails(),compList.get(0).getAccholdername(),compList.get(0).getAccno(),compList.get(0).getBankname(),compList.get(0).getBankbranch(),compList.get(0).getIfsccode(),"Hems Food");

        //  imm.hideSoftInputFromWindow(idcustomer.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        getDialog().dismiss();
        //  new AddInvoiceDetailsFragment().show(getActivity().getSupportFragmentManager(), AddInvoiceDetailsFragment.TAG);
        // Log.d(getFormatedDate(dateview.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd") + quantity.getText().toString() + delboySelid + routeSelid, "tst");

        // progress.showLoading();
    }

    public void onLoginFailed() {
        //  Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        //btn.setEnabled(true);
    }


    public boolean validate() {
        boolean valid = true;


        return valid;
    }


    public void setupFlag() {


    }


    private void setupToolbar() {
        toolbar.setTitle("Edit Invoice");

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

                // save();
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

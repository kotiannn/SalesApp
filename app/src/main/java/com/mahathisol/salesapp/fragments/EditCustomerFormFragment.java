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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mahathisol.salesapp.R;
import com.mahathisol.salesapp.activities.DatabaseHelper;
import com.mahathisol.salesapp.pojos.EventADForm;
import com.mahathisol.salesapp.pojos.EventCustomer;
import com.mahathisol.salesapp.pojos.EventRefresh;
import com.mahathisol.salesapp.pojos.Statesli;
import com.mahathisol.salesapp.utils.KeyboardUtil;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.vlonjatg.progressactivity.ProgressActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class EditCustomerFormFragment extends BottomSheetDialogFragment implements AdapterView.OnItemSelectedListener {


    public static final String TAG = EditCustomerFormFragment.class.getSimpleName();
    private final int REQ_PERMISSION = 999;
    private final int UPDATE_INTERVAL = 1000;
    private final int FASTEST_INTERVAL = 900;

    Unbinder unbinder2;

    @BindView(R.id.idiretail)
    CheckBox idiretail;
    Unbinder unbinder3;
    @BindView(R.id.idname)
    EditText idname;
    @BindView(R.id.idphone)
    EditText idphone;
    @BindView(R.id.idemail)
    EditText idemail;
    Unbinder unbinder4;
    private DatabaseHelper db;
    String id = "";
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.idadd1)
    EditText idadd1;
    @BindView(R.id.idadd2)
    EditText idadd2;
    @BindView(R.id.idadd3)
    EditText idadd3;
    @BindView(R.id.idcity)
    EditText idcity;
    @BindView(R.id.idpin)
    EditText idpin;
    @BindView(R.id.idstate)
    AppCompatSpinner idstate;
    @BindView(R.id.idgstinno)
    EditText idgstinno;
    @BindView(R.id.idisactive)
    CheckBox idisactive;
    @BindView(R.id.btn_exit)
    AppCompatButton btnExit;
    @BindView(R.id.btn_save)
    AppCompatButton btnSave;
    @BindView(R.id.progress)
    ProgressActivity progress;
    @BindView(R.id.bottom_sheet)
    FrameLayout bottomSheet;
    Unbinder unbinder1;
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
//    @BindView(R.id.idretyppassword)
//    EditText idretyppassword;

    private Unbinder unbinder;

    Drawable emptyDrawable;
    Drawable succDrawable;
    Drawable errorDrawable;


    boolean isDboyloaded = false;

    boolean isRoutloaded = false;


    String routeSelid = null;


    String isactive = "1";
    String isMRP = "Retail";
    //Spinner
    List<Statesli.Statelist> rl;


    String selectedid = null;

    String selectedDboy = null;


    private BottomSheetBehavior mBehavior;

    private boolean camflag = true;


    InputMethodManager imm;

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

        //MapsInitializer.initialize(getContext());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = View.inflate(getContext(), R.layout.fragment_edit_customer, null);

        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        db = new DatabaseHelper(this.getActivity());
        idstate.setOnItemSelectedListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            new KeyboardUtil(getActivity(), view);

        //fab = (FloatingActionButton) view.findViewById(R.id.fab);
        //circleDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.circle_shape);


        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(idadd1.getWindowToken(), 0);

        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |     WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        idadd1.setOnEditorActionListener(new DoneOnEditorActionListener());


        setupToolbar();

        intiateLoading();

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


        loadSpinnerData();
        // callForStates("");


        return dialog;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void doThis(EventCustomer eventCustomer) {


        id = String.valueOf(eventCustomer.getDdata().getId());
        idname.setText(eventCustomer.getDdata().getName());
        idphone.setText( eventCustomer.getDdata().getMobile());
        idemail.setText(eventCustomer.getDdata().getEmailid());
        idadd1.setText(eventCustomer.getDdata().getAddress1());
        idadd2.setText(eventCustomer.getDdata().getAddress2());
        idadd3.setText(eventCustomer.getDdata().getAddress3());
        idcity.setText(eventCustomer.getDdata().getCity());
        idpin.setText(eventCustomer.getDdata().getPincode());
        routeSelid = String.valueOf(eventCustomer.getDdata().getStateid());
        idgstinno.setText(eventCustomer.getDdata().getGstinno());
        //  idstate.setText(eventCustomer.getDdata().getState());


        if (eventCustomer.getDdata().getActive().equals("0")) {
            idisactive.setChecked(false);
        } else {
            idisactive.setChecked(true);
        }
        if (eventCustomer.getDdata().getRetailmrp().equals("MRP")) {
            idiretail.setChecked(false);
        } else {
            idiretail.setChecked(true);
        }

    }

    @OnClick(R.id.idisactive)
    public void onClickCheckBox(CheckBox ch) {

        if (ch.isChecked()) {

            isactive = "1";
        } else {
            isactive = "0";
        }
    }

    @OnClick(R.id.idiretail)
    public void onClickRetail(CheckBox ch) {

        if (ch.isChecked()) {

            isMRP = "Retail";
        } else {

            isMRP = "MRP";
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
        //   progress.showLoading();

        //loading

    }

    private void loadSpinnerData() {
        List<String> lables = db.getAllLabels("State");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        idstate.setAdapter(dataAdapter);
        int stid = Integer.parseInt(routeSelid);


        String label = db.getCustomerState(stid, "State");

        // = String.valueOf(dataAdapter.getPosition("Hot Channa Red Chilli"));
        int selectionPosition = dataAdapter.getPosition(label);
        idstate.setSelection(selectionPosition);
//        if (!routeSelid.equals(null)) {
//            int spinnerPosition = dataAdapter.getPosition(routeSelid);
//            idstate.setSelection(spinnerPosition);
//        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // On selecting a spinner item

        String label = parent.getItemAtPosition(position).toString();
        int sid = db.getId(label, "State");
        routeSelid = String.valueOf(sid);
        //   Toast.makeText(parent.getContext(), "You selected: " + routeSelid, Toast.LENGTH_LONG).show();


    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

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
        //  editCustomerDetails(id, idadd1.getText().toString(), idadd2.getText().toString(), idadd3.getText().toString(), idpin.getText().toString(), idcity.getText().toString(), routeSelid, idgstinno.getText().toString(), isactive,isMRP,NAME_NOT_SYNCED_WITH_SERVER);

        //  progress.showLoading();

        save();


    }

    private void editCustomerDetails(String id, String name,String phone,String email,String txtaddress1, String txtaddress2, String txtaddress3, String txtpin, String txtcity, String txtstateid, String txtgstinno, String txtactive, String retailmrp, int status) {

        db.updateCustomer(id, name,phone,email,txtaddress1, txtaddress2, txtaddress3, txtpin, txtcity, txtstateid, txtgstinno, txtactive, retailmrp, status);
        progress.showEmpty(succDrawable,
                "Customer Details Edited Successfully",
                "");
        EventBus.getDefault().post(new EventADForm());
        // mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        EventBus.getDefault().post(new EventRefresh());
        imm.hideSoftInputFromWindow(idstate.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
        editCustomerDetails(id,idname.getText().toString(),idphone.getText().toString(),idemail.getText().toString(),idadd1.getText().toString(), idadd2.getText().toString(), idadd3.getText().toString(), idpin.getText().toString(), idcity.getText().toString(), routeSelid, idgstinno.getText().toString(), isactive, isMRP, NAME_NOT_SYNCED_WITH_SERVER);
        //  progress.showLoading();
    }

    public void onLoginFailed() {

    }


    public boolean validate() {
        boolean valid = true;


        String strname = idname.getText().toString();
        String stridemail = idemail.getText().toString();

        String stridphone = idphone.getText().toString();

        String stridadd1 = idadd1.getText().toString();
        String stridadd2 = idadd2.getText().toString();


        String stridpin = idpin.getText().toString();


//3remaining


        boolean isDboyloaded = false;

        boolean isRoutloaded = false;


//        String citySelid = null;
//
//        String routeSelid = null;


        if (stridadd1.isEmpty()) {
            idadd1.setError("please enter address line1");
            valid = false;
        } else {
            idadd1.setError(null);
        }


        if (stridadd1.isEmpty() || stridpin.isEmpty()) {
            //idemail.setError("enter the eggs");

            Snackbar.make(getDialog().getWindow().getDecorView().getRootView(), "Please fill the forms", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            valid = false;
        } else {
            //idemail.setError(null);
        }


        if (routeSelid.isEmpty() || routeSelid == null) {
            //route.setError("enter the eggs");
            Snackbar.make(getView(), "Please Select State", Snackbar.LENGTH_LONG)
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
        toolbar.setTitle("Edit Customer Profile");

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
    public void onStop() {
        Log.i(TAG, "onStop()");
//        if (mapView != null) {
//            mapView.onResume();
//        }
        super.onStop();

        EventBus.getDefault().unregister(this);
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


     //   unbinder4 = ButterKnife.bind(this, rootView);
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
      //  unbinder4.unbind();

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

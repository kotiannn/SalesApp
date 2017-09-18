package com.mahathisol.salesapp.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.mahathisol.salesapp.R;
import com.mahathisol.salesapp.activities.BloothPrinterActivity;
import com.mahathisol.salesapp.activities.DatabaseHelper;
import com.mahathisol.salesapp.app.AppController;
import com.mahathisol.salesapp.pojos.EventADForm;
import com.mahathisol.salesapp.pojos.EventInvoiceDetails;
import com.mahathisol.salesapp.pojos.EventRefresh;
import com.mahathisol.salesapp.pojos.ProductDetails;
import com.mahathisol.salesapp.pojos.UploadProductType;
import com.mahathisol.salesapp.utils.KeyboardUtil;
import com.mahathisol.salesapp.utils.SharedPrefKey;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.vlonjatg.progressactivity.ProgressActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EditInvoiceDetailsFormFragment extends BottomSheetDialogFragment implements OnItemSelectedListener, View.OnClickListener {

    String id = "";
    String invid = "";
    String oldquantity = "";
    String pid = "";
    public static final String TAG = EditInvoiceDetailsFormFragment.class.getSimpleName();
    private final int REQ_PERMISSION = 999;
    private final int UPDATE_INTERVAL = 1000;
    private final int FASTEST_INTERVAL = 900;
    List<ProductDetails> productList = new ArrayList<>();
    List<ProductDetails> plist = new ArrayList<>();
    Unbinder unbinder2;
    @BindView(R.id.idinvoiceno)
    TextView idinvoiceno;
    @BindView(R.id.idinvdate)
    TextView idinvdate;
    @BindView(R.id.idcname)
    TextView idcname;
    @BindView(R.id.idcmobile)
    TextView idcmobile;
    @BindView(R.id.idccity)
    TextView idccity;
    @BindView(R.id.idcgstinno)
    TextView idcgstinno;
    @BindView(R.id.idprComapny)
    AppCompatSpinner idprComapny;
    @BindView(R.id.idprCAtegory)
    AppCompatSpinner idprCAtegory;
    @BindView(R.id.idprType)
    AppCompatSpinner idprType;
    @BindView(R.id.idprVariant)
    AppCompatSpinner idprVariant;
    @BindView(R.id.idquantity)
    EditText idquantity;

    @BindView(R.id.idrate)
    EditText idrate;
    int token = 0;
    @BindView(R.id.idinstock)
    TextView idinstock;


    Unbinder unbinder3;
    @BindView(R.id.idhsncode)
    EditText idhsncode;

    @BindView(R.id.idtotalamt)
    EditText idtotalamt;
    @BindView(R.id.idtaxableamt)
    EditText idtaxableamt;



    @BindView(R.id.idgstrate)
    EditText idgstrate;
    @BindView(R.id.idgstamt)
    EditText idgstamt;
    Unbinder unbinder4;

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
    String pr_companyid = null;
    String pr_categoryid = null;
    String pr_typeid = null;
    String pr_variantid = null;
    String isactive = "1";
    String isMRP = "MRP";
    //Spinner
    List<UploadProductType.Prtypelist> rl;
   // List<Statesli.Statelist> rl;
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
    Unbinder unbinder1;
    private SharedPreferences mSecurePrefs;
    private Unbinder unbinder;
    private BottomSheetBehavior mBehavior;
    private boolean camflag = true;
    String InStock = "";
    String soldquant = "";
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
    String tripid = "";
    String TR = "";

    private IntentIntegrator qrScan;

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
//        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        db = new DatabaseHelper(this.getActivity());

        mSecurePrefs = getSharedPref();
        route_code = mSecurePrefs.getString(SharedPrefKey.ROUTE_CODE, "");
        route_counter = mSecurePrefs.getString(SharedPrefKey.INVOICE_CTR, "");
        RetailOrMrp = mSecurePrefs.getString(SharedPrefKey.RETAIL_MRP, "");
        cid = mSecurePrefs.getString(SharedPrefKey.CID, "");
        cname = mSecurePrefs.getString(SharedPrefKey.CNAME, "");
        cstate = mSecurePrefs.getString(SharedPrefKey.CSTATE, "");
        cmobile = mSecurePrefs.getString(SharedPrefKey.CMOBIL, "");
        ccity = mSecurePrefs.getString(SharedPrefKey.CCITY, "");
        cgst = mSecurePrefs.getString(SharedPrefKey.CGST, "");
        cinvoicedate = mSecurePrefs.getString(SharedPrefKey.INDATE, "");
        tripid = mSecurePrefs.getString(SharedPrefKey.TRIP_ID,"");

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

        View view = View.inflate(getContext(), R.layout.fragment_edit_invoice_details, null);

        unbinder = ButterKnife.bind(this, view);

        EventBus.getDefault().register(this);
        db = new DatabaseHelper(this.getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            new KeyboardUtil(getActivity(), view);

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(idquantity.getWindowToken(), 0);


        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |     WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        idprComapny.setOnItemSelectedListener(new ProductCompanySelect());
        idprCAtegory.setOnItemSelectedListener(new CategorySelect());
        idprType.setOnItemSelectedListener(new ProductTypeSelct());
        idprVariant.setOnItemSelectedListener(new ProductVariantSelect());


        idquantity.setOnEditorActionListener(new DoneOnEditorActionListener());
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

        loadSpinnerData();//for Pr company
   // loadSpinnerCategory();//for Pr category
       //loadProductType();
//        loadSpinnerVariantEdit();
        return dialog;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void doThis(EventInvoiceDetails eventInvoiceDetails) {


        id = eventInvoiceDetails.getDdata().getId();
        invid = eventInvoiceDetails.getDdata().getInvoiceId();
        idcname.setText("Name : " + eventInvoiceDetails.getDdata().getCuName());
        idcmobile.setText("Mobile : " + eventInvoiceDetails.getDdata().getCuMobile());
        idcgstinno.setText("GSTIN No : " + eventInvoiceDetails.getDdata().getCuGSTINNO());
        idccity.setText("City : " + eventInvoiceDetails.getDdata().getCuCity());
        idinvoiceno.setText("Invoice No : " + eventInvoiceDetails.getDdata().getInvoiceNo());
        idhsncode.setText(eventInvoiceDetails.getDdata().getPHSNcode());
        idquantity.setText(eventInvoiceDetails.getDdata().getQuantity());
        oldquantity = eventInvoiceDetails.getDdata().getQuantity();
        pid = eventInvoiceDetails.getDdata().getPrid();
        InStock = db.getInstock(pid,tripid);
        if(InStock.equals("")) {
            InStock = String.valueOf(0);
            idinstock.setText("In Stock : " + InStock);
        }else {
            idinstock.setText("In Stock : " + InStock);
        }
        idrate.setText(eventInvoiceDetails.getDdata().getRate());
        idhsncode.setText(eventInvoiceDetails.getDdata().getPHSNcode());
        gcgstrate = eventInvoiceDetails.getDdata().getCGSTRate();
        gsgstrate = eventInvoiceDetails.getDdata().getSGSTRate();
        gigstrate = eventInvoiceDetails.getDdata().getIGSTRate();
        pr_typeid = eventInvoiceDetails.getDdata().getPTypeId();
        pr_variantid = eventInvoiceDetails.getDdata().getPVarintId();
        pr_categoryid = eventInvoiceDetails.getDdata().getProdcategoryid();
        pid = eventInvoiceDetails.getDdata().getProductId();
        //getPRoduct(pid);

        calculateedit();

    }


    public void calculateedit() {
        float GSTRate;
        if (gigstrate.equals("")) {


            GSTRate = Float.valueOf(gsgstrate)+ Float.valueOf(gcgstrate);
            String gstrate = String.valueOf(GSTRate);
            idgstrate.setText(gstrate);
            float quantity = Float.valueOf(idquantity.getText().toString());
            float rate = Float.valueOf(idrate.getText().toString());

            float TA = (quantity) * (rate);
            String stt = String.format("%.2f", TA);
            idtotalamt.setText(stt);
            float TRate = (rate/(1+GSTRate/100));
            TR = String.format("%.2f", TRate);
            // float GSTRate = Float.valueOf(gsgstrate + gsgstrate);
            float PA = TA / (1 + (GSTRate / 100));
            String sttr = String.format("%.2f", PA);
            idtaxableamt.setText(sttr);
            float CGSTAmt = PA * Float.valueOf(gcgstrate) / 100;
            String cgst = String.format("%.2f", CGSTAmt);
            // Calculate SGST Amount
            float SGSTAmt =  PA * Float.valueOf(gsgstrate) / 100;
            String sgst = String.format("%.2f", SGSTAmt);
            gcgstamount = cgst;
            gsgstamount = sgst;
            float igstamt = CGSTAmt + SGSTAmt;
            String st = String.format("%.2f", igstamt);
            idgstamt.setText(st);
        } else {


            GSTRate = Float.valueOf(gigstrate);
            String gstrate = String.valueOf(GSTRate);
            idgstrate.setText(gstrate);
            float quantity = Float.valueOf(idquantity.getText().toString());
            float rate = Float.valueOf(idrate.getText().toString());

            float TA = (quantity) * (rate);
            String stt = String.format("%.2f", TA);
            idtotalamt.setText(stt);
            float TRate = (rate/(1+GSTRate/100));
            TR = String.format("%.2f", TRate);
            // float GSTRate = Float.valueOf(gsgstrate + gsgstrate);
            float PA = TA / (1 + ((GSTRate) / 100));
            String sttr = String.format("%.2f", PA);
            idtaxableamt.setText(sttr);
            float IGSTAmt = TA - PA;
            String ist = String.format("%.2f", IGSTAmt);
            idgstamt.setText(ist);
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
        idquantity.addTextChangedListener(new EditTextListener());
        // progressActivity.showLoading();

        //loading

    }


    @Override
    public void onClick(View view) {
        qrScan.initiateScan();
    }

    private class EditTextListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String strquantity = idquantity.getText().toString();

            if (!strquantity.isEmpty()) {

                if (RetailOrMrp.equals("MRP")) {
                    idrate.setText(productList.get(0).getMrpRate());
                } else {
                    idrate.setText(productList.get(0).getRetailRate());
                }
                float GSTRate;
                if (cstate.equals("17")) {


                    GSTRate = Float.valueOf(gsgstrate)+ Float.valueOf(gcgstrate);
                    String gstrate = String.valueOf(GSTRate);
                    idgstrate.setText(gstrate);
                    float quantity = Float.valueOf(idquantity.getText().toString());
                    float rate = Float.valueOf(idrate.getText().toString());

                    float TA = (quantity) * (rate);
                    String stt = String.format("%.2f", TA);
                    idtotalamt.setText(stt);
                    float TRate = (rate/(1+GSTRate/100));
                    TR = String.format("%.2f", TRate);
                    // float GSTRate = Float.valueOf(gsgstrate + gsgstrate);
                    float PA = TA / (1 + (GSTRate / 100));
                    String sttr = String.format("%.2f", PA);
                    idtaxableamt.setText(sttr);
                    float CGSTAmt = PA * Float.valueOf(gcgstrate) / 100;
                    String cgst = String.format("%.2f", CGSTAmt);
                    // Calculate SGST Amount
                    float SGSTAmt =  PA * Float.valueOf(gsgstrate) / 100;
                    String sgst = String.format("%.2f", SGSTAmt);
                    gcgstamount = cgst;
                    gsgstamount = sgst;
                    float igstamt = CGSTAmt + SGSTAmt;
                    String st = String.format("%.2f", igstamt);
                    idgstamt.setText(st);
                } else {

                    idgstrate.setText(productList.get(0).getIgstRate());
                    GSTRate = Float.valueOf(gigstrate);
                    float quantity = Float.valueOf(idquantity.getText().toString());
                    float rate = Float.valueOf(idrate.getText().toString());

                    float TA = (quantity) * (rate);
                    String stt = String.format("%.2f", TA);
                    idtotalamt.setText(stt);
                    float TRate = (rate/(1+GSTRate/100));
                    TR = String.format("%.2f", TRate);
                    // float GSTRate = Float.valueOf(gsgstrate + gsgstrate);
                    float PA = TA / (1 + ((GSTRate) / 100));
                    String sttr = String.format("%.2f", PA);
                    idtaxableamt.setText(sttr);
                    float IGSTAmt =  PA * Float.valueOf(gigstrate) / 100;
                    String ist = String.format("%.2f", IGSTAmt);
                    idgstamt.setText(ist);
                }
            } else {
                strquantity = "0";
                idtotalamt.setText(strquantity);


            }
        }

        @Override
        public void afterTextChanged(Editable s) {


        }

    }



    private void loadSpinnerData() {
        List<String> lables = db.getAllLabels("ProductCompany");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, lables);
        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        idprComapny.setAdapter(dataAdapter);
    }

    private void loadSpinnerCategory(String compid) {
        List<String> lables = db.getCategoryOnCompId(compid,"ProductCategory");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, lables);
        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        idprCAtegory.setAdapter(dataAdapter);
        int stid = Integer.parseInt(pr_categoryid);

        String label = db.getProduct(stid ,"ProductCategory");

        // = String.valueOf(dataAdapter.getPosition("Hot Channa Red Chilli"));
        int selectionPosition= dataAdapter.getPosition(label);
        idprCAtegory.setSelection(selectionPosition);
    }




    private void loadSpinnerVariant(String pid,String tripid) {
        List<String> lables = db.getVariantFilter("Variant", pid,tripid);
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, lables);
        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        idprVariant.setAdapter(dataAdapter);
        int stid = Integer.parseInt(pr_variantid);

        String label = db.getProduct(stid ,"Variant");

        // = String.valueOf(dataAdapter.getPosition("Hot Channa Red Chilli"));
        int selectionPosition= dataAdapter.getPosition(label);
        idprVariant.setSelection(selectionPosition);
    }

    class ProductCompanySelect implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            String label = parent.getItemAtPosition(position).toString();
            int sid = db.getId(label, "ProductCompany");
            pr_companyid = String.valueOf(sid);
            loadSpinnerCategory(pr_companyid);
            // Toast.makeText(v.getContext(), "Your choose :" + pr_companyid,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    class CategorySelect implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            String label = parent.getItemAtPosition(position).toString();
            int sid = db.getIdProduct(label, "ProductCategory");
            pr_categoryid = String.valueOf(sid);
         loadProductType(pr_categoryid,tripid);
            // Toast.makeText(v.getContext(), "Your choose :" + pr_companyid,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    private void loadProductType(String pr_categoryid,String tripid) {
        List<String> lables = db.getBindLabels("ProductType", pr_categoryid,tripid);
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, lables);
        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        idprType.setAdapter(dataAdapter);
        int stid = Integer.parseInt(pr_typeid);

        String label = db.getProduct(stid ,"ProductType");

        // = String.valueOf(dataAdapter.getPosition("Hot Channa Red Chilli"));
        int selectionPosition= dataAdapter.getPosition(label);
        idprType.setSelection(selectionPosition);

    }

    class ProductTypeSelct implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            String label = parent.getItemAtPosition(position).toString();
            int sid = db.getIdProduct(label, "ProductType");
            pr_typeid = String.valueOf(sid);
          loadSpinnerVariant(pr_typeid,tripid);


        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }


    class ProductVariantSelect implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            String label = parent.getItemAtPosition(position).toString();
            int sid = db.getIdProduct(label, "Variant");
            pr_variantid = String.valueOf(sid);

          calculate();
            //  Toast.makeText(v.getContext(), "Your choose :" + pr_companyid,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    public void calculate() {
        productList = db.getProductDetails(pr_companyid, pr_typeid, pr_variantid);
        if (productList.isEmpty()) {
            new MaterialDialog.Builder(this.getActivity())
                    .iconRes(R.drawable.iconhems)
                    .limitIconToDefaultSize()
                    .title("Hems Sales")
                    .content("Please Select Valid ProductType and Variant!")
                    .positiveText("OK")

                    .show();
        } else {
            pid = productList.get(0).getId();
            InStock = db.getInstock(pid,tripid);
            soldquant = db.getSoldQuant(pid,tripid);
            if(InStock.equals("")) {
                InStock = String.valueOf(0);
                idinstock.setText("In Stock : " + InStock);
            }else {
                idinstock.setText("In Stock : " + InStock);
            }
            gsgstrate = productList.get(0).getSgstRate();
            gcgstrate = productList.get(0).getCgstRate();
            ghsncode = productList.get(0).getHsncode();
            gmrp = productList.get(0).getMrpRate();
            gretail = productList.get(0).getRetailRate();
            gigstrate = productList.get(0).getIgstRate();

            String gmrp = productList.get(0).getMrpRate();
            productcode = productList.get(0).getProductcode();

            idhsncode.setText(productList.get(0).getHsncode());
            float GSTRate;
            if (RetailOrMrp.equals("MRP")) {
                idrate.setText(gmrp);
            } else {
                idrate.setText(gretail);
            }
            if (cstate.equals("17")) {


                GSTRate = Float.valueOf(gsgstrate)+ Float.valueOf(gcgstrate);
                String gstrate = String.valueOf(GSTRate);
                idgstrate.setText(gstrate);
                float quantity = Float.valueOf(idquantity.getText().toString());
                float rate = Float.valueOf(idrate.getText().toString());

                float TA = (quantity) * (rate);
                String stt = String.format("%.2f", TA);
                idtotalamt.setText(stt);
                float TRate = (rate/(1+GSTRate/100));
                TR = String.format("%.2f", TRate);
                // float GSTRate = Float.valueOf(gsgstrate + gsgstrate);
                float PA = TA / (1 + (GSTRate / 100));
                String sttr = String.format("%.2f", PA);
                idtaxableamt.setText(sttr);
                float CGSTAmt = PA * Float.valueOf(gcgstrate) / 100;
                String cgst = String.format("%.2f", CGSTAmt);
                // Calculate SGST Amount
                float SGSTAmt =  PA * Float.valueOf(gsgstrate) / 100;
                String sgst = String.format("%.2f", SGSTAmt);
                gcgstamount = cgst;
                gsgstamount = sgst;
                float igstamt = CGSTAmt + SGSTAmt;
                String st = String.format("%.2f", igstamt);
                idgstamt.setText(st);
            } else {

                idgstrate.setText(productList.get(0).getIgstRate());
                GSTRate = Float.valueOf(gigstrate);
                float quantity = Float.valueOf(idquantity.getText().toString());
                float rate = Float.valueOf(idrate.getText().toString());

                float TA = (quantity) * (rate);
                String stt = String.format("%.2f", TA);
                idtotalamt.setText(stt);
                float TRate = (rate/(1+GSTRate/100));
                TR = String.format("%.2f", TRate);
                // float GSTRate = Float.valueOf(gsgstrate + gsgstrate);
                float PA = TA / (1 + ((GSTRate) / 100));
                String sttr = String.format("%.2f", PA);
                idtaxableamt.setText(sttr);
                float IGSTAmt =  PA * Float.valueOf(gigstrate) / 100;
                String ist = String.format("%.2f", IGSTAmt);
                idgstamt.setText(ist);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    @OnClick(R.id.btn_save)
    public void onclicksave() {
        int count = db.checkRecordForEdit(pr_typeid, pr_variantid, id,invid);
        if (count > 0) {
            new MaterialDialog.Builder(this.getActivity())
                    .iconRes(R.drawable.iconhems)
                    .limitIconToDefaultSize()
                    .title("Hems Sales")
                    .content("Invoice Details Already Exist")
                    .positiveText("OK")

                    .show();
        } else {
            mSecurePrefs.edit().putString(SharedPrefKey.PAY_AMOUNT, idtotalamt.getText().toString()).apply();
            mSecurePrefs.edit().putString(SharedPrefKey.INVOICEID_TO_PRINT, id).apply();
            db.updateInvoiceDetails(id, productList.get(0).getId(), ghsncode, productcode, idquantity.getText().toString(), idrate.getText().toString(), idtotalamt.getText().toString(), gcgstrate, gcgstamount, gsgstrate, gsgstamount, gigstrate, gigstamt, active, productList.get(0).getPtypename(), pr_typeid, productList.get(0).getPvaraint(), pr_variantid,TR,idtaxableamt.getText().toString());

            progressActivity.showEmpty(succDrawable,
                    "Invoice Details Edited Successfully",
                    "");
            float instock = Float.parseFloat(InStock);
            float newqty = Float.parseFloat(idquantity.getText().toString());

            float sqty = (Float.parseFloat(soldquant) - Float.parseFloat(oldquantity)) + newqty;
            String soldq = String.valueOf(sqty);
            db.updateTripProduct(pid, tripid, soldq);
            EventBus.getDefault().post(new EventADForm());
            // mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            EventBus.getDefault().post(new EventRefresh());

            imm.hideSoftInputFromWindow(idccity.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
        toolbar.setTitle("Edit Invoice Details");

        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                getDialog().dismiss();
                EventBus.getDefault().post(new EventADForm());
            }
        });
        ImageButton print = (ImageButton) toolbar.findViewById(R.id.print);

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
                Intent intent = new Intent(getActivity(), BloothPrinterActivity.class);
                startActivity(intent);
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
        EventBus.getDefault().unregister(this);
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

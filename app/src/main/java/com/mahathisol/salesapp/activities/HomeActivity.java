package com.mahathisol.salesapp.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.github.fabtransitionactivity.SheetLayout;
import com.google.gson.reflect.TypeToken;
import com.mahathisol.salesapp.R;
import com.mahathisol.salesapp.app.AppController;
import com.mahathisol.salesapp.fragments.AddCustomerFormFragment;
import com.mahathisol.salesapp.fragments.AddDieselFillingFragment;
import com.mahathisol.salesapp.fragments.DieselFragment;
import com.mahathisol.salesapp.fragments.DisplayCustomerFragment;
import com.mahathisol.salesapp.fragments.InvoiceHomeFragment;
import com.mahathisol.salesapp.fragments.StartTripFormFragment;
import com.mahathisol.salesapp.fragments.TripHomeFragment;
import com.mahathisol.salesapp.pojos.BindStatus;
import com.mahathisol.salesapp.pojos.CreateCustomer;
import com.mahathisol.salesapp.pojos.EventADForm;
import com.mahathisol.salesapp.pojos.EventRefresh;
import com.mahathisol.salesapp.pojos.InsertInvoice;
import com.mahathisol.salesapp.pojos.NewInsertCust;
import com.mahathisol.salesapp.pojos.UploadCompany;
import com.mahathisol.salesapp.pojos.UploadCustomer;
import com.mahathisol.salesapp.pojos.UploadPaymentMode;
import com.mahathisol.salesapp.pojos.UploadProduct;
import com.mahathisol.salesapp.pojos.UploadProductCategory;
import com.mahathisol.salesapp.pojos.UploadProductCompany;
import com.mahathisol.salesapp.pojos.UploadProductType;
import com.mahathisol.salesapp.pojos.UploadTripProduct;
import com.mahathisol.salesapp.pojos.UploadVaraint;
import com.mahathisol.salesapp.utils.ApiEndPoint;
import com.mahathisol.salesapp.utils.NetworkStateChecker;
import com.mahathisol.salesapp.utils.SharedPrefKey;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity implements SheetLayout.OnFabAnimationEndListener {
    public static final String TAG = HomeActivity.class.getSimpleName();
    private DatabaseHelper myDb;
    UploadVaraint.Variantlist addr;
    UploadProductType.Prtypelist prlst;
    UploadCustomer.Custlist culist;
    UploadProductCategory.Prcat prcat;
    UploadProduct.Prlist product;
    UploadProductCompany.Prcomplist prcomp;
    UploadCompany.Comapnyli comp;
    UploadPaymentMode.Paymentmodeli pay;
    UploadTripProduct.TripProductlist tripproduct;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.contentContainer)
    FrameLayout contentContainer;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.bottom_sheet)
    SheetLayout mSheetLayout;
    @BindView(R.id.bottomBar)
    BottomBar bottomBar;
    // CustomerSync.Customeradd synclist;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final int REQUEST_SIGNUP = 0;
    private ProgressDialog progressDialog;
    private static final int REQUEST_CODE = 1;
    String custregid = "";
    private SharedPreferences mSecurePrefs;
    int id = 0;
    String customer_id = "";
    String invctr = "";
    String name = "";
    private int casetype = 0;
    int token = 0;
    ProgressDialog progressdialog;
    Handler handler = new Handler();
    int status = 0;
    String tripstatus = "";
    String tripno = "";
    String totalvalue= "";
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";
    private BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
//        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        myDb = new DatabaseHelper(this);
        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        mSecurePrefs = getSharedPref();
        customer_id = mSecurePrefs.getString(SharedPrefKey.CUSTOMER_ID, "");
        name = mSecurePrefs.getString(SharedPrefKey.CUSTOMER_NAME, "");
        System.out.println("CUSTMORE_ID" + customer_id);
        bindStatus(customer_id);
        setupToolbar();
        //    bindCustomersFromSql();
        EventBus.getDefault().register(this);

        this.setSupportActionBar(toolbar);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //loading the names again
                //loadNames();
            }
        };

        //registering the broadcast receiver to update sync status
        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));
        getSupportActionBar().setElevation(0);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {


            @Override
            public void onTabSelected(@IdRes int tabId) {
                //messageView.setText(TabMessage.get(tabId, false));
                switch (tabId) {
                    case R.id.tab_invoice:

                        casetype = 0;
                        invoicehome();
                        mFab.hide();

                        break;
                    case R.id.tab_trip:
                        casetype = 1;
                        mFab.hide();
                        tripehome();
                        // addLangFragment();

                        break;

                    case R.id.tab_customer:
                        casetype = 2;
                        addCustomerFragment();
                        //  new DisplayCustomerFragment().show(getSupportFragmentManager(), DisplayCustomerFragment.TAG);
                        mFab.show();

                        //  addLangFragment();
                        // addTimelineFragment();
//                        Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.tab_DieselFilling:
                        casetype = 3;
                        mFab.show();
                        ShowDiesel();
                        //openRowing();
                        break;


                }
            }
        });

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                //Toast.makeText(getApplicationContext(), TabMessage.get(tabId, true), Toast.LENGTH_LONG).show();
            }
        });

        mSheetLayout.setFab(mFab);
        mSheetLayout.setFabAnimationEndListener(this);
    }

    public void bindStatus(String cust_id) {

        AndroidNetworking.get(ApiEndPoint.BASE_URL + ApiEndPoint.BIND_STATUS)
                // AndroidNetworking.get("http://www.json-generator.com/api/json/get/ceMXczcMpu?indent=2")
                .addQueryParameter("sid", cust_id)
                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()

                .getAsOkHttpResponseAndParsed(new TypeToken<BindStatus>() {
                }, new OkHttpResponseAndParsedRequestListener<BindStatus>() {
                    @Override
                    public void onResponse(Response okHttpResponse, BindStatus adel) {
                        if (!((adel == null) || adel.getStatus().isEmpty())) {
                            // Log.d("failure", user.getBlogList().toString());
                            tripstatus = adel.getStatus();
                            tripno = adel.getTripno();
                            totalvalue = adel.getTotalValue();
                         //   mSecurePrefs.edit().putString(SharedPrefKey.TRIP_STATUS, tripstatus).apply();
                           // mSecurePrefs.edit().putString(SharedPrefKey.TRIP_NO, tripno).apply();
                          //  mSecurePrefs.edit().putString(SharedPrefKey.TOT_VAL, totalvalue).apply();
                   if(tripstatus.equals("1")){
                   new StartTripFormFragment().show(getSupportFragmentManager(), StartTripFormFragment.TAG);
                     }


                        } else {

                        }
                        //

                    }

                    @Override
                    public void onError(ANError anError) {


                    }
                });


    }
    public void addCustomerFragment() {
        FragmentManager fragMan = getSupportFragmentManager();

        FragmentTransaction fragTrans = fragMan.beginTransaction();
        //AssignDelivery fragB = new AssignDelivery();
        DisplayCustomerFragment fragcustomer = DisplayCustomerFragment.newInstance(token);
        //fragB.n
        fragTrans.replace(R.id.contentContainer, fragcustomer);
        //getSupportActionBar().setTitle("Timeline");
        //toolbar.setTitle("Timeline");
        //childFragTrans.addToBackStack("B");
        fragTrans.commit();

    }

    public void invoicehome() {
        FragmentManager fragMan = getSupportFragmentManager();

        FragmentTransaction fragTrans = fragMan.beginTransaction();
        //AssignDelivery fragB = new AssignDelivery();
        InvoiceHomeFragment fragin = InvoiceHomeFragment.newInstance(token);
        //fragB.n
        fragTrans.replace(R.id.contentContainer, fragin);
        //getSupportActionBar().setTitle("Timeline");
        //toolbar.setTitle("Timeline");
        //childFragTrans.addToBackStack("B");
        fragTrans.commit();

    }

    public void tripehome() {
        FragmentManager fragMan = getSupportFragmentManager();

        FragmentTransaction fragTrans = fragMan.beginTransaction();
        //AssignDelivery fragB = new AssignDelivery();
        TripHomeFragment fragtrip = TripHomeFragment.newInstance(token);
        //fragB.n
        fragTrans.replace(R.id.contentContainer, fragtrip);
        //getSupportActionBar().setTitle("Timeline");
        //toolbar.setTitle("Timeline");
        //childFragTrans.addToBackStack("B");
        fragTrans.commit();

    }

    public void ShowDiesel() {
        FragmentManager fragMan = getSupportFragmentManager();

        FragmentTransaction fragTrans = fragMan.beginTransaction();
        //AssignDelivery fragB = new AssignDelivery();
        DieselFragment fragdiesel = DieselFragment.newInstance(token);
        //fragB.n
        fragTrans.replace(R.id.contentContainer, fragdiesel);
        //getSupportActionBar().setTitle("Timeline");
        //toolbar.setTitle("Timeline");
        //childFragTrans.addToBackStack("B");
        fragTrans.commit();
    }

    private SharedPreferences getSharedPref() {
        if (mSecurePrefs == null) {
            mSecurePrefs = AppController.getInstance().getSharedPreferences();
        }
        return mSecurePrefs;
    }

    @Override
    protected void onPause() {
        // LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @OnClick(R.id.fab)
    void onFabClick() {


        mSheetLayout.expandFab();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addCarLocation(EventADForm loc) {

        mSheetLayout.contractFab();
        // markerForCar(loc.getLatLngselect());
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    public void setTab(int position) {
        if (bottomBar != null) {
            bottomBar.selectTabAtPosition(position);
        }
    }

    @Override
    public void onFabAnimationEnd() {
//        Intent intent = new Intent(this, RowingDigitalActivity.class);
//        startActivityForResult(intent, REQUEST_CODE);

//        Intent intent = new Intent(this, RowingActivity.class);
//        startActivityForResult(intent, REQUEST_CODE);
//        overridePendingTransition(R.anim.fadein, R.anim.fadeout);


        switch (casetype) {
            case 0:
                //  new AddCustomerFormFragment().show(getSupportFragmentManager(), AddCustomerFormFragment.TAG);


                break;


            case 2:
                new AddCustomerFormFragment().show(getSupportFragmentManager(), AddCustomerFormFragment.TAG);
                // addLangFragment();

                break;

            case 3:

                new AddDieselFillingFragment().show(getSupportFragmentManager(), AddDieselFillingFragment.TAG);
                break;

            case 4:
                //  new AddRouteFormFragment().show(getSupportFragmentManager(), AddRouteFormFragment.TAG);
                break;

            case 5:
                //   new AddCityFormFragment().show(getSupportFragmentManager(), AddCityFormFragment.TAG);
                break;

            case 6:
                //  new AddRateFormFragment().show(getSupportFragmentManager(), AddRateFormFragment.TAG);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            // mSheetLayout.contractFab();
            bottomBar.selectTabAtPosition(0);

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //  EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs

        mSheetLayout.contractFab();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return true;


//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
//        searchView.setQueryHint(this.getString(R.string.search));
//
//        ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text))
//                .setHintTextColor(getResources().getColor(R.color.nliveo_white));
//        searchView.setOnQueryTextListener(onQuerySearchView);
//
//		menu.findItem(R.id.menu_add).setVisible(true);
//		menu.findItem(R.id.menu_search).setVisible(true);

        //mSearchCheck = false;
    }

    //
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        switch (item.getItemId()) {

            case R.id.action_settings:

                mSecurePrefs.edit().putString(SharedPrefKey.CUSTOMER_ID, "").apply();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.sync:
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

                //if there is a network
                if (activeNetwork != null) {
                    //if connected to wifi or mobile data plan
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                        CreateProgressDialog();
                        downloadCustomer();
                        downloadVariant();
                        downloadProductType();
                        downloadProductCompany();
                        downloadProduct();
                        downloadProductCategory();
                        downloadCompany();
                       downloadPaymentMode();

                        //downloadTripProduct();
                        ShowProgressDialog();
//                        new MaterialDialog.Builder(this)
//                                .iconRes(R.drawable.iconhems)
//                                .limitIconToDefaultSize()
//                                .title("Hems Sales")
//                                .content("All Details Uploaded Successfully")
//                                .positiveText("OK")
//
//                                .show();

                    }
                } else {

                    new MaterialDialog.Builder(this)
                            .iconRes(R.drawable.iconhems)
                            .limitIconToDefaultSize()
                            .title("Hems Sales")
                            .content("No Internet Connection")
                            .positiveText("OK")

                            .show();
                }

                break;
            case R.id.upload:

                ConnectivityManager cmm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworks = cmm.getActiveNetworkInfo();

                //if there is a network
                if (activeNetworks != null) {
                    //if connected to wifi or mobile data plan
                    if (activeNetworks.getType() == ConnectivityManager.TYPE_WIFI || activeNetworks.getType() == ConnectivityManager.TYPE_MOBILE) {

                        CreateProgressDialogUpload();

                      uploadCustomer();
                       uploadTripProduct();
                        UpdateInvoiceCtr(mSecurePrefs.getString(SharedPrefKey.INVOICE_CTR, ""),customer_id);
                      uploadInvoice();
                      //  ShowProgressDialog();
//                        new MaterialDialog.Builder(this)
//                                .iconRes(R.drawable.iconhems)
//                                .limitIconToDefaultSize()
//                                .title("Hems Sales")
//                                .content("All Details Uploaded Successfully")
//                                .positiveText("OK")
//
//                                .show();

                    }
                } else {

                    new MaterialDialog.Builder(this)
                            .iconRes(R.drawable.iconhems)
                            .limitIconToDefaultSize()
                            .title("Hems Sales")
                            .content("No Internet Connection")
                            .positiveText("OK")

                            .show();
                }
                break;
//            case R.id.change:
//
////                Intent intent2 = new Intent(this, ChangePassword.class);
////               this.startActivityForResult(intent2, REQUEST_SIGNUP);
//                break;
        }
        return true;
    }
    public void CreateProgressDialog()
    {

        progressdialog = new ProgressDialog(HomeActivity.this);
        progressdialog.setMessage("Downloading....");
        progressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressdialog.setMax(100);
        progressdialog.setIndeterminate(false);
        progressdialog.setCancelable(true);
        progressdialog.show();

    }
    public void CreateProgressDialogUpload()
    {
        progressdialog = new ProgressDialog(HomeActivity.this);
        progressdialog.setMessage("Uploading....");
        progressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressdialog.setMax(100);
        progressdialog.setIndeterminate(false);
        progressdialog.setCancelable(true);
        progressdialog.show();


    }

    public void ShowProgressDialog()
    {
        status = 0;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(status < 100){

                    status +=1;

                    try{
                        Thread.sleep(200);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            progressdialog.setProgress(status);

                            if(status == 100){

                                progressdialog.dismiss();

                            }
                        }
                    });
                }
            }
        }).start();

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
 public void  uploadTripProduct() {

    Cursor cursor = myDb.getUnsyncedTripProduct();
    if (cursor.moveToFirst()) {
        do {
            //calling the method to save the unsynced name to MySQL
            saveTripProduct(
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
    public void saveTripProduct(final String tid,final String tripid, final String productid, final String openingstock, final String inwardqty, final String closingstock, final String loaddate, final String returnqty,final String soldquant) {

        AndroidNetworking.post(ApiEndPoint.BASE_URL + ApiEndPoint.DOWNLOAD_FROM_SQLITE)

                .addQueryParameter("id", tid)

//                .addUrlEncodeFormBodyParameter("txttripId", tripid)
//
//                .addUrlEncodeFormBodyParameter("txtproductId", productid)
//
//                .addUrlEncodeFormBodyParameter("txtopeningStock", openingstock)
//                .addUrlEncodeFormBodyParameter("txtinwardQty", inwardqty)
//                .addUrlEncodeFormBodyParameter("txtclosingStock", closingstock)
//                //.addUrlEncodeFormBodyParameter("txtloaddate", loaddate)
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


    public void uploadCustomer(){

        Cursor cursor = myDb.getUnsyncedNames();
        if (cursor.moveToFirst()) {
            do {
                //calling the method to save the unsynced name to MySQL
                saveName(
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_MOBILE)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ADD1)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ADD2)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ADD3)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PIN)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CITY)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_SID)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_GST)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ACTIVE)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_RETAILMRP))
                );
            } while (cursor.moveToNext());
        }
    }


    public void saveName(final int oldid,final String txtname, final String txtphoneNo, final String txtemailId, final String txtaddress1, final String txtaddress2, final String txtaddress3, final String txtpin, final String txtcity, final String txtstateid, final String txtgstinno, final String txtactive,final String retailmrp) {

        AndroidNetworking.post(ApiEndPoint.BASE_URL + ApiEndPoint.CREATE_CUSTOMER)

                .addUrlEncodeFormBodyParameter("txtname", txtname)

                .addUrlEncodeFormBodyParameter("txtmobile", txtphoneNo)

                .addUrlEncodeFormBodyParameter("txtemailid", txtemailId)

                .addUrlEncodeFormBodyParameter("txtaddress1", txtaddress1)
                .addUrlEncodeFormBodyParameter("txtaddress2", txtaddress2)
                .addUrlEncodeFormBodyParameter("txtaddress3", txtaddress3)
                .addUrlEncodeFormBodyParameter("txtcity", txtcity)
                .addUrlEncodeFormBodyParameter("txtpincode", txtpin)


                .addUrlEncodeFormBodyParameter("txtstateid", txtstateid)
                .addUrlEncodeFormBodyParameter("txtgstinno", txtgstinno)
                .addUrlEncodeFormBodyParameter("txtactive", txtactive)
                .addUrlEncodeFormBodyParameter("txtretailOrMRP", retailmrp)

                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()

                .getAsOkHttpResponseAndParsed(new TypeToken<NewInsertCust>() {
                }, new OkHttpResponseAndParsedRequestListener<NewInsertCust>() {
                    @Override
                    public void onResponse(Response okHttpResponse, NewInsertCust cal) {


                        if(cal.getValue().isEmpty()){

                            Log.d("failure", String.valueOf(id));
                          //  myDb.updateNameStatus(id, AddCustomerFormFragment.NAME_SYNCED_WITH_SERVER);

                           // context.sendBroadcast(new Intent(DisplayCustomerFragment.DATA_SAVED_BROADCAST));
                        } else {
                            //  saveNameToLocalStorage(txtname,txtphoneNo,txtemailId,txtaddress1,txtaddress2,txtaddress3,txtpin,txtcity,txtstateid,txtgstinno,txtactive,NAME_SYNCED_WITH_SERVER);

//
                            myDb.updateNameStatus(oldid, AddCustomerFormFragment.NAME_SYNCED_WITH_SERVER);
                            //sending the broadcast to refresh the list
                         //   context.sendBroadcast(new Intent(DisplayCustomerFragment.DATA_SAVED_BROADCAST));
                            String id = cal.getValue().get(0).getId();
                            String name= cal.getValue().get(0).getName();
                            String mobile= cal.getValue().get(0).getMobile();
                            myDb.updateCustomerId(id, oldid);
                            int maxcid = myDb.getMAXid("Customer");

                            int count = myDb.getCount("Counters");
                            if(count > 0){
                                myDb.updateCounter(maxcid+1,"customerId","Counters");
                            }else {
                                myDb.insertCustomerIdCOunter(maxcid + 1,"customerId");

                            }

                            EventBus.getDefault().post(new EventADForm());
                            // mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            EventBus.getDefault().post(new EventRefresh());
                            new CountDownTimer(1000, 1000) {
                                public void onFinish() {
                                    // When timer is finished



                                    // Execute your code here
                                }

                                //
                                public void onTick(long millisUntilFinished) {


                                }
                            }.start();


                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error


                    }
                });


    }


 public void  uploadInvoice() {

        Cursor cursor = myDb.getAllInvoice();
        if (cursor.moveToFirst()) {
            do {
                //calling the method to save the unsynced name to MySQL
                saveToInvoice(
                       cursor.getString(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("invoiceNo")),
                        cursor.getString(cursor.getColumnIndex("invoiceDate")),
                        cursor.getString(cursor.getColumnIndex("customerId")),
                        cursor.getString(cursor.getColumnIndex("cuName")),
                        cursor.getString(cursor.getColumnIndex("cuMobile")),
                        cursor.getString(cursor.getColumnIndex("cuEmailid")),
                        cursor.getString(cursor.getColumnIndex("cuAddress1")),
                        cursor.getString(cursor.getColumnIndex("cuAddress2")),
                        cursor.getString(cursor.getColumnIndex("cuAddress3")),
                        cursor.getString(cursor.getColumnIndex("cuCity")),
                        cursor.getString(cursor.getColumnIndex("cuStaeid")),
                        cursor.getString(cursor.getColumnIndex("cuGSTINNO")),
                        cursor.getString(cursor.getColumnIndex("companyId")),
                        cursor.getString(cursor.getColumnIndex("cName")),
                        cursor.getString(cursor.getColumnIndex("cMobile")),
                        cursor.getString(cursor.getColumnIndex("cEmailid")),
                        cursor.getString(cursor.getColumnIndex("cAddress1")),
                        cursor.getString(cursor.getColumnIndex("cAddress2")),
                        cursor.getString(cursor.getColumnIndex("cAddress3")),
                        cursor.getString(cursor.getColumnIndex("ccity")),
                        cursor.getString(cursor.getColumnIndex("cpincode")),
                        cursor.getString(cursor.getColumnIndex("cstateid")),
                        cursor.getString(cursor.getColumnIndex("cGSTINNo")),

                        cursor.getString(cursor.getColumnIndex("paymentmodeid")),
                        cursor.getString(cursor.getColumnIndex("amountPaid")),
                        cursor.getString(cursor.getColumnIndex("instrNum")),
                        cursor.getString(cursor.getColumnIndex("instrDate")),
                        cursor.getString(cursor.getColumnIndex("remarks"))

                       );
            } while (cursor.moveToNext());
        }
    }
    public void saveToInvoice(final String oldid,final String invoiceNo, final String invoiceDate,final String customerId,final String cuName,final String cuMobile,final String cuEmailid,final String cuAddress1,final String cuAddress2,final String cuAddress3,final String cuCity,final String cuStaeid,final String cuGSTINNO,final String companyId,final String cName,final String cMobile,final String cEmailid,final String cAddress1,final String cAddress2,final String cAddress3,final String ccity,final String cpincode,final String cstateid,final String cGSTINNo,final String paymid,final String amtpaid,final String instrnum,final String instrdate,final String remarks) {

        AndroidNetworking.post(ApiEndPoint.BASE_URL + ApiEndPoint.INSERT_INVOICE)


                .addUrlEncodeFormBodyParameter("txtinvoiceNo", invoiceNo)

                .addUrlEncodeFormBodyParameter("txtinvoiceDate", invoiceDate)

                .addUrlEncodeFormBodyParameter("txtcustomerId", customerId)
                .addUrlEncodeFormBodyParameter("txtcuName", cuName)
                .addUrlEncodeFormBodyParameter("txtcuMobile", cuMobile)
                .addUrlEncodeFormBodyParameter("txtcuEmailid", cuEmailid)
               .addUrlEncodeFormBodyParameter("txtcuAddress1", cuAddress1)
                .addUrlEncodeFormBodyParameter("txtcuAddress2", cuAddress2)
                .addUrlEncodeFormBodyParameter("txtcuAddress3", cuAddress3)
                .addUrlEncodeFormBodyParameter("txtcuCity", cuCity)
                .addUrlEncodeFormBodyParameter("txtcuStaeid", cuStaeid)
                .addUrlEncodeFormBodyParameter("txtcuGSTINNO", cuGSTINNO)
               .addUrlEncodeFormBodyParameter("txtcompanyId", companyId)
                .addUrlEncodeFormBodyParameter("txtcName", cName)
                .addUrlEncodeFormBodyParameter("txtcMobile", cMobile)
                .addUrlEncodeFormBodyParameter("txtcEmailid", cEmailid)
              .addUrlEncodeFormBodyParameter("txtcAddress1", cAddress1)
               .addUrlEncodeFormBodyParameter("txtcAddress2", cAddress2)
                .addUrlEncodeFormBodyParameter("txtcAddress3", cAddress3)
                .addUrlEncodeFormBodyParameter("txtccity", ccity)
                .addUrlEncodeFormBodyParameter("txtcpincode", cpincode)
                .addUrlEncodeFormBodyParameter("txtcstateid", cstateid)
                .addUrlEncodeFormBodyParameter("txtcGSTINNo", cGSTINNo)


                .addUrlEncodeFormBodyParameter("txtpaymentmodeid", paymid)
             .addUrlEncodeFormBodyParameter("txtamountPaid", amtpaid)
                .addUrlEncodeFormBodyParameter("txtinstrNum", instrnum)
              .addUrlEncodeFormBodyParameter("txtinstrDate", instrdate)
                .addUrlEncodeFormBodyParameter("txtremarks", remarks)
//                .addUrlEncodeFormBodyParameter("txtcTaxIncludedAmt", cTaxIncludedAmt)
//                .addUrlEncodeFormBodyParameter("txtreportFooter", reportFooter)
//                .addUrlEncodeFormBodyParameter("txtactive", active)
//                .addUrlEncodeFormBodyParameter("txtshowbankdetails", showbankdetails)
//                .addUrlEncodeFormBodyParameter("txtaccHolderName", accHolderName)
//                .addUrlEncodeFormBodyParameter("txtaccNo", accNo)
//                .addUrlEncodeFormBodyParameter("txtbankname", bankname)
//                .addUrlEncodeFormBodyParameter("txtbankbranch", bankbranch)
//                .addUrlEncodeFormBodyParameter("txtifsccode", ifsccode)
//                .addUrlEncodeFormBodyParameter("txtinHeader", inHeader)
                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()

                .getAsOkHttpResponseAndParsed(new TypeToken<InsertInvoice>() {
                }, new OkHttpResponseAndParsedRequestListener<InsertInvoice>() {
                    @Override
                    public void onResponse(Response okHttpResponse, InsertInvoice cal) {


                        if (cal.getValue().isEmpty()) {
                           // Toast.makeText(getApplicationContext(),"uploaded",Toast.LENGTH_LONG).show();

                        } else {
                            String newid = cal.getValue().get(0).getId();
                            String invoiceno= cal.getValue().get(0).getInvoiceNo();

                     boolean b =  myDb.updateInvoiceId(newid,oldid);

if(b == true){
ShowProgressDialog();
}

                        }


                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error


                    }
                });


    }




    public void downloadPaymentMode() {

        AndroidNetworking.get(ApiEndPoint.BASE_URL + ApiEndPoint.GET_PAYMENT)
                // AndroidNetworking.get("http://www.json-generator.com/api/json/get/ceMXczcMpu?indent=2")

                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()
                .getAsOkHttpResponseAndParsed(new TypeToken<UploadPaymentMode>() {
                }, new OkHttpResponseAndParsedRequestListener<UploadPaymentMode>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UploadPaymentMode adel) {

                        List<UploadPaymentMode.Paymentmodeli> custdatalist = adel.getPaymentmodeli();


                        if (!custdatalist.isEmpty()) {

                            int lengthData = adel.getPaymentmodeli().size();
                            for (int i = 0; i <= lengthData - 1; i++) {
                                pay = custdatalist.get(i);
                                myDb.saveToPaymentMode(pay.getId(), pay.getDescription(),pay.getActive());
                            }

                        } else {
//                            progress.showEmpty(emptyDrawable,
//                                    "Trip Product is empty",
//                                    "Please wait");
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        //progressActivity.showContent();

//                        progress.showError(errorDrawable, "No Connection",
//                                "We could not establish a connection with our servers. Try again when you are connected to the internet.",
//                                "Try Again", errorClickListener);

                        //Toast.makeText(getApplicationContext(), anError.getErrorDetail(), Toast.LENGTH_SHORT).show();

                    }
                });


    }
    public void downloadVariant() {

        AndroidNetworking.get(ApiEndPoint.BASE_URL + ApiEndPoint.GET_VARIANT)
                // AndroidNetworking.get("http://www.json-generator.com/api/json/get/ceMXczcMpu?indent=2")

                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()
                .getAsOkHttpResponseAndParsed(new TypeToken<UploadVaraint>() {
                }, new OkHttpResponseAndParsedRequestListener<UploadVaraint>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UploadVaraint adel) {

                        List<UploadVaraint.Variantlist> custdatalist = adel.getVariantlist();


                        if (!custdatalist.isEmpty()) {

                            int lengthData = adel.getVariantlist().size();
                            for (int i = 0; i <= lengthData - 1; i++) {
                                addr = custdatalist.get(i);
                                saveToVariant(addr.getId(), addr.getDescription());
                            }

                        } else {
//                            progress.showEmpty(emptyDrawable,
//                                    "Trip Product is empty",
//                                    "Please wait");
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        //progressActivity.showContent();

//                        progress.showError(errorDrawable, "No Connection",
//                                "We could not establish a connection with our servers. Try again when you are connected to the internet.",
//                                "Try Again", errorClickListener);

                        //Toast.makeText(getApplicationContext(), anError.getErrorDetail(), Toast.LENGTH_SHORT).show();

                    }
                });


    }

    public void downloadProductType() {

        AndroidNetworking.get(ApiEndPoint.BASE_URL + ApiEndPoint.GET_PRODUCT_TYPE)
                // AndroidNetworking.get("http://www.json-generator.com/api/json/get/ceMXczcMpu?indent=2")

                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()
                .getAsOkHttpResponseAndParsed(new TypeToken<UploadProductType>() {
                }, new OkHttpResponseAndParsedRequestListener<UploadProductType>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UploadProductType adel) {

                        List<UploadProductType.Prtypelist> prdatalist = adel.getPrtypelist();


                        if (!prdatalist.isEmpty()) {
                            int lengthData = adel.getPrtypelist().size();
                            for (int i = 0; i <= lengthData - 1; i++) {
                                prlst = prdatalist.get(i);
                                saveToProductType(prlst.getId(), prlst.getDescription(), prlst.getProdcategoryid());
                            }


                        } else {
//                            progress.showEmpty(emptyDrawable,
//                                    "Trip Product is empty",
//                                    "Please wait");
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        //progressActivity.showContent();

//                        progress.showError(errorDrawable, "No Connection",
//                                "We could not establish a connection with our servers. Try again when you are connected to the internet.",
//                                "Try Again", errorClickListener);

                        //Toast.makeText(getApplicationContext(), anError.getErrorDetail(), Toast.LENGTH_SHORT).show();

                    }
                });


    }

    public void downloadProductCompany() {

        AndroidNetworking.get(ApiEndPoint.BASE_URL + ApiEndPoint.GET_PRODUCT_COMPANY)
                // AndroidNetworking.get("http://www.json-generator.com/api/json/get/ceMXczcMpu?indent=2")

                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()
                .getAsOkHttpResponseAndParsed(new TypeToken<UploadProductCompany>() {
                }, new OkHttpResponseAndParsedRequestListener<UploadProductCompany>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UploadProductCompany adel) {

                        List<UploadProductCompany.Prcomplist> prdatalist = adel.getPrcomplist();


                        if (!prdatalist.isEmpty()) {
                            int lengthData = adel.getPrcomplist().size();
                            for (int i = 0; i <= lengthData - 1; i++) {
                                prcomp = prdatalist.get(i);
                                saveToProductCompany(prcomp.getId(), prcomp.getName());
                            }

                        } else {
//                            progress.showEmpty(emptyDrawable,
//                                    "Trip Product is empty",
//                                    "Please wait");
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        //progressActivity.showContent();

//                        progress.showError(errorDrawable, "No Connection",
//                                "We could not establish a connection with our servers. Try again when you are connected to the internet.",
//                                "Try Again", errorClickListener);

                        //Toast.makeText(getApplicationContext(), anError.getErrorDetail(), Toast.LENGTH_SHORT).show();

                    }
                });


    }

    public void downloadProduct() {

        AndroidNetworking.get(ApiEndPoint.BASE_URL + ApiEndPoint.GET_PRODUCT)
                // AndroidNetworking.get("http://www.json-generator.com/api/json/get/ceMXczcMpu?indent=2")

                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()
                .getAsOkHttpResponseAndParsed(new TypeToken<UploadProduct>() {
                }, new OkHttpResponseAndParsedRequestListener<UploadProduct>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UploadProduct adel) {

                        List<UploadProduct.Prlist> prdatalist = adel.getPrlist();


                        if (!prdatalist.isEmpty()) {
                            int lengthData = adel.getPrlist().size();
                            for (int i = 0; i <= lengthData - 1; i++) {
                                product = prdatalist.get(i);
                                saveToProduct(product.getId(), product.getProductcode(), product.getPrCompanyId(), product.getPrTypeId(), product.getPrVariantId(), product.getCgstRate(), product.getSgstRate(), product.getIgstRate(), product.getBarcodeno(), product.getHsncode(), product.getMrpRate(), product.getRetailRate());

                            }

                        } else {
//                            progress.showEmpty(emptyDrawable,
//                                    "Trip Product is empty",
//                                    "Please wait");
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        //progressActivity.showContent();

//                        progress.showError(errorDrawable, "No Connection",
//                                "We could not establish a connection with our servers. Try again when you are connected to the internet.",
//                                "Try Again", errorClickListener);

                        //Toast.makeText(getApplicationContext(), anError.getErrorDetail(), Toast.LENGTH_SHORT).show();

                    }
                });


    }

    public void downloadProductCategory() {

        AndroidNetworking.get(ApiEndPoint.BASE_URL + ApiEndPoint.GET_PRODUCT_CATEGORY)
                // AndroidNetworking.get("http://www.json-generator.com/api/json/get/ceMXczcMpu?indent=2")

                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()
                .getAsOkHttpResponseAndParsed(new TypeToken<UploadProductCategory>() {
                }, new OkHttpResponseAndParsedRequestListener<UploadProductCategory>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UploadProductCategory adel) {

                        List<UploadProductCategory.Prcat> prdatalist = adel.getPrcat();


                        if (!prdatalist.isEmpty()) {
                            int lengthData = adel.getPrcat().size();
                            for (int i = 0; i <= lengthData - 1; i++) {
                                prcat = prdatalist.get(i);
                                saveToProductCategory(prcat.getId(), prcat.getDescription());
                            }
                        } else {
//                            progress.showEmpty(emptyDrawable,
//                                    "Trip Product is empty",
//                                    "Please wait");
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        //progressActivity.showContent();

//                        progress.showError(errorDrawable, "No Connection",
//                                "We could not establish a connection with our servers. Try again when you are connected to the internet.",
//                                "Try Again", errorClickListener);

                        //Toast.makeText(getApplicationContext(), anError.getErrorDetail(), Toast.LENGTH_SHORT).show();

                    }
                });


    }

    public void downloadCompany() {

        AndroidNetworking.get(ApiEndPoint.BASE_URL + ApiEndPoint.GET_COMPANY)
                // AndroidNetworking.get("http://www.json-generator.com/api/json/get/ceMXczcMpu?indent=2")

                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()
                .getAsOkHttpResponseAndParsed(new TypeToken<UploadCompany>() {
                }, new OkHttpResponseAndParsedRequestListener<UploadCompany>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UploadCompany adel) {

                        List<UploadCompany.Comapnyli> prdatalist = adel.getComapnyli();


                        if (!prdatalist.isEmpty()) {
                            int lengthData = adel.getComapnyli().size();
                            for (int i = 0; i <= lengthData - 1; i++) {
                                comp = prdatalist.get(i);
                                saveToCompany(comp.getId(),comp.getName(),comp.getMobile(),comp.getEmailid(),comp.getAddress1(),
                                        comp.getAddress2(),comp.getAddress3(),comp.getCity(),comp.getStateid(),comp.getPincode(),comp.getGstinno(),
                                        comp.getInvoiceno(),comp.getTaxaccross(),comp.getCgst(),comp.getSgst(),comp.getIgst(),comp.getInvoicectr(),
                                        comp.getTaxincludedamt(),comp.getReportfooter(),comp.getShowbankdetails(),comp.getAccholdername(),
                                        comp.getAccno(),comp.getBankname(),comp.getBankbranch(),comp.getIfsccode(),comp.getInheaderwithtax(),
                                        comp.getInheaderwithouttax(),comp.getPassword(),comp.getLogincount(),comp.getTripnoAI(),comp.getTripnoCounter(),comp.getCompanylogo());
                            }

                        } else {
//                            progress.showEmpty(emptyDrawable,
//                                    "Trip Product is empty",
//                                    "Please wait");
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        //progressActivity.showContent();

//                        progress.showError(errorDrawable, "No Connection",
//                                "We could not establish a connection with our servers. Try again when you are connected to the internet.",
//                                "Try Again", errorClickListener);

                        //Toast.makeText(getApplicationContext(), anError.getErrorDetail(), Toast.LENGTH_SHORT).show();

                    }
                });


    }
//    public void downloadTripProduct() {
//
//        AndroidNetworking.get(ApiEndPoint.BASE_URL + ApiEndPoint.GET_TRIPPRODUCT)
//                // AndroidNetworking.get("http://www.json-generator.com/api/json/get/ceMXczcMpu?indent=2")
//
//                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
//                .setPriority(Priority.HIGH)
//                .build()
//                .getAsOkHttpResponseAndParsed(new TypeToken<UploadTripProduct>() {
//                }, new OkHttpResponseAndParsedRequestListener<UploadTripProduct>() {
//                    @Override
//                    public void onResponse(Response okHttpResponse, UploadTripProduct adel) {
//
//                        List<UploadTripProduct.TripProductlist> prdatalist = adel.getTripProductlist();
//
//
//                        if (!prdatalist.isEmpty()) {
//                            int lengthData = adel.getTripProductlist().size();
//                            for (int i = 0; i <= lengthData - 1; i++) {
//                                tripproduct = prdatalist.get(i);
////                                saveToCompany(comp.getId(),comp.getName(),comp.getMobile(),comp.getEmailid(),comp.getAddress1(),
////                                        comp.getAddress2(),comp.getAddress3(),comp.getCity(),comp.getStateid(),comp.getPincode(),comp.getGstinno(),
////                                        comp.getInvoiceno(),comp.getTaxaccross(),comp.getCgst(),comp.getSgst(),comp.getIgst(),comp.getInvoicectr(),
////                                        comp.getTaxincludedamt(),comp.getReportfooter(),comp.getShowbankdetails(),comp.getAccholdername(),
////                                        comp.getAccno(),comp.getBankname(),comp.getBankbranch(),comp.getIfsccode(),comp.getInheaderwithtax(),
////                                        comp.getInheaderwithouttax(),comp.getPassword(),comp.getLogincount(),comp.getTripnoAI(),comp.getTripnoCounter(),comp.getCompanylogo());
//                          }
//
//                        } else {
////                            progress.showEmpty(emptyDrawable,
////                                    "Trip Product is empty",
////                                    "Please wait");
//                        }
//
//
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        // handle error
//                        //progressActivity.showContent();
//
////                        progress.showError(errorDrawable, "No Connection",
////                                "We could not establish a connection with our servers. Try again when you are connected to the internet.",
////                                "Try Again", errorClickListener);
//
//                        //Toast.makeText(getApplicationContext(), anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//
//
//    }
    public void downloadCustomer() {

        AndroidNetworking.get(ApiEndPoint.BASE_URL + ApiEndPoint.GET_CUSTOMER)
                // AndroidNetworking.get("http://www.json-generator.com/api/json/get/ceMXczcMpu?indent=2")

                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()
                .getAsOkHttpResponseAndParsed(new TypeToken<UploadCustomer>() {
                }, new OkHttpResponseAndParsedRequestListener<UploadCustomer>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UploadCustomer adel) {

                        List<UploadCustomer.Custlist> prdatalist = adel.getCustlist();


                        if (!prdatalist.isEmpty()) {
                            int lengthData = adel.getCustlist().size();
                            for (int i = 0; i <= lengthData - 1; i++) {
                                culist = prdatalist.get(i);
                          saveToCustomer(culist.getId(), culist.getName(), culist.getMobile(),culist.getEmailid(),culist.getAddress1(),culist.getAddress2(),
                                         culist.getAddress3(),culist.getCity(),culist.getStateid(),culist.getPincode(),culist.getGstinno(),
                                         culist.getActive(),culist.getRetailOrMRP());

                            }
                            int maxcid = myDb.getMAXid("Customer");

                            int count = myDb.getCount("Counters");
                            int ctr = maxcid+1;
                            if(count > 0){
                                myDb.updateCounter(ctr,"customerId","Counters");
                            }else {
                                myDb.insertCustomerIdCOunter(ctr,"customerId");

                            }
                          //  myDb.updateCounter(1,"invoiceId","Counters");
                        } else {
//                            progress.showEmpty(emptyDrawable,
//                                    "Trip Product is empty",
//                                    "Please wait");
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        //progressActivity.showContent();

//                        progress.showError(errorDrawable, "No Connection",
//                                "We could not establish a connection with our servers. Try again when you are connected to the internet.",
//                                "Try Again", errorClickListener);

                        //Toast.makeText(getApplicationContext(), anError.getErrorDetail(), Toast.LENGTH_SHORT).show();

                    }
                });


    }

    private void saveToVariant(String id, String desc) {




        myDb.addVariant(id, desc);

    }

    private void saveToProductType(String id, String desc, String catid) {

   // myDb.delete("ProductType");

        myDb.addProductType(id, desc, catid);
    }

    private void saveToProductCompany(String id, String desc) {

    //  myDb.delete("ProductCompany");

        myDb.addProductCompany(id, desc);
    }

    private void saveToProduct(String id, String prcode, String pcompid, String ptypeid, String varintid, String cgstRate, String sgstRate, String igstRate, String barcodeno, String hsncode, String mrp, String retail) {

     //myDb.delete("Product");


        myDb.addProduct(id, prcode, pcompid, ptypeid, varintid, cgstRate, sgstRate, igstRate, barcodeno, hsncode, mrp, retail);
    }

    private void saveToProductCategory(String id, String desc) {

      //  myDb.delete("ProductCategory");

        myDb.addProductCategory(id, desc);
    }

    private void saveToCompany(String id, String name, String mobile, String emailid, String address1, String address2, String address3, String city, String stateid, String pincode, String gstinno, String invoiceno, String taxaccross, String cgst, String sgst, String igst, String invoicectr, String taxincludedamt, String reportfooter, String showbankdetails, String accholdername, String accno, String bankname, String bankbranch, String ifsccode, String inheaderwithtax, String inheaderwithouttax, String password, String logincount, String tripnoAI, String tripnoCounter,String complogo) {

     // myDb.delete("Company");

       myDb.addCompany(id,name,mobile,emailid,address1,address2,address3,city,stateid,pincode,gstinno,invoiceno,taxaccross,cgst,sgst,igst,invoicectr,taxincludedamt,reportfooter,showbankdetails,accholdername,accno,bankname,bankbranch,ifsccode,inheaderwithtax,inheaderwithouttax,password,logincount,tripnoAI,tripnoCounter,complogo);
    }

    private void saveToCustomer(String id, String name, String mobile, String emailid, String address1, String address2, String address3, String city, String stateid, String pincode, String gstinno, String active, String retailOrMRP) {
    //   int sid = myDb.getCount();
       // Toast.makeText(getApplicationContext(),"hello"+sid, Toast.LENGTH_LONG).show();
    // myDb.delete("Customer");

      myDb.addCustomer(id,name,mobile,emailid,address1,address2,address3,city,stateid,pincode,gstinno,active,retailOrMRP);
    }
    private void setupToolbar() {
        toolbar.setTitle("Hems Sales" );


    }

}

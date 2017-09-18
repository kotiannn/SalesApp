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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.github.fabtransitionactivity.SheetLayout;
import com.mahathisol.salesapp.R;
import com.mahathisol.salesapp.activities.DatabaseHelper;
import com.mahathisol.salesapp.adapter.InvoiceAdapter;
import com.mahathisol.salesapp.app.AppController;
import com.mahathisol.salesapp.pojos.EventADForm;
import com.mahathisol.salesapp.pojos.EventInvoiceDetails;
import com.mahathisol.salesapp.pojos.EventRefresh;
import com.mahathisol.salesapp.pojos.InvoiceDetailsList;
import com.mahathisol.salesapp.utils.KeyboardUtil;
import com.mahathisol.salesapp.utils.SharedPrefKey;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.vlonjatg.progressactivity.ProgressActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class EditInvoiceDetailsList extends BottomSheetDialogFragment {
    public static final String ARG_ITEM_ID = "hotel_id";
    public static final String TAG = EditInvoiceDetailsList.class.getSimpleName();
    private final int REQ_PERMISSION = 999;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    Unbinder unbinder1;
    @BindView(R.id.bottom_sheet)
    SheetLayout bottomSheet;
    @BindView(R.id.idinvoiceno)
    TextView idinvoiceno;
    Unbinder unbinder2;
    private SharedPreferences mSecurePrefs;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress)
    ProgressActivity progressActivity;
    Unbinder unbinder;
    int token = 0;
    private BottomSheetBehavior mBehavior;
    private DatabaseHelper myDb;
    String invoiceno = "";
    InvoiceAdapter adaptern;
    List<InvoiceDetailsList> productList = new ArrayList<>();
    String tripid = "";
    String soldquant = "";
    String pid = "";
    Drawable emptyDrawable;
    String InStock = "";
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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = View.inflate(getContext(), R.layout.fragment_edit_invoicedetails_results, null);

        unbinder = ButterKnife.bind(this, view);


        emptyDrawable = new IconDrawable(getActivity(), Iconify.IconValue.zmdi_alert_circle)
                .colorRes(R.color.colorAccent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            new KeyboardUtil(getActivity(), view);

        myDb = new DatabaseHelper(this.getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSecurePrefs = getSharedPref();
        invoiceno = mSecurePrefs.getString(SharedPrefKey.INVOICE_DETAILS_NO, "");
        tripid = mSecurePrefs.getString(SharedPrefKey.TRIP_ID, "");
        initadapterlisner();
        setupToolbar();
        prepareProductData();
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


        return dialog;
    }

    @OnClick(R.id.fab)
    void onFabClick() {

        new AddMainInvoiceDetailsFragment().show(getActivity().getSupportFragmentManager(), AddMainInvoiceDetailsFragment.TAG);

    }

    private SharedPreferences getSharedPref() {
        if (mSecurePrefs == null) {
            mSecurePrefs = AppController.getInstance().getSharedPreferences();
        }
        return mSecurePrefs;
    }

    void initadapterlisner() {
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                super.onItemChildClick(adapter, view, position);
                switch (view.getId()) {

                    case R.id.img_more:


                        ImageView btnMore = (ImageView) view.findViewById(R.id.img_more);


                        PopupMenu popup = new PopupMenu(view.getContext(), btnMore);
                        MenuInflater inflater = popup.getMenuInflater();
                        popup.setGravity(Gravity.CENTER);
                        inflater.inflate(R.menu.invoice_details, popup.getMenu());


                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.assign:
                                        new EditInvoiceDetailsFormFragment().show(getActivity().getSupportFragmentManager(), EditInvoiceDetailsFormFragment.TAG);
                                        // TimeLineFrehs.BlogList bfb=   bloglist.get(position);
                                        InvoiceDetailsList dblpoj = productList.get(position);
                                        EventBus.getDefault().postSticky(new EventInvoiceDetails(dblpoj));

                                        break;
                                    case R.id.edit:
                                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                                        //    dialog.setTitle(R.string.content);
                                        dialog.setMessage("Are you sure you want to delete this Invoice?");
                                        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                InvoiceDetailsList dblpoj = productList.get(position);
                                                int id = Integer.parseInt(dblpoj.getId());
                                                int qty = Integer.parseInt(dblpoj.getQuantity());
                                                pid = dblpoj.getProductId();
                                                myDb.getWritableDatabase();

                                                myDb.deletein(id);

                                                soldquant = myDb.getSoldQuant(pid,tripid);
                                                float quantity = Float.parseFloat(dblpoj.getQuantity());
                                                float soldqty = Float.parseFloat(soldquant) - quantity;
                                                String sdqty = String.valueOf(soldqty);
                                                myDb.updateTripProduct(pid, tripid, sdqty);
                                                prepareProductData();
                                                EventBus.getDefault().post(new EventADForm());
                                                // mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                                EventBus.getDefault().post(new EventRefresh());

                                            }
                                        });
                                        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Enter your Code for exit Application
                                                dialog.dismiss();
                                            }
                                        });
                                        dialog.show();

                                        return true;


                                }

                                return false;
                            }
                        });


                        popup.show();


                        break;


                    default:
                        break;
                }
            }


        });


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(EventRefresh loc) {
        // Toast.makeText(getActivity().getApplicationContext(), "EventRefresh", Toast.LENGTH_SHORT).show();
        // progressActivity.showLoading();
        // callTimeLineFreshRestApi(customer_id);
        prepareProductData();

    }

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
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
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

//        unbinder.unbind();


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
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // TODO: inflate a fragment view
//        View rootView = super.onCreateView(inflater, container, savedInstanceState);
//
//        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
////        ButterKnife.bind(this, rootView);
////        ButterKnife.bind(this, rootView);
//        return rootView;
//    }


    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        EventBus.getDefault().post(new EventADForm());
    }

    class DoneOnEditorActionListener implements TextView.OnEditorActionListener {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            // TODO Auto-generated method stub
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);


                return true;
            }
            return false;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

//        unbinder2 = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
       // unbinder2.unbind();

    }

    public static EditInvoiceDetailsList newInstance(int hotelid) {
        EditInvoiceDetailsList mFragment = new EditInvoiceDetailsList();
        Bundle mBundle = new Bundle();
        mBundle.putInt(ARG_ITEM_ID, hotelid);
        mFragment.setArguments(mBundle);
        return mFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDb = new DatabaseHelper(this.getActivity());
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // load dummy item by using the passed item ID.
            //dummyItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            token = getArguments().getInt(ARG_ITEM_ID);
        }
        //   initadapterlisner();
//        setupToolbar();
        //  prepareProductData();

        setHasOptionsMenu(true);
    }


    private void prepareProductData() {
        productList = myDb.getAllInvoiceDetails(invoiceno);
        if (productList.isEmpty()) {
            // Log.d("failure", user.getBlogList().toString());

            progressActivity.showEmpty(emptyDrawable,
                    "No Invoice Details Found ",
                    "");
        } else {
            idinvoiceno.setText("Invoice No : " + productList.get(0).getInvoiceNo());
            adaptern = new InvoiceAdapter(productList);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(adaptern);
//        notificationAdapter.setItemClickCallBack((NotificationAdapter.ItemClickCallBack) this);


            //ArrayList<Product> products = new ArrayList<>();


            adaptern.notifyDataSetChanged();
        }
    }

    private void setupToolbar() {

        toolbar.setTitle("Invoice Details");

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                getDialog().dismiss();

                EventBus.getDefault().post(new EventADForm());
                EventBus.getDefault().post(new EventRefresh());
            }
        });


    }

}

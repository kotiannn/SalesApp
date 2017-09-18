package com.mahathisol.salesapp.fragments;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.mahathisol.salesapp.R;
import com.mahathisol.salesapp.activities.DatabaseHelper;
import com.mahathisol.salesapp.adapter.AdapterP;
import com.mahathisol.salesapp.app.AppController;
import com.mahathisol.salesapp.pojos.EventRefresh;
import com.mahathisol.salesapp.pojos.ProductList;
import com.mahathisol.salesapp.utils.NetworkStateChecker;
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
import butterknife.Unbinder;


/**
 * A placeholder fragment containing ic_english simple view.
 */
public class DisplayProductFragment extends Fragment {
    public static final String ARG_ITEM_ID = "hotel_id";
    public static final String TAG = DisplayProductFragment.class.getSimpleName();
    //RestClient.RestApiInterface service;
    View viewheader;
    Drawable emptyDrawable;
    Drawable errorDrawable;
    ProgressActivity progressActivity;
    int token = 0;
    String tripid = "";
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    public static final String DATA_SAVED_BROADCAST = "com.mahathisol.mobilesales";
    //    @BindView(R.id.swipeLayout)
//    SwipeRefreshLayout swipeLayout;

    Unbinder unbinder;
    @BindView(R.id.totalvalue)
    TextView totalvalue;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.progress)
    ProgressActivity progress;
    private BroadcastReceiver broadcastReceiver;
    String customer_id = "";
    String totval = "";
    AdapterP adaptern;
    List<ProductList> productList = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private DatabaseHelper myDb;
    private SharedPreferences mSecurePrefs;
    private View.OnClickListener errorClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //if(!customer_id.isEmpty()) {
            //  progressActivity.showLoading();
            prepareProductData();
            // }\


        }
    };

    public static DisplayProductFragment newInstance(int hotelid) {
        DisplayProductFragment mFragment = new DisplayProductFragment();
        Bundle mBundle = new Bundle();
        mBundle.putInt(ARG_ITEM_ID, hotelid);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // load dummy item by using the passed item ID.
            //dummyItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            token = getArguments().getInt(ARG_ITEM_ID);
        }
        mSecurePrefs = getSharedPref();
        customer_id = mSecurePrefs.getString(SharedPrefKey.CUSTOMER_ID, "");
       // totalvalue.setText(" Total Value : " + adel.getTotalValue());
        totval = mSecurePrefs.getString(SharedPrefKey.TOT_VAL, "");

        tripid = mSecurePrefs.getString(SharedPrefKey.TRIP_ID, "");
        //customer_id  =mSecurePrefs.getString(SharedPrefKey.CUSTOMER_ID, "");
        System.out.println("CUSTMORE_ID" + customer_id);

        //the broadcast receiver to update sync status

        setHasOptionsMenu(true);
    }

//    private void getvall(String customer_id) {
//        String totval = myDb.gettotalval(customer_id);
//        totalvalue.setText("Total Value (Rs.) : "+totval);
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_product_results, container, false);

        getActivity().registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        //getActivity().setTitle("TimeLine");
        // getActivity().getSupportActionBar().setElevation(0);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);
          totalvalue = (TextView) rootView.findViewById(R.id.totalvalue);

        progressActivity = (ProgressActivity) rootView.findViewById(R.id.progress);


        //  List<Integer> skipIds = new ArrayList<>();
        //skipIds.add(R.id.to);
        emptyDrawable = new IconDrawable(getActivity(), Iconify.IconValue.zmdi_alert_circle)
                .colorRes(R.color.colorAccent);

        errorDrawable = new IconDrawable(getActivity(), Iconify.IconValue.zmdi_wifi_off)
                .colorRes(R.color.colorAccent);
        // progressActivity.showLoading();
        myDb = new DatabaseHelper(this.getActivity());

        initadapterlisner();

        prepareProductData();
   totalvalue.setText("Total Value (Rs.) : "+totval);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        unbinder = ButterKnife.bind(this, rootView);
        return rootView;


    }


    private void prepareProductData() {
        // mSecurePrefs = getSharedPref();


        productList = myDb.getAllProducts(tripid);
        if (productList.isEmpty()) {
            // Log.d("failure", user.getBlogList().toString());

            progressActivity.showEmpty(emptyDrawable,
                    "No Products Found",
                    "");
        } else {

            adaptern = new AdapterP(productList);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(adaptern);
//        notificationAdapter.setItemClickCallBack((NotificationAdapter.ItemClickCallBack) this);


            //ArrayList<Product> products = new ArrayList<>();


            adaptern.notifyDataSetChanged();
        }
    }


    void initadapterlisner() {
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                super.onItemChildClick(adapter, view, position);

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

    private SharedPreferences getSharedPref() {
        if (mSecurePrefs == null) {
            mSecurePrefs = AppController.getInstance().getSharedPreferences();
        }
        return mSecurePrefs;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}




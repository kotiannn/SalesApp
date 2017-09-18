package com.mahathisol.salesapp.fragments;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.mahathisol.salesapp.R;
import com.mahathisol.salesapp.activities.DatabaseHelper;
import com.mahathisol.salesapp.adapter.AdapterN;
import com.mahathisol.salesapp.app.AppController;
import com.mahathisol.salesapp.pojos.Customer;
import com.mahathisol.salesapp.pojos.CustomerList;
import com.mahathisol.salesapp.pojos.EventCustomer;
import com.mahathisol.salesapp.pojos.EventRefresh;
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
public class DisplayCustomerFragment extends Fragment {
    public static final String ARG_ITEM_ID = "hotel_id";
    //RestClient.RestApiInterface service;
    View viewheader;
    Drawable emptyDrawable;
    Drawable errorDrawable;
    ProgressActivity progressActivity;
    int token = 0;
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    public static final String DATA_SAVED_BROADCAST = "com.mahathisol.mobilesales";
//    @BindView(R.id.swipeLayout)
//    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.progress)
    ProgressActivity progress;
    Unbinder unbinder;

    String customer_id = "";
    AdapterN adaptern;
    List<CustomerList> productList = new ArrayList<>();
    List<Customer.Customerlist> adlist;
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

    public static DisplayCustomerFragment newInstance(int hotelid) {
        DisplayCustomerFragment mFragment = new DisplayCustomerFragment();
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
        //customer_id  =mSecurePrefs.getString(SharedPrefKey.CUSTOMER_ID, "");
        System.out.println("CUSTMORE_ID" + customer_id);

        //the broadcast receiver to update sync status

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_customer_results, container, false);


        //getActivity().setTitle("TimeLine");
        // getActivity().getSupportActionBar().setElevation(0);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);

//        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
//        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // Refresh items
//                prepareProductData();
//                swipeLayout.setRefreshing(false);
//            }
//        });
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



        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //initAdapter();
        prepareProductData();
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;


    }


    private void prepareProductData() {
        productList = myDb.getAllCustomers();
        if (productList.isEmpty()) {
            // Log.d("failure", user.getBlogList().toString());

            progressActivity.showEmpty(emptyDrawable,
                    "No Customers Found",
                    "");
        } else {

            adaptern = new AdapterN(productList);
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
                switch (view.getId()) {

                    case R.id.img_more:
                        //  Toast.makeText(getActivity().getApplicationContext(), "edit", Toast.LENGTH_SHORT).show();
                        new EditCustomerFormFragment().show(getActivity().getSupportFragmentManager(), EditCustomerFormFragment.TAG);
                        // TimeLineFrehs.BlogList bfb=   bloglist.get(position);
                        CustomerList dblpoj = productList.get(position);
                        EventBus.getDefault().postSticky(new EventCustomer(dblpoj));

//                       ImageView btnMore = (ImageView) view.findViewById(R.id.img_more);
//
//
//                        PopupMenu popup = new PopupMenu(view.getContext(), btnMore);
//                        MenuInflater inflater = popup.getMenuInflater();
//                        popup.setGravity(Gravity.CENTER);
//                        inflater.inflate(R.menu.customer_popup_menu, popup.getMenu());
//
//
//                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                            @Override
//                            public boolean onMenuItemClick(MenuItem item) {
//                                switch (item.getItemId()) {
//                                    case R.id.edit:
//
//
//
//                                        break;
//
//                                }
//                                return false;
//                            }
//                        });
//
//
//                        popup.show();
//
//

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




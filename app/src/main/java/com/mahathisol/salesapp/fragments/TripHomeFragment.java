package com.mahathisol.salesapp.fragments;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.mahathisol.salesapp.R;
import com.mahathisol.salesapp.activities.DatabaseHelper;
import com.mahathisol.salesapp.adapter.AdapterN;
import com.mahathisol.salesapp.app.AppController;
import com.mahathisol.salesapp.pojos.EventRefresh;
import com.vlonjatg.progressactivity.ProgressActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A placeholder fragment containing ic_english simple view.
 */
public class TripHomeFragment extends Fragment {
    public static final String TAG = TripHomeFragment.class.getSimpleName();
    public static final String ARG_ITEM_ID = "hotel_id";
    //RestClient.RestApiInterface service;
    View viewheader;
    Drawable emptyDrawable;
    Drawable errorDrawable;
    ProgressActivity progressActivity;
    int token = 0;

    String customer_id = "";
    AdapterN adaptern;


    Unbinder unbinder;

    @BindView(R.id.startTrip)
    ImageButton startTrip;
    @BindView(R.id.stopTrip)
    ImageButton stopTrip;
    @BindView(R.id.editRemarks)
    ImageButton editRemarks;

    @BindView(R.id.progress)
    ProgressActivity progress;
    @BindView(R.id.bottom_sheet)
    FrameLayout bottomSheet;
    @BindView(R.id.viewproducts)
    ImageButton viewproducts;

    private RecyclerView mRecyclerView;
    private DatabaseHelper myDb;
    private SharedPreferences mSecurePrefs;

    public static TripHomeFragment newInstance(int hotelid) {
        TripHomeFragment mFragment = new TripHomeFragment();
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


        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_trip_home, container, false);


        myDb = new DatabaseHelper(this.getActivity());


        unbinder = ButterKnife.bind(this, rootView);
        return rootView;


    }

    @OnClick(R.id.startTrip)
    public void onclickstartTrip() {
        new StartTripFormFragment().show(getActivity().getSupportFragmentManager(), StartTripFormFragment.TAG);


    }

    @OnClick(R.id.stopTrip)
    public void onclickstopTrip() {
        new StopTripFormFragment().show(getActivity().getSupportFragmentManager(), StopTripFormFragment.TAG);

    }

    @OnClick(R.id.editRemarks)
    public void onclickeditRemarks() {
        new EditRemarksFormFragment().show(getActivity().getSupportFragmentManager(), EditRemarksFormFragment.TAG);

    }
    @OnClick(R.id.viewproducts)
    public void onclickviewProducts() {
        FragmentManager fragMan = this.getActivity().getSupportFragmentManager();

        FragmentTransaction fragTrans = fragMan.beginTransaction();
        //AssignDelivery fragB = new AssignDelivery();
        DisplayProductFragment fragdiesel = DisplayProductFragment.newInstance(token);
        //fragB.n
        fragTrans.replace(R.id.contentContainer, fragdiesel);
        //getSupportActionBar().setTitle("Timeline");
        //toolbar.setTitle("Timeline");
        //childFragTrans.addToBackStack("B");
        fragTrans.commit();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(EventRefresh loc) {
        // Toast.makeText(getActivity().getApplicationContext(), "EventRefresh", Toast.LENGTH_SHORT).show();
//        progressActivity.showLoading();
        // callTimeLineFreshRestApi(customer_id);


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
        // null.unbind();
    }
}




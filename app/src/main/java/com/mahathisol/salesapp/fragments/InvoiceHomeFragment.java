package com.mahathisol.salesapp.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mahathisol.salesapp.R;
import com.mahathisol.salesapp.activities.BloothPrinterActivity;
import com.mahathisol.salesapp.activities.DatabaseHelper;
import com.mahathisol.salesapp.adapter.AdapterN;
import com.mahathisol.salesapp.app.AppController;
import com.mahathisol.salesapp.utils.SharedPrefKey;
import com.vlonjatg.progressactivity.ProgressActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A placeholder fragment containing ic_english simple view.
 */
public class InvoiceHomeFragment extends Fragment {
    public static final String TAG = InvoiceHomeFragment.class.getSimpleName();
    public static final String ARG_ITEM_ID = "hotel_id";
    //RestClient.RestApiInterface service;
    View viewheader;
    Drawable emptyDrawable;
    Drawable errorDrawable;
    ProgressActivity progressActivity;
    int token = 0;

    String customer_id = "";
    String customer_name = "";
    AdapterN adaptern;


    @BindView(R.id.addButton)
    ImageButton addButton;
    @BindView(R.id.editButton)
    ImageButton editButton;
    @BindView(R.id.printButton)
    ImageButton printButton;
    @BindView(R.id.progress)
    ProgressActivity progress;
    @BindView(R.id.bottom_sheet)
    FrameLayout bottomSheet;
    Unbinder unbinder;
    String tripflag = "";
    String tripno = "";
    @BindView(R.id.loginname)
    TextView loginname;
    @BindView(R.id.tripstatus)
    TextView tripstatus;
    @BindView(R.id.tripnos)
    TextView tripnos;

    //    @BindView(R.id.status)
//    TextView status;
    private RecyclerView mRecyclerView;
    private DatabaseHelper myDb;
    private SharedPreferences mSecurePrefs;

    public static InvoiceHomeFragment newInstance(int hotelid) {
        InvoiceHomeFragment mFragment = new InvoiceHomeFragment();
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
//        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //  EventBus.getDefault().unregister(this);
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
        customer_name = mSecurePrefs.getString(SharedPrefKey.CUSTOMER_NAME, "");
        tripno = mSecurePrefs.getString(SharedPrefKey.TRIP_NO, "");
        System.out.println("CUSTMORE_ID" + customer_id);


        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_invoice_home, container, false);
        progressActivity = (ProgressActivity)rootView. findViewById(R.id.progress);

        myDb = new DatabaseHelper(this.getActivity());
        loginname = (TextView) rootView.findViewById(R.id.loginname);
        tripstatus = (TextView) rootView.findViewById(R.id.tripstatus);
        tripnos = (TextView)rootView.findViewById(R.id.tripnos);
        addButton = (ImageButton) rootView.findViewById(R.id.addButton);
        editButton = (ImageButton) rootView.findViewById(R.id.editButton);
        printButton = (ImageButton)rootView.findViewById(R.id.printButton);
//progressActivity.showLoading();
        tripflag =  mSecurePrefs.getString(SharedPrefKey.TRIP_STATUS, "");

        if (tripflag.equals("0")) {
            tripstatus.setText("Trip Status : open");
            progressActivity.showContent();
            addButton.setClickable(false);
            editButton.setClickable(false);
            printButton.setClickable(false);

        } else if (tripflag.equals("1")) {
            tripstatus.setText("Trip Status : loaded");
            progressActivity.showContent();
            addButton.setClickable(false);
            editButton.setClickable(false);
            printButton.setClickable(false);
        } else if (tripflag.equals("2")) {
            tripstatus.setText("Trip Status : started");
            progressActivity.showContent();
        } else if (tripflag.equals("3")) {
            tripstatus.setText("Trip Status : closed");
            progressActivity.showContent();
            addButton.setClickable(false);
            editButton.setClickable(false);
            printButton.setClickable(false);
        }
      // bindStatus(customer_id);
         loginname.setText(customer_name);
      tripnos.setText("Trip No : " + tripno);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;


    }


    @OnClick(R.id.addButton)
    public void onClickAdd() {
        new AddInvoiceFragment().show(getActivity().getSupportFragmentManager(), AddInvoiceFragment.TAG);

    }

    @OnClick(R.id.editButton)
    public void onClickEdit() {
        new EditInvoice().show(getActivity().getSupportFragmentManager(), EditInvoice.TAG);

    }
    @OnClick(R.id.printButton)
    public void onClickPrint() {

        Intent intent = new Intent(getActivity(), BloothPrinterActivity.class);
        startActivity(intent);

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




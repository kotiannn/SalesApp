package com.mahathisol.salesapp.fragments;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.reflect.TypeToken;
import com.mahathisol.salesapp.R;
import com.mahathisol.salesapp.adapter.DieselAdapter;
import com.mahathisol.salesapp.app.AppController;
import com.mahathisol.salesapp.pojos.Dieselli;
import com.mahathisol.salesapp.pojos.EventDiesel;
import com.mahathisol.salesapp.pojos.EventRefresh;
import com.mahathisol.salesapp.utils.ApiEndPoint;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.vlonjatg.progressactivity.ProgressActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import okhttp3.Response;


/**
 * A placeholder fragment containing ic_english simple view.
 */
public class DieselFragment extends Fragment {
    public static final String ARG_ITEM_ID = "hotel_id";
    //RestClient.RestApiInterface service;
    View viewheader;
    Drawable emptyDrawable;
    Drawable errorDrawable;
    ProgressActivity progressActivity;
int token=0;

    String  customer_id="";
    List<Dieselli.Diesellist> adlist;
    private RecyclerView mRecyclerView;
    private DieselAdapter mQuickAdapter;
    private SharedPreferences mSecurePrefs;
    private View.OnClickListener errorClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //if(!customer_id.isEmpty()) {
            progressActivity.showLoading();
            callTimeLineFreshRestApi(customer_id);
            // }\


        }
    };

    public static DieselFragment newInstance(int hotelid){
        DieselFragment mFragment = new DieselFragment();
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

            token=  getArguments().getInt(ARG_ITEM_ID);
        }
        mSecurePrefs = getSharedPref();
        //customer_id  =mSecurePrefs.getString(SharedPrefKey.CUSTOMER_ID, "");
        System.out.println("CUSTMORE_ID"+customer_id);


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

       progressActivity = (ProgressActivity)rootView. findViewById(R.id.progress);


      //  List<Integer> skipIds = new ArrayList<>();
        //skipIds.add(R.id.to);
         emptyDrawable = new IconDrawable(getActivity(), Iconify.IconValue.zmdi_alert_circle)
                .colorRes(R.color.colorAccent);

         errorDrawable = new IconDrawable(getActivity(), Iconify.IconValue.zmdi_wifi_off)
                .colorRes(R.color.colorAccent);
        progressActivity.showLoading();
       // mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//if(!customer_id.isEmpty()) {
//    callTimeLineFreshRestApi(customer_id);
//}
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {

//            @Override
//            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
//                //  Toast.makeText(getActivity().getApplicationContext(), "hi", Toast.LENGTH_SHORT).show();
//                Customer.Customerlist dblpoj = adlist.get(position);
//
//                String value1 = dblpoj.getName();
//                new MaterialDialog.Builder(getActivity())
//                        //new MaterialDialog.Builder(this)
//                        .title(R.string.customer)
//                     //   .content((CharSequence) adlist.get(position))
//
//                        .content("Name : "+dblpoj.getName()+"\n"+"Phone No : "+dblpoj.getPhoneNo()+"\n"+
//                                "EmailId : "+dblpoj.getEmailId()+"\n"+"DoorNo : "+dblpoj.getDoorNo()+"\n"+
//                                "Address1 : "+dblpoj.getAddress1()+"\n"+"Address2 : "+dblpoj.getAddress2()+"\n"+
//                                "Address3 : "+dblpoj.getAddress3()+"\n"+"Pincode : "+dblpoj.getPin()+"\n"+
//                                "Post : "+dblpoj.getPost()+"\n"+"District : "+dblpoj.getDistrict()+"\n"+
//                                "State : "+dblpoj.getState())
//
//                        .positiveText(R.string.agree)
//
//                        .show();
//                if(dblpoj.getAddress2().equals("")){
//
//
//                }
//               //Toast.makeText(getActivity().getApplicationContext(), (CharSequence) adlist, Toast.LENGTH_SHORT).show();
//            }
//
@Override
public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

}

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                super.onItemChildClick(adapter, view, position);
                switch (view.getId()) {

                    case R.id.img_more:
                        new EditDieselFillingFragment().show(getActivity().getSupportFragmentManager(), EditDieselFillingFragment.TAG);
//                        // TimeLineFrehs.BlogList bfb=   bloglist.get(position);
                        Dieselli.Diesellist dblpoj = adlist.get(position);
                        EventBus.getDefault().postSticky(new EventDiesel(dblpoj));

                        break;


                    default:
                        break;
                }
            }


        });
        callTimeLineFreshRestApi(customer_id);
        //initAdapter();

        return rootView;


    }

    public void callTimeLineFreshRestApi(String cust_id){

       AndroidNetworking.get(ApiEndPoint.BASE_URL+ApiEndPoint.DIESEL_LIST)
       // AndroidNetworking.get("http://www.json-generator.com/api/json/get/ceMXczcMpu?indent=2")

                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()

                .getAsOkHttpResponseAndParsed(new TypeToken<Dieselli>() {
                }, new OkHttpResponseAndParsedRequestListener<Dieselli>() {
                    @Override
                    public void onResponse(Response okHttpResponse, Dieselli adel) {


                        if(adel.getDiesellist().isEmpty()){
                           // Log.d("failure", user.getBlogList().toString());

                            progressActivity.showEmpty(emptyDrawable,
                                    "Diesel Filling Details not Found",
                                    "Please Add Diesel Filling");
                        }
                        else{

                                Log.d("TIME_LINE_REQUEST_TAG",adel.getDiesellist().toString() );


                            if(!adel.getDiesellist().isEmpty()){
                                adlist= adel.getDiesellist();
                                initAdapter(adlist) ;
                                progressActivity.showContent();
                            }
                                else{
                                progressActivity.showEmpty(emptyDrawable,
                                        "No Gags found!!",
                                        "");

                                }





                        }




                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        //progressActivity.showContent();

                        progressActivity.showError(errorDrawable, "No Connection",
                                "We could not establish a connection with our servers. Try again when you are connected to the Internet.",
                                "Try Again", errorClickListener);

                        //Toast.makeText(getApplicationContext(), anError.getErrorDetail(), Toast.LENGTH_SHORT).show();

                    }
                });


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(EventRefresh loc) {
        // Toast.makeText(getActivity().getApplicationContext(), "EventRefresh", Toast.LENGTH_SHORT).show();
        progressActivity.showLoading();
        callTimeLineFreshRestApi(customer_id);

    }

    private SharedPreferences getSharedPref() {
        if (mSecurePrefs == null) {
            mSecurePrefs = AppController.getInstance().getSharedPreferences();
        }
        return mSecurePrefs;
    }

    private void initAdapter(List<Dieselli.Diesellist> dlist) {
//        Toast.makeText(getActivity(), "fragment", Toast.LENGTH_LONG).show();



        mQuickAdapter = new DieselAdapter(dlist);
        mQuickAdapter.openLoadAnimation();

        mRecyclerView.setAdapter(mQuickAdapter);







    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        // TODO Auto-generated method stub
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu_vendor, menu);
//
////        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
////        searchView.setQueryHint(this.getString(R.string.search));
////
////        ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text))
////                .setHintTextColor(getResources().getColor(R.color.nliveo_white));
////        searchView.setOnQueryTextListener(onQuerySearchView);
////
////		menu.findItem(R.id.menu_add).setVisible(true);
////		menu.findItem(R.id.menu_search).setVisible(true);
//
//        //mSearchCheck = false;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // TODO Auto-generated method stub
//
////		switch (item.getItemId()) {
////
////		case R.id.menu_add:
////
////			break;
////
////		case R.id.menu_search:
////			mSearchCheck = true;
////
////			break;
////		}
//        return true;
//    }
}

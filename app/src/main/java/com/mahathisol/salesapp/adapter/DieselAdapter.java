package com.mahathisol.salesapp.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mahathisol.salesapp.R;
import com.mahathisol.salesapp.pojos.Dieselli;


import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class DieselAdapter extends BaseQuickAdapter<Dieselli.Diesellist,BaseViewHolder> {





    public DieselAdapter(List<Dieselli.Diesellist> hresult) {

        super( R.layout.card_view_recycler_diesel_item, hresult);
    }

    @Override
    protected void convert(final BaseViewHolder helper, Dieselli.Diesellist item) {

//        helper.setText(R.id.article_title, "Jan, 5, 2016")
//                .setText(R.id.article_subtitle, "Watergrill Abbotsford")
//                .setText(R.id.bookmarks, item.getLike_count()+" "+"Bookmarks")
//                .setText(R.id.shares, item.getShare_count()+" "+"shares")
//                .setText(R.id.comments, item.getComment_count()+" "+"Comments")
//                .addOnClickListener(R.id.img_bookmark)
//                .addOnClickListener(R.id.img_comments)
//                .addOnClickListener(R.id.img_whatsapp)
//                .addOnClickListener(R.id.img_more);

       // String add= "Address: "+"\n"+"D/No: "+item.getDoorNo()+"\n"+item.getAddress1()+"\n"+item.getAddress2()+"\n"+item.getAddress3()+"\n"+item.getCity()+"\n"+item.getDistrict();
        // add.replaceAll("\\S+","");
                helper.setText(R.id.vehicle_name,"Vehicle : " +item.getVehicleregno())
                .setText(R.id.fill_date, "Date : "+item.getFilldate())
                        .setText(R.id.fill_rate, "Rate (Rs.) : "+item.getRate())
                        .setText(R.id.fill_litre, "Litre : "+item.getLitre())
                        .setText(R.id.fill_amt, "Amount (Rs.) : "+item.getAmount())
                                .setText(R.id.fill_bunk, "Bunk : "+item.getBunk())
                        .addOnClickListener(R.id.img_more);

    }








}

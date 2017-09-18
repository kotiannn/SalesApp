package com.mahathisol.salesapp.adapter;

/**
 * Created by HP on 4/13/2017.
 */


import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mahathisol.salesapp.R;
import com.mahathisol.salesapp.pojos.CustomerList;

import java.util.List;

import butterknife.BindView;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class AdapterN extends BaseQuickAdapter<CustomerList, BaseViewHolder> {





    public AdapterN(List<CustomerList> hresult) {

        super(R.layout.card_view_recycler_customer_item, hresult);

    }

    @Override
    protected void convert(final BaseViewHolder helper, CustomerList item) {


String add ;
if(item.getMobile()=="" || item.getMobile()==null){
   add = " ";
}else{
   add = item.getMobile();
}

        helper.setText(R.id.customer_name, "Customer Name: "+item.getName())
                .setText(R.id.phone_no, "Mobile No.: " + add)
                .setText(R.id.address,"City: "+ item.getCity())
               // .setText(R.id.imageViewStatus, (item.getStatus().equals("0")) ? imageViewStatus.setBackgroundResource(R.drawable.stopwatch) :  imageViewStatus.setBackgroundResource(R.drawable.success) );
//               .setImageResource(R.id.imageViewStatus,(item.getStatus().equals("0"))?R.drawable.stopwatch:R.drawable.success)
               .setVisible(R.id.img_more,(item.getStatus().equals("0"))?true: false)
         .addOnClickListener(R.id.img_more);

    }


}



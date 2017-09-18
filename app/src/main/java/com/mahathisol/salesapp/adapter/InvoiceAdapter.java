package com.mahathisol.salesapp.adapter;

/**
 * Created by HP on 4/13/2017.
 */


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mahathisol.salesapp.R;
import com.mahathisol.salesapp.pojos.InvoiceDetailsList;

import java.util.List;

import static com.mahathisol.salesapp.R.id.idtaxableamt;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class InvoiceAdapter extends BaseQuickAdapter<InvoiceDetailsList, BaseViewHolder> {





    public InvoiceAdapter(List<InvoiceDetailsList> hresult) {

        super(R.layout.card_view_recycler_invoice_item, hresult);

    }

    @Override
    protected void convert(final BaseViewHolder helper, InvoiceDetailsList item) {

//        helper.setText(R.id.article_title, "Jan, 5, 2016")
//                .setText(R.id.article_subtitle, "Watergrill Abbotsford")
//                .setText(R.id.bookmarks, item.getLike_count()+" "+"Bookmarks")
//                .setText(R.id.shares, item.getShare_count()+" "+"shares")
//                .setText(R.id.comments, item.getComment_count()+" "+"Comments")
//                .addOnClickListener(R.id.img_bookmark)
//                .addOnClickListener(R.id.img_comments)
//                .addOnClickListener(R.id.img_whatsapp)
//                .addOnClickListener(R.id.img_more);

        String add = "Product : " + item.getPTypeName() + " - " + item.getPVariantName();
        String cgst = item.getCGSTRate();
        String sgst = item.getSGSTRate();
        String igst = item.getIGSTRate();
        float gst;
        float igstamt;
        String gstamt;
        String stt;
        String sttr;
        if (igst.equals("")) {
          gst = Float.parseFloat(cgst + sgst);

            float qty = Float.parseFloat(item.getQuantity());
            float rate = Float.parseFloat(item.getRate());
            float ta = qty * rate;
            stt = String.format("%.2f", ta);
            float PA = ta / (1 + ((gst) / 100));
            sttr = String.format("%.2f", PA);
            float CGSTAmt = PA * Float.valueOf(cgst) / 100;
            // Calculate SGST Amount
            float SGSTAmt = ta - PA - CGSTAmt;

           igstamt = CGSTAmt + SGSTAmt;
            gstamt = String.format("%.2f", igstamt);
        }else {
            gst = Float.parseFloat(igst);
            float qty = Float.parseFloat(item.getQuantity());
            float rate = Float.parseFloat(item.getRate());
            float ta = qty * rate;
          stt = String.format("%.2f", ta);
            float PA = ta / (1 + ((gst) / 100));
             sttr = String.format("%.2f", PA);
            igstamt = ta - PA;
            gstamt = String.format("%.2f", igstamt);
        }

         helper.setText(R.id.product,add)
                 .setText(R.id.idrate,"Rate (Rs.): " + item.getRate())
                .setText(R.id.idhsncode,"HSNCode : " + item.getPHSNcode())
                 .setText(R.id.quantity,"Quantity : " + item.getQuantity())
                .setText(R.id.gstrate,"GST Rate : " + gst + "%")
                .setText(idtaxableamt,"Taxable Amount (Rs.): " + sttr)
                .setText(R.id.idgstamt,"GST Amount (Rs.): " + gstamt)
                .setText(R.id.idtotalamt,"Total Amount (Rs.): " + stt)
                 .addOnClickListener(R.id.img_more);


    }


}



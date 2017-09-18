package com.mahathisol.salesapp.adapter;

/**
 * Created by HP on 4/13/2017.
 */


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mahathisol.salesapp.R;
import com.mahathisol.salesapp.pojos.ProductList;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class AdapterP extends BaseQuickAdapter<ProductList, BaseViewHolder> {


    public AdapterP(List<ProductList> hresult) {

        super(R.layout.card_view_recycler_product_item, hresult);

    }

    @Override
    protected void convert(final BaseViewHolder helper, ProductList item) {

        helper.setText(R.id.pr_type, "Product: "+item.getPrtype() + " - " + item.getPrvariant())

                .setText(R.id.inward_qty,"Inward Qty : "+ item.getInwardQty())
                .setText(R.id.opening_stock,"Opening Stock : "+ item.getOpeningStock())
                .setText(R.id.closin_stock,"Closing Stock : "+ item.getClosingStock())
                .setText(R.id.return_qty,"Return Qty : "+ item.getreturnquantity())
                .setText(R.id.sold_qty,"Sold Qty : "+ item.getsoldquantity());
    }


}



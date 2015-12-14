package com.wqlabs.sgxstocks;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;

import java.util.List;

/**
 * Created by RadianceOng on 14-Dec-15.
 */
public class AddStockAutoCompleteAdapter extends ArrayAdapter<SGXStockInfo> implements ListAdapter {
    public AddStockAutoCompleteAdapter(Context context, List<SGXStockInfo> objects) {
        super(context, R.layout.trade_info_group, R.id.tradeInfoGroupTextView, objects);
    }
}

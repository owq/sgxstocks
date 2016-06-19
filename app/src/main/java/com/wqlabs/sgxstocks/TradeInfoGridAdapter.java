package com.wqlabs.sgxstocks;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by RadianceOng on 13-Dec-15.
 */
public class TradeInfoGridAdapter extends BaseAdapter {
    private Context _context;

    FilteredTradeViewModel tradeData;

    public TradeInfoGridAdapter(Context _context, FilteredTradeViewModel tradeData) {
        this._context = _context;
        this.tradeData = tradeData;
    }

    @Override
    public int getCount() {
        return tradeData.infoSize();
    }

    @Override
    public SGXStockInfo getItem(int position) {
        if (tradeData != null) {
            return tradeData.getInfo(position);
        }
        else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.trade_info_group, null);
        }

        SGXStockInfo info = getItem(position);
        if (info != null) {
            TextView tv = (TextView) convertView.findViewById(R.id.tradeInfoGroupTextView);
            String s = String.format(
                    "<b>%s</b>" + "<br>" +
                            "%s",
                    info.getStockCode(), info.getLastPrice());

            tv.setText(Html.fromHtml(s));
        }

        return convertView;
    }
}

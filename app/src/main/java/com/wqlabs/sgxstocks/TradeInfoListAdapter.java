package com.wqlabs.sgxstocks;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * Created by RadianceOng on 13-Dec-15.
 */
public class TradeInfoListAdapter extends BaseExpandableListAdapter {

    private Context _context;

    FilteredTradeViewModel tradeData;

    public TradeInfoListAdapter(Context _context, FilteredTradeViewModel tradeData) {
        this._context = _context;
        this.tradeData = tradeData;
    }

    @Override
    public int getGroupCount() {
        return tradeData.infoSize();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public SGXStockInfo getGroup(int groupPosition) { //Probably can have an interface later...
        if (tradeData != null) {
            return tradeData.getInfo(groupPosition);
        }

        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.trade_info_group, null);
        }

        SGXStockInfo info = getGroup(groupPosition);
        if (info != null) {
            TextView tv = (TextView) convertView.findViewById(R.id.tradeInfoGroupTextView);
            String s = new StockInfoHelper(info).asVerboseHTML();

            tv.setText(Html.fromHtml(s));
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}

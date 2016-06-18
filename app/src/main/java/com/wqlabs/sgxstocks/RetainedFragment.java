package com.wqlabs.sgxstocks;

import android.app.Fragment;
import android.os.Bundle;

import java.util.Date;

/**
 * Created by RadianceOng on 18-Jun-16.
 */
public class RetainedFragment extends Fragment {
    Date lastUpdatedDate;
    // data object we want to retain
    private TradeData data;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public void setData(TradeData data) {
        this.data = data;
    }

    public TradeData getData() {
        return data;
    }
}

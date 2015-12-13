package com.wqlabs.sgxstocks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by RadianceOng on 13-Dec-15.
 */
public class TradeData {

    JSONObject obj;
    JSONArray items;

    public TradeData() throws IOException {
        load();
    }

    private void load() throws IOException {
        obj = new SGXParser().getTradeData();
        if(obj == null) throw new IOException();
        try {
            JSONArray items = obj.getJSONArray("items");
            this.items = items;
        } catch (JSONException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }

    public String getTimeStamp() {
        try {
            return obj.getString("label");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray getItems() {
        return items;
    }
}

package com.wqlabs.sgxstocks;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by RadianceOng on 13-Dec-15.
 */
public class SGXStockInfo {

    JSONObject obj;

    public SGXStockInfo(JSONObject obj) {
        this.obj = obj;
    }

    public double getBuyVol() {
        return getDouble("BV");
    }

    public double getSellVol() {
        return getDouble("SV");
    }

    public String getStockName() {
        return getStr("N");
    }

    public String getStockCode() {
        return getStr("NC");
    }


    public String getBuyPrice() {
        return getStr("B");
    }

    public String getSellPrice() {
        return getStr("S");
    }

    private String getStr(String p) {
        try {
            return obj.getString(p);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private double getDouble(String str) {
        try {
            return obj.getDouble(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public String toString() {
        return getStockCode() + " " + getStockName();
    }
}

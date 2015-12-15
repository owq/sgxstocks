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

    public long getID() { return getLong("ID"); }

    public String getStockName() {
        return getStr("N");
    }

    public String getSIP() {
        return getStr("SIP");
    }

    public String getStockCode() {
        return getStr("NC");
    }

    public String getMarket() { return getStr("M"); } //t is catalist, none is main

    public double getLastPrice() {
        return getDouble("LT");
    }

    public double getChange() {
        return getDouble("C");
    }

    public double getVolume() {
        return getDouble("LT")*1000;
    }

    public double getBuyVol() {
        return getDouble("BV")*1000;
    }

    public String getBuyPrice() {
        return getStr("B");
    }

    public String getSellPrice() {
        return getStr("S");
    }

    public double getSellVol() {
        return getDouble("SV")*1000;
    }

    public double getOpenPrice() {
        return getDouble("O");
    }

    public double getHighPrice() {
        return getDouble("H");
    }

    public double getLowPrice() {
        return getDouble("L");
    }

    public double getValue() {
        return getDouble("O")*1000;
    }

    public double getPreviousOpeningPrice() {
        return getDouble("PV");
    }

    public String getPreviousTradeDate() {
        return getStr("PTD");
    }

    public String getBoardLot() {
        return getStr("BL");
    }

    public double getChangePercent() {
        return getDouble("P");
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

    private long getLong(String str) {
        try {
            return obj.getLong(str);
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

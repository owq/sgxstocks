package com.wqlabs.sgxstocks;

/**
 * Created by RadianceOng on 19-Jun-16.
 */
public class StockInfoHelper {
    SGXStockInfo info;

    public StockInfoHelper(SGXStockInfo info) {
        this.info = info;
    }

    public String asVerboseHTML() {
        String s = String.format(
                "<b>%s %s</b> %s, %s-%s (%s)" + "<br>" +
                        "(%.0f) %s / %s (%.0f)",
                info.getStockName(), info.getStockCode(), info.getLastPrice(), info.getLowPrice(), info.getHighPrice(), info.getOpenPrice(),
                info.getBuyVol(), info.getBuyPrice(), info.getSellPrice(), info.getSellVol());
        return s;
    }
}

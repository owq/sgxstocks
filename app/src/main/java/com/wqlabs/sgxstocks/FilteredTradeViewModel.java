package com.wqlabs.sgxstocks;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by RadianceOng on 13-Dec-15.
 */
public class FilteredTradeViewModel {
    Map<String, SGXStockInfo> codeMap = new HashMap<>();
    List<SGXStockInfo> stocksList = Collections.emptyList();

    public FilteredTradeViewModel(TradeData tradeData) {
        try {
            for (int i = 0; i < tradeData.getItems().length(); i++) {
                SGXStockInfo info = new SGXStockInfo(tradeData.getItems().getJSONObject(i));
                codeMap.put(info.getStockCode(), info);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasCode(String code) {
        return codeMap.containsKey(code);
    }

    public SGXStockInfo getInfo(int i) {
        return stocksList.get(i);
    }

    public int infoSize() {
        return stocksList.size();
    }

    public void setFilter(Collection<String> stockCodes) {
        List<SGXStockInfo> s = new ArrayList<>();
        for(final String code : stockCodes) {
            SGXStockInfo info = codeMap.get(code);
            if(info != null) {
                s.add(info);
            } else {
                s.add(new SGXStockInfo(null) {
                    @Override
                    public String getStockCode() {
                        return code;
                    }
                });
            }
        }
        stocksList = s;
    }
}

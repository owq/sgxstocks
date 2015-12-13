package com.wqlabs.sgxstocks;

import android.util.JsonReader;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Created by RadianceOng on 13-Dec-15.
 */
public class SGXParser {

    /**
     * label: As of time
     * items: JSONArray
     *
     * each item--
     * N: short name
     * NC: code
     * BV: buy vol
     * B: buy
     * S: sell
     * SV: sell volume
     * @return
     */
    public JSONObject getTradeData() {
        String urlStr = "http://sgx.com/JsonRead/JsonData?qryId=RStock&timeout=30&nocache=" + new Date().toString();
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = r.readLine()) != null)
            {
                sb.append(line);
            }
            JSONObject obj = new JSONObject(sb.substring(4));
            return obj;

        } catch(Exception e) {
            Logger.getGlobal().warning(e.toString());
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }
}

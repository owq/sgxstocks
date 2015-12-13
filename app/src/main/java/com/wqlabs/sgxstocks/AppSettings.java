package com.wqlabs.sgxstocks;

import android.content.Context;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by RadianceOng on 13-Dec-15.
 */
public class AppSettings {
    File dataFile;

    Context context;

    LinkedHashSet<String> stockCodes = new LinkedHashSet<>();

    PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public static final String stockCodeChange = "stockcode";

    public AppSettings(Context context) {
        this.context = context;
        dataFile = new File(context.getFilesDir(), "app.dat");
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName, listener);
    }

    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void load() {
        try {
            ObjectInputStream s = new ObjectInputStream(new FileInputStream(dataFile));
            stockCodes = (LinkedHashSet<String>) s.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            ObjectOutputStream s = new ObjectOutputStream(new FileOutputStream(dataFile));

            s.writeObject(stockCodes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fireStockCodeChange() {
        firePropertyChange(stockCodeChange, 0, 1);
    }

    public void addStockCode(String c) {
        stockCodes.add(c);
        save();
        fireStockCodeChange();
    }

    public void removeStockCode(String s) {
        stockCodes.remove(s);
        save();
        fireStockCodeChange();
    }

    public Collection<String> getStockCodes() {
        return stockCodes;
    }
}

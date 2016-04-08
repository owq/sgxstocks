package com.wqlabs.sgxstocks;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class TradeInfoActivity extends AppCompatActivity implements AddStockFragment.AddStockDialogListener {

    AppSettings settings;
    FilteredTradeViewModel tradeModel;

    Date lastUpdatedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_info_classic);

        ExpandableListView v = getTradeListView();
        registerForContextMenu(v);

        settings = new AppSettings(this);
        settings.load();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lastUpdatedDate = (Date)savedInstanceState.getSerializable("lastUpdatedDate");
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putSerializable("lastUpdatedDate", lastUpdatedDate);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_stock_item, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_remove:
                TradeInfoListAdapter adap = (TradeInfoListAdapter)getTradeListView().getExpandableListAdapter();
                int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
                SGXStockInfo stock = adap.getGroup(groupPos);
                removeCode(stock.getStockCode());
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    private ExpandableListView getTradeListView() {
        return (ExpandableListView)findViewById(R.id.expandableListView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        autoRefreshIfNeeded();
    }

    private void refresh() {
        final ExpandableListView v = getTradeListView();
        v.setGroupIndicator(null);

        getTopView().setText("Loading...");

        AsyncTask<Object,Object,TradeData> task = new AsyncTask<Object, Object, TradeData>() {

            @Override
            protected TradeData doInBackground(Object... params) {

                try {
                    return new TradeData();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(TradeData tradeData) {
                if(tradeData != null) {
                    loadTradeData(tradeData);
                    updateTradeListView();
                }
            }
        };

        task.execute();
    }

    private TextView getTopView() {
        return (TextView)findViewById(R.id.topView);
    }

    private void autoRefreshIfNeeded() {
        if(lastUpdatedDate != null) {
            Date current = new Date();
            if(current.getTime() - lastUpdatedDate.getTime() > 1000 * 60) {
                refresh();
            }
        }
        else {
            refresh();
        }
    }

    private void loadTradeData(TradeData tradeData) {
//        Snackbar.make(getTradeListView(), tradeData.getTimeStamp(), Snackbar.LENGTH_INDEFINITE).show();
        lastUpdatedDate = new Date();
        getTopView().setText(tradeData.getTimeStamp());
        this.tradeModel = new FilteredTradeViewModel(tradeData);
    }

    private void setFilter() {
        this.tradeModel.setFilter(settings.getStockCodes()); //TODO optimize.
    }

    private void updateTradeListView() {
        setFilter();
        //Store position first...
        Parcelable state = getTradeListView().onSaveInstanceState();
        getTradeListView().setAdapter(new TradeInfoListAdapter(TradeInfoActivity.this, tradeModel));
        getTradeListView().onRestoreInstanceState(state);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trade_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id) {
            case R.id.action_refresh:
                refresh();
                return true;
            case R.id.action_add_stock:
                AddStockFragment frag = new AddStockFragment();
                frag.setModel(new AddStockAutoCompleteAdapter(this, new ArrayList<>(tradeModel.getAllStocks())));
                frag.setValidator(new AutoCompleteTextView.Validator() {
                    @Override
                    public boolean isValid(CharSequence text) {
                        return tradeModel.hasCode(text.toString());
                    }

                    @Override
                    public CharSequence fixText(CharSequence invalidText) {
                        return invalidText;
                    }
                });
                frag.show(getFragmentManager(), "AddStockFragment");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean addCode(String code) {
        if(tradeModel.hasCode(code)) {
            settings.addStockCode(code);
            updateTradeListView();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeCode(String code) {
        if(tradeModel.hasCode(code)) {
            settings.removeStockCode(code);
            updateTradeListView();
            return true;
        } else {
            return false;
        }
    }
}

package com.wqlabs.sgxstocks;

import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.TreeSet;

public class TradeInfoActivity extends AppCompatActivity implements AddStockFragment.AddStockDialogListener {

    AppSettings settings;
    FilteredTradeViewModel tradeModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_info_classic);

        ExpandableListView v = getTradeListView();
        registerForContextMenu(v);

        settings = new AppSettings(this);
        settings.load();
        refresh();
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

    private void loadTradeData(TradeData tradeData) {
//        Snackbar.make(getTradeListView(), tradeData.getTimeStamp(), Snackbar.LENGTH_INDEFINITE).show();
        getTopView().setText(tradeData.getTimeStamp());
        this.tradeModel = new FilteredTradeViewModel(tradeData);
    }

    private void setFilter() {
        this.tradeModel.setFilter(new TreeSet<>(settings.getStockCodes())); //TODO optimize.
    }

    private void updateTradeListView() {
        setFilter();
        getTradeListView().setAdapter(new TradeInfoListAdapter(TradeInfoActivity.this, tradeModel));
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
                DialogFragment frag = new AddStockFragment();
                frag.show(getFragmentManager(), "AddStockFragment");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addCode(String code) {
        if(tradeModel.hasCode(code)) {
            settings.addStockCode(code);
            updateTradeListView();
        }
    }

    @Override
    public void removeCode(String code) {
        if(tradeModel.hasCode(code)) {
            settings.removeStockCode(code);
            updateTradeListView();
        }
    }
}

package com.wqlabs.sgxstocks;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class TradeInfoActivity extends AppCompatActivity implements AddStockFragment.AddStockDialogListener {

    AppSettings settings;
    TradeData tradeData;
    FilteredTradeViewModel tradeModel;
    RetainedFragment dataFragment;

    AsyncTask<Object,Object,TradeData> task;

    Date lastUpdatedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_info_grid);

//        ExpandableListView v = getTradeListView();
//        registerForContextMenu(v);
//        v.setGroupIndicator(null);

        settings = new AppSettings(this);
        settings.load();

        // find the retained fragment on activity restarts
        FragmentManager fm = getFragmentManager();
        dataFragment = (RetainedFragment) fm.findFragmentByTag("data");

        // create the fragment and data the first time
        if (dataFragment == null) {
            // add the fragment
            dataFragment = new RetainedFragment();
            fm.beginTransaction().add(dataFragment, "data").commit();
        }
        if(dataFragment.getData() != null) {
            loadTradeData(dataFragment.getData());
            updateTradeListView();
        }
        lastUpdatedDate = dataFragment.getLastUpdatedDate();

        //Init grid view
        getTradeGridView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SGXStockInfo info = (SGXStockInfo)parent.getItemAtPosition(position);
                new AlertDialog.Builder(TradeInfoActivity.this)
                        .setMessage(Html.fromHtml(new StockInfoHelper(info).asVerboseHTML())).show();
            }
        });
        getTradeGridView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final SGXStockInfo info = (SGXStockInfo)parent.getItemAtPosition(position);
                new AlertDialog.Builder(TradeInfoActivity.this)
                        .setMessage("Remove " + info.getStockCode())
                        .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeCode(info.getStockCode());
                            }
                        })
                        .show();
                return true;
            }
        });
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

    private GridView getTradeGridView() {
        return (GridView)findViewById(R.id.tradeGridView);
    }

    private GridView getMainView() {
        return getTradeGridView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        autoRefreshIfNeeded();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(task != null) task.cancel(true);
        dataFragment.setData(tradeData);
        dataFragment.setLastUpdatedDate(lastUpdatedDate);
    }

    private void refresh() {
        if(task != null) task.cancel(true);

//        final ExpandableListView v = getTradeListView();
//        v.setGroupIndicator(null);

        getTopView().setText("Loading...");

        task = new AsyncTask<Object, Object, TradeData>() {

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
                    lastUpdatedDate = new Date();
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
//                refresh();
            }
        }
        else {
            refresh();
        }
    }

    private void loadTradeData(TradeData tradeData) {
//        Snackbar.make(getTradeListView(), tradeData.getTimeStamp(), Snackbar.LENGTH_INDEFINITE).show();
        this.tradeData = tradeData;
        getTopView().setText(tradeData.getTimeStamp());
        this.tradeModel = new FilteredTradeViewModel(tradeData);
    }

    private void setFilter() {
        this.tradeModel.setFilter(settings.getStockCodes()); //TODO optimize.
    }

    private void updateTradeListView() {
        setFilter();
        //Store position first...
        Parcelable state = getMainView().onSaveInstanceState();
//        getMainView().setAdapter(new TradeInfoListAdapter(TradeInfoActivity.this, tradeModel));
        getMainView().setAdapter(new TradeInfoGridAdapter(TradeInfoActivity.this, tradeModel));
        getMainView().onRestoreInstanceState(state);
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

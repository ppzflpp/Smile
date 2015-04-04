package com.dragon.smile;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.dragon.smile.data.BusinessData;
import com.dragon.smile.fragment.POIFragment;
import com.dragon.smile.server.BusinessManager;

import java.util.ArrayList;


public class PoiSearchActivity extends ActionBarActivity implements UiCallback {

    private final static int MSG_UPDATE_LOCATION = 0;
    private final static int MSG_UPDATE_POI_RESULT = 1;
    private TextView mLocationView = null;
    private String mUserLocation = null;
    private ArrayList<BusinessData> mBusinessDataList = null;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_UPDATE_LOCATION:
                    mLocationView.setText(mUserLocation);
                    break;
                case MSG_UPDATE_POI_RESULT:
                    ((POIFragment) getFragmentManager().findFragmentById(R.id.poi_fragment)).setDataList(mBusinessDataList);
                    break;

            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_search);

        setupViews();

        BusinessManager.getInstance().init(this.getApplicationContext());
        BusinessManager.getInstance().registerCallback(this);
        BusinessManager.getInstance().start();
    }

    private void setupViews() {
        mLocationView = (TextView) findViewById(R.id.user_location);
        getFragmentManager().beginTransaction().replace(R.id.poi_fragment, new POIFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_poi_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUIRefresh(int type, Object data) {
        switch (type) {
            case UI_TYPE_LOCATION_STRING:
                mUserLocation = (String) data;
                mHandler.sendEmptyMessage(MSG_UPDATE_LOCATION);
                break;
            case UI_TYPE_LOCATION_POI_RESULT:
                mBusinessDataList = (ArrayList<BusinessData>) data;
                mHandler.sendEmptyMessage(MSG_UPDATE_POI_RESULT);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusinessManager.getInstance().stop();
    }
}

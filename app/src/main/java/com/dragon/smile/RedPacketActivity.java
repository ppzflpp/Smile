package com.dragon.smile;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dragon.smile.data.RedPacket;
import com.dragon.smile.utils.ApplicationUtils;


public class RedPacketActivity extends ActionBarActivity {

    private static final String TAG = "RedPacketActivity";

    private TextView mPacketSizeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_packet);

        if (Build.VERSION.SDK_INT >= 21) {
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.title_bg_color));
        }
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        View view = LayoutInflater.from(this).inflate(R.layout.title_bar, null);
        ((TextView) view.findViewById(R.id.action_bar_title_view)).setText(R.string.title_activity_red_packet);
        view.findViewById(R.id.action_bar_title_view_back).setVisibility(View.VISIBLE);
        view.findViewById(R.id.action_bar_title_view_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        getSupportActionBar().setCustomView(view, lp);

        findViewById(R.id.get_red_packet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doGetPacketAnimation();
                generatePacket();
            }
        });

        mPacketSizeView = (TextView) findViewById(R.id.red_packet_size);


    }

    private void doGetPacketAnimation() {

    }

    private void generatePacket() {
        if (canGetPacket()) {
            RedPacket rp = ((SmileApplication) this.getApplication()).mLoginUser.redPacket;
            rp.generateRedPacket(System.currentTimeMillis());

            String s = String.format(getString(R.string.get_red_packet), rp.getRedPacketSize());
            SpannableString msp = new SpannableString(s);
            msp.setSpan(new RelativeSizeSpan(1.5f), 7, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            msp.setSpan(new ForegroundColorSpan(Color.RED), 7, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mPacketSizeView.setText(msp);

            new Thread() {
                public void run() {
                    super.run();
                    ApplicationUtils.saveUserData(getApplicationContext(), ((SmileApplication) getApplication()).mLoginUser);
                }
            }.start();
        } else {
            Toast.makeText(this, R.string.can_not_get_red_packet, Toast.LENGTH_LONG).show();
        }
    }

    private boolean canGetPacket() {
        return ((SmileApplication) this.getApplication()).mLoginUser.redPacket.canGetRedPacket(System.currentTimeMillis());
    }


}

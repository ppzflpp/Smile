package com.dragon.smile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bmob.pay.tool.BmobPay;
import com.bmob.pay.tool.PayListener;
import com.dragon.smile.data.PayItem;
import com.dragon.smile.utils.LogUtils;


public class PayActivity extends ActionBarActivity {

    private final static String TAG = "PayActivity";

    private final static String APP_ID = "23bdc5c09d29d653c30fe3d5932fb525";

    private final static int PAY_METHOD_WEI_XIN = 0;
    private int mPayMethodId = PAY_METHOD_WEI_XIN;
    private final static int PAY_METHOD_ZHI_FU_BAO = 1;
    private String mPayMethod;
    ;
    private BmobPay mBmobPay;
    private ProgressDialog mPayDialog = null;

    private RadioGroup mPayMethodView;
    private Button mButtonPay;
    private TextView mServiceNameView;
    private TextView mServicePriceView;
    private TextView mServiceUserNameView;

    private PayItem mPayItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        BmobPay.init(this, APP_ID);
        mBmobPay = new BmobPay(this);
        mPayItem = (PayItem) getIntent().getSerializableExtra("pay_item");
        setupViews();
    }

    private void setupViews() {
        mPayMethodView = (RadioGroup) findViewById(R.id.pay_method);
        mPayMethodView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.pay_method_wei_xin:
                        mPayMethod = getString(R.string.method_wei_xin);
                        mPayMethodId = PAY_METHOD_WEI_XIN;
                        break;
                    case R.id.pay_method_zhi_fu_bao:
                        mPayMethod = getString(R.string.method_zhi_fu_bao);
                        mPayMethodId = PAY_METHOD_ZHI_FU_BAO;
                        break;

                }
            }
        });

        mButtonPay = (Button) findViewById(R.id.button_pay);
        mButtonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay(mPayMethodId);
            }
        });
        if (mPayItem == null) {
            mButtonPay.setEnabled(false);
        }

        mServiceUserNameView = (TextView) findViewById(R.id.user_name);
        mServiceNameView = (TextView) findViewById(R.id.service_name);
        mServicePriceView = (TextView) findViewById(R.id.order_money_amount);
        if (mPayItem != null) {
            mServiceUserNameView.setText(mPayItem.userName);
            mServiceNameView.setText(mPayItem.serviceName);
            mServicePriceView.setText(mPayItem.servicePrice);
        }
    }

    private void pay(int payMethod) {
        switch (payMethod) {
            case PAY_METHOD_WEI_XIN:
                payByWeiXin();
                break;
            case PAY_METHOD_ZHI_FU_BAO:
                payByAli();
                break;

        }
    }

    private double getPrice() {
        if (mPayItem != null)
            return Double.parseDouble(mPayItem.servicePrice);
        return 0;
    }

    private String getBody() {
        return mPayItem.serviceInfo;
    }

    private void showPayDialog() {
        if (mPayDialog == null) {
            mPayDialog = new ProgressDialog(this);
            mPayDialog.setTitle(R.string.paying);
        }

        if (!mPayDialog.isShowing()) {
            mPayDialog.show();
        }
    }

    private void hidePayDialog() {
        if (mPayDialog != null && !mPayDialog.isShowing()) {
            mPayDialog.dismiss();
            mPayDialog = null;
        }
    }


    // 调用微信支付
    void payByWeiXin() {

        showPayDialog();

        mBmobPay.payByWX(getPrice(), mPayMethod, getBody(), new PayListener() {
            @Override
            public void unknow() {
                hidePayDialog();
                Toast.makeText(PayActivity.this, "支付结果未知,请稍后手动查询", Toast.LENGTH_LONG).show();
            }

            // 支付成功,如果金额较大请手动查询确认
            @Override
            public void succeed() {
                hidePayDialog();
                Toast.makeText(PayActivity.this, "支付成功!", Toast.LENGTH_SHORT)
                        .show();
            }

            // 无论成功与否,返回订单号
            @Override
            public void orderId(String orderId) {
                // 此处应该保存订单号,比如保存进数据库等,以便以后查询
                LogUtils.d(TAG, "------------------------------orderId----------------orderId = " + orderId);
            }

            // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
            @Override
            public void fail(int code, String reason) {

                // 当code为-2,意味着用户中断了操作
                // code为-3意味着没有安装BmobPlugin插件
                if (code == -3) {
                    LogUtils.d(TAG, "------------------------------fail----------------reson = " + reason);
                } else {
                    Toast.makeText(PayActivity.this, "支付中断!",
                            Toast.LENGTH_SHORT).show();
                }

                hidePayDialog();
            }
        });
    }

    // 调用支付宝支付
    void payByAli() {
        showPayDialog();

        mBmobPay.pay(getPrice(), mPayMethod, getBody(), new PayListener() {

            // 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
            @Override
            public void unknow() {

                Toast.makeText(PayActivity.this, "支付结果未知,请稍后手动查询",
                        Toast.LENGTH_SHORT).show();
            }

            // 支付成功,如果金额较大请手动查询确认
            @Override
            public void succeed() {
                hidePayDialog();
                Toast.makeText(PayActivity.this, "支付成功!", Toast.LENGTH_SHORT)
                        .show();
            }

            // 无论成功与否,返回订单号
            @Override
            public void orderId(String orderId) {
                // 此处应该保存订单号,比如保存进数据库等,以便以后查询

            }

            // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
            @Override
            public void fail(int code, String reason) {
                hidePayDialog();
                Toast.makeText(PayActivity.this, "支付中断!", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

}

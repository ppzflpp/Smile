package com.dragon.smile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dragon.smile.data.LoginUser;
import com.dragon.smile.utils.LogUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends ActionBarActivity {

    private static final String TAG = "LoginActivity";

    private UserLoginTask mAuthTask = null;
    private boolean mLoginSuccess = false;
    private EditText mUserNameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginButton;
    private View mRegisterButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= 21) {
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.title_bg_color));
        }
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        View view = LayoutInflater.from(this).inflate(R.layout.title_bar, null);
        ((TextView) view.findViewById(R.id.action_bar_title_view)).setText(R.string.login);
        view.findViewById(R.id.action_bar_title_view_back).setVisibility(View.VISIBLE);
        view.findViewById(R.id.action_bar_title_view_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        getSupportActionBar().setCustomView(view, lp);

        mProgressView = findViewById(R.id.login_progress);

        mUserNameView = (EditText) findViewById(R.id.login_user_name);
        mPasswordView = (EditText) findViewById(R.id.login_user_password);

        mLoginButton = findViewById(R.id.login);
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserNameView.getText() != null && mPasswordView.getText() != null) {
                    attemptLogin();
                }
            }
        });

        findViewById(R.id.user_login_register).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.user_login_find_password).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FindPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        String userName = mUserNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        showProgress(true);
        mAuthTask = new UserLoginTask(userName, password);
        mAuthTask.execute((Void) null);
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginButton.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginButton.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginButton.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginButton.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUserName;
        private final String mPassword;

        UserLoginTask(String userName, String password) {
            mUserName = userName;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            final LoginUser user = new LoginUser();
            user.setUsername(mUserName);
            user.setPassword(mPassword);
            user.login(LoginActivity.this, new SaveListener() {
                @Override
                public void onSuccess() {
                    // TODO Auto-generated method stub
                    LogUtils.d(TAG, "login success");
                    ((SmileApplication) getApplication()).mIsLoginIn = true;
                    ((SmileApplication) getApplication()).mLoginUser = BmobUser.getCurrentUser(getApplicationContext(), LoginUser.class);
                    LogUtils.d(TAG, " size = " + ((SmileApplication) getApplication()).mLoginUser.redPacket.getRedPacketSize());
                    mLoginSuccess = true;
                }

                @Override
                public void onFailure(int code, String msg) {
                    // TODO Auto-generated method stub
                    LogUtils.d(TAG, "login fail,code = " + code + ",msg = " + msg);
                    mLoginSuccess = false;
                }
            });


            // TODO: register the new account here.
            return mLoginSuccess;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                Toast.makeText(LoginActivity.this, R.string.user_not_exist, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}




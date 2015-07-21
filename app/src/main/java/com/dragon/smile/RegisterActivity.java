package com.dragon.smile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dragon.smile.data.LoginUser;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends ActionBarActivity {

    private final static String TAG = "RegisterActivity";

    private UserRegisterTask mAuthTask = null;

    private EditText mPasswordView;
    private EditText mPhoneView;
    private EditText mEmailView;
    private View mProgressView;
    private View mRegisterButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
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

        mProgressView = findViewById(R.id.register_progress);

        mPasswordView = (EditText) findViewById(R.id.register_user_password);
        mPhoneView = (EditText) findViewById(R.id.register_user_phone);
        mEmailView = (EditText) findViewById(R.id.register_user_email);

        mRegisterButton = findViewById(R.id.register_button_register);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPhoneView.getText() != null && mPasswordView.getText() != null
                        && mEmailView.getText() != null) {
                    attemptRegister();
                }
            }
        });

    }

    public void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }

        String password = mPasswordView.getText().toString();
        String phone = null;
        if (mPhoneView.getText() != null)
            phone = mPhoneView.getText().toString();
        String email = null;
        if (mEmailView.getText() != null)
            email = mEmailView.getText().toString();

        showProgress(true);
        mAuthTask = new UserRegisterTask(phone, password, phone, email);
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

            mRegisterButton.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterButton.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterButton.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mRegisterButton.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUserName;
        private final String mPassword;
        private final String mEmail;
        private final String mPhone;

        UserRegisterTask(String userName, String password, String phone, String email) {
            mUserName = userName;
            mEmail = email;
            mPassword = password;
            mPhone = phone;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            final LoginUser user = new LoginUser();
            user.setUsername(mUserName);
            user.setPassword(mPassword);
            user.setEmail(mEmail);
            user.phone = mPhone;

            user.signUp(RegisterActivity.this, new SaveListener() {
                @Override
                public void onSuccess() {
                    SharedPreferences sp = RegisterActivity.this.getSharedPreferences(SmileSharedPreference.FILE_NAME, 0);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString(SmileSharedPreference.FIELD_USER_NAME, mUserName);
                    edit.commit();
                    ((SmileApplication) getApplication()).mIsLoginIn = true;
                    ((SmileApplication) getApplication()).mLoginUser = BmobUser.getCurrentUser(getApplicationContext(), LoginUser.class);
                }

                @Override
                public void onFailure(int code, String msg) {
                    Log.d("TAG", "msg = " + msg);
                    Toast.makeText(RegisterActivity.this, R.string.register_fail, Toast.LENGTH_SHORT).show();
                }
            });


            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {

            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}




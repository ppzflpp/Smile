package com.dragon.smile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.dragon.smile.data.LoginUser;
import com.dragon.smile.utils.LogUtils;

import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends Activity {

    private final static String TAG = "RegisterActivity";

    private UserRegisterTask mAuthTask = null;

    private EditText mUserNameView;
    private EditText mPasswordView;
    private EditText mPhoneView;
    private EditText mEmailView;
    private View mProgressView;
    private View mRegisterButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        mProgressView = findViewById(R.id.register_progress);

        mUserNameView = (EditText) findViewById(R.id.register_user_name);
        mPasswordView = (EditText) findViewById(R.id.register_user_password);
        mPhoneView = (EditText) findViewById(R.id.register_user_phone);
        mEmailView = (EditText) findViewById(R.id.register_user_email);

        mRegisterButton = findViewById(R.id.register_button_register);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserNameView.getText() != null && mPasswordView.getText() != null) {
                    attemptRegister();
                }
            }
        });

    }

    public void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }

        String userName = mUserNameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String phone = null;
        if (mPhoneView.getText() != null)
            phone = mPhoneView.getText().toString();
        String email = null;
        if (mEmailView.getText() != null)
            email = mEmailView.getText().toString();

        showProgress(true);
        mAuthTask = new UserRegisterTask(userName, password, phone, email);
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

            LoginUser user = new LoginUser();
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
                }

                @Override
                public void onFailure(int code, String msg) {
                    LogUtils.d(TAG, "------------------------------------------register fail,code = " + code + ",msg = " + msg);
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




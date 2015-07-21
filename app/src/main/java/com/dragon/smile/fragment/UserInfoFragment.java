package com.dragon.smile.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dragon.smile.FindPasswordActivity;
import com.dragon.smile.LoginActivity;
import com.dragon.smile.R;
import com.dragon.smile.RegisterActivity;
import com.dragon.smile.SmileApplication;

import cn.bmob.v3.BmobUser;


public class UserInfoFragment extends Fragment {

    private final static String TAG = "UserInfoFragment";

    private static UserInfoFragment sInstance;

    private TextView mUserNameView;
    private TextView mUserIconView;
    private Button mLoginView;
    private TextView mRedPacketView;
    private View mRedPacketViewParent;

    public UserInfoFragment() {
    }

    // TODO: Rename and change types of parameters
    public static UserInfoFragment newInstance(String param1, String param2) {
        if (sInstance != null)
            return sInstance;

        UserInfoFragment fragment = new UserInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        sInstance = fragment;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        boolean isLogin = ((SmileApplication) getActivity().getApplication()).mIsLoginIn;
        updateViewState(isLogin);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.user_info, null);
        mUserNameView = (TextView) view.findViewById(R.id.user_info_user_name);
        mUserIconView = (TextView) view.findViewById(R.id.user_info_user_icon);
        mLoginView = (Button) view.findViewById(R.id.user_info_login_out);
        mLoginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLogin = ((SmileApplication) getActivity().getApplication()).mIsLoginIn;
                if (isLogin) {
                    BmobUser.logOut(getActivity().getApplicationContext());
                    ((SmileApplication) getActivity().getApplication()).mIsLoginIn = false;
                    ((SmileApplication) getActivity().getApplication()).mLoginUser = null;
                    updateViewState(false);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });

        view.findViewById(R.id.user_info_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                getActivity().startActivity(intent);
            }
        });

        view.findViewById(R.id.user_info_find_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FindPasswordActivity.class);
                getActivity().startActivity(intent);
            }
        });

        mRedPacketViewParent = view.findViewById(R.id.my_red_packet_parent);
        mRedPacketView = (TextView) view.findViewById(R.id.my_red_packet);

        return view;
    }

    private void updateViewState(boolean loginIn) {
        if (!loginIn) {
            mUserNameView.setText(R.string.un_login);
            mLoginView.setText(R.string.login);
            mUserIconView.setBackgroundResource(R.drawable.ic_user_unlogin);
            mRedPacketViewParent.setVisibility(View.GONE);
        } else {
            mUserNameView.setText(((SmileApplication) getActivity().getApplication()).mLoginUser.getUsername());
            mUserIconView.setBackgroundResource(R.drawable.ic_user_login);
            mLoginView.setText(R.string.login_out);
            mRedPacketViewParent.setVisibility(View.VISIBLE);
            mRedPacketView.setText(getString(R.string.renminbi) + ((SmileApplication) getActivity().getApplication()).mLoginUser.redPacket.getRedPacketSize());
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}

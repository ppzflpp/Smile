package com.dragon.smile.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dragon.smile.LoginActivity;
import com.dragon.smile.R;
import com.dragon.smile.SmileSharedPreference;


public class UserInfoFragment extends Fragment {

    public UserInfoFragment() {
    }

    // TODO: Rename and change types of parameters
    public static UserInfoFragment newInstance(String param1, String param2) {
        UserInfoFragment fragment = new UserInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.user_info, null);
        TextView textView = (TextView) view.findViewById(R.id.user_info_user_name);

        SharedPreferences sp = this.getActivity().getSharedPreferences(SmileSharedPreference.FILE_NAME, 0);
        String userName = sp.getString(SmileSharedPreference.FIELD_USER_NAME, null);
        if (userName == null) {
            //no user had login,start login activity
            Intent intent = new Intent(this.getActivity(), LoginActivity.class);
            this.getActivity().startActivity(intent);
        } else {
            textView.setText(userName);
        }
        return view;
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

package com.dragon.smile.fragment;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dragon.smile.R;


public class AboutFragment extends Fragment {

    private final static String TAG = "AboutFragment";
    private static AboutFragment sInstance;

    private boolean mAttached = false;

    public AboutFragment() {
    }

    // TODO: Rename and change types of parameters
    public static AboutFragment newInstance(String param1, String param2) {
        if (sInstance != null)
            return sInstance;
        AboutFragment fragment = new AboutFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.about, null);
        View aboutContentParentView = view.findViewById(R.id.about_us_parent);
        final TextView aboutContentView = (TextView) view.findViewById(R.id.about_us_content);
        aboutContentParentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visibility = aboutContentView.getVisibility() == View.VISIBLE ? View.GONE :
                        View.VISIBLE;
                aboutContentView.setVisibility(visibility);
            }
        });

        View softwareUpdateParentView = view.findViewById(R.id.software_update_parent);
        TextView softwareUpdateVersionView = (TextView) view.findViewById(R.id.software_update_version);
        PackageManager manager = this.getActivity().getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(this.getActivity().getPackageName(), 0);
            softwareUpdateVersionView.setText(info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            softwareUpdateVersionView.setText(R.string.unknow_version);
        }
        softwareUpdateParentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO:check software update
            }
        });

        View callUsView = view.findViewById(R.id.call_us);
        callUsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:call us
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAttached = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mAttached = false;
    }
}

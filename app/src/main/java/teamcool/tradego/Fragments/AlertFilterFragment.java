package teamcool.tradego.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import teamcool.tradego.R;

public class AlertFilterFragment extends Fragment {

    String query;

    public AlertFilterFragment() {

    }

    public static AlertFilterFragment newInstance(String query) {
        AlertFilterFragment fragment = new AlertFilterFragment();
        Bundle args = new Bundle();
        args.putString("query", query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            query = getArguments().getString("query");
        }
    }

}

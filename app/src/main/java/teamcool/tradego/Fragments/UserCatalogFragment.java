package teamcool.tradego.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import teamcool.tradego.R;

/**
 * Created by kshah97 on 7/8/16.
 */
public class UserCatalogFragment extends CatalogListFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateTimeLine();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog_list, container, false);
        ButterKnife.bind(this,view);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //if swipe container exists, must setOnRefreshListener here, not onCreateView or onCreate
        super.onViewCreated(view, savedInstanceState);
    }

    public void populateTimeLine() {
        return; //placeholder
    }

}

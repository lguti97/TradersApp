package teamcool.tradego.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseUser;

import java.util.List;

import butterknife.ButterKnife;
import teamcool.tradego.Clients.ParseClient;
import teamcool.tradego.Models.Item;
import teamcool.tradego.R;


//import teamcool.tradego.ParseClient;

/**
 * Created by kshah97 on 7/8/16.
 */
public class UserCatalogFragment extends CatalogListFragment {

    List<Item> items;
    ParseClient parseClient;

    public UserCatalogFragment() {

    }

    public static UserCatalogFragment newInstance(String objId, String status) {
        UserCatalogFragment frag = new UserCatalogFragment();
        Bundle args = new Bundle();
        args.putString("status",status);
        args.putString("id",objId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog_list, container, false);
        ButterKnife.bind(this, view);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //if swipe container exists, must setOnRefreshListener here, not onCreateView or onCreate
        super.onViewCreated(view, savedInstanceState);
        parseClient = new ParseClient();
        //populateCatalog(getArguments().getString("objId"));
    }

    public void populateCatalog(String id, String status) {

        //parseClient.queryItemsInDatabaseOnUser(ParseUser.getCurrentUser()).clear();
        items = parseClient.queryAvailableItemsInDatabaseOnUser(ParseUser.getCurrentUser());

        addAll(items);

    }
}

package teamcool.tradego.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
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

    @BindView(R.id.swipeContainerCatalog) SwipeRefreshLayout swipeContainer;

    public UserCatalogFragment() {

    }


    public static UserCatalogFragment newInstance(String objId, String status) {
        UserCatalogFragment frag = new UserCatalogFragment();
        Bundle args = new Bundle();
        args.putString("status", status);
        args.putString("id", objId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseClient = new ParseClient();
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
        populateCatalog(getArguments().getString("id"), getArguments().getString("status"));
        //if swipe container exists, must setOnRefreshListener here, not onCreateView or onCreate
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateCatalog(getArguments().getString("id"), getArguments().getString("status"));
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        super.onViewCreated(view, savedInstanceState);
    }


    public void populateCatalog(String id, String status) {
        ParseUser targetedUser = parseClient.queryUserBasedonObjectID(id);
        if (status.equalsIgnoreCase("Available")) {
            items = parseClient.queryItemsOnUserAndStatus(targetedUser, "Available");
            Log.d("DEBUG", items.size() + "<------ size");
        } else if (status.equalsIgnoreCase("On hold")) {
            items = parseClient.queryItemsOnUserAndStatus(targetedUser, "on hold");
        } else if (status.equalsIgnoreCase("Sold")) {
            items = parseClient.queryItemsOnUserAndStatus(targetedUser, "Sold");
        } else if (status.equalsIgnoreCase("Bought")) {
            //what the current user has bought
            items = parseClient.queryBoughtItemsOnUser(targetedUser);
        } else {
            items = new ArrayList<>();
        }

        addAll(items);
        swipeContainer.setRefreshing(false);

    }
}

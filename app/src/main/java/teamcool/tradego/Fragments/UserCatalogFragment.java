package teamcool.tradego.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseUser;

import java.util.ArrayList;
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

    //boolean self: is the items query against the user itself
    // or is the query against other users
    // if self, then it's the user inquiring about its own items
    // else, it's the user inquiring about other peoples items
    public static UserCatalogFragment newInstance(String objId, String status, boolean self) {
        UserCatalogFragment frag = new UserCatalogFragment();
        Bundle args = new Bundle();
        args.putString("status",status);
        args.putString("id",objId);
        args.putBoolean("self",self);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseClient = new ParseClient();
        populateCatalog(getArguments().getString("id"),getArguments().getString("status"),getArguments().getBoolean("self"));
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
    }

    public void populateCatalog(String id, String status, boolean self) {
        if (!self) {
            if (status.equalsIgnoreCase("Available")) {
                items = parseClient.queryAvailableItemsInDatabaseOnUser(ParseUser.getCurrentUser());
                Log.d("DEBUG", items.size() + "<------ size");
            } else if (status.equalsIgnoreCase("On hold")) {
                items = parseClient.queryOnholdItemsInDatabaseOnUser(ParseUser.getCurrentUser());
            } else if (status.equalsIgnoreCase("Sold")) {
                items = parseClient.querySoldItemsInDatabaseOnUser(ParseUser.getCurrentUser());
            } else {
                items = new ArrayList<>();
            }
        } else {
            ParseUser currUser = ParseUser.getCurrentUser();
            if (status.equalsIgnoreCase("Sold")) {
                //what the current user has sold
                items = parseClient.querySoldItemsInDatabaseOnUser(currUser);
            } else if (status.equalsIgnoreCase("Bought")) {
                //what the current user has bought
                items = parseClient.queryBoughtItemsInDatabaseOnUser(currUser);
            } else if (status.equalsIgnoreCase("On hold")) {
                //what the current user has on hold
                //items = parseClient.queryOnholdItemsInDatabaseOnUser(currUser);
            } else {
                items = new ArrayList<>();
            }
        }
        addAll(items);

    }
}

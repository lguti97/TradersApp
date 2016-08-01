package teamcool.tradego.Fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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
    boolean isViewCreated = false;
    boolean isSeen = false;
    boolean isLoaded = false;
    boolean isRefresh = false;

    @BindView(R.id.swipeContainerCatalog) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.ivNoItems) ImageView ivNoItems;

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


    private class AsyncDataLoading extends AsyncTask<String,Void,List<Item>> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            if (!isRefresh) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Loading");
                progressDialog.setMessage("Please wait...");
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
            }
            super.onPreExecute();
        }

        @Override
        protected List<Item> doInBackground(String... strings) {
            ParseClient parseClient = new ParseClient();
            String id = strings[0];
            String status = strings[1];
            ParseUser targetedUser = parseClient.queryUserBasedonObjectID(id);
            if (status.equalsIgnoreCase("Available")) {
                items = parseClient.queryItemsOnUserAndStatus(targetedUser, "Available");
            } else if (status.equalsIgnoreCase("On hold")) {
                items = parseClient.queryItemsOnUserAndStatus(targetedUser, "On hold");
            } else if (status.equalsIgnoreCase("Sold")) {
                items = parseClient.queryItemsOnUserAndStatus(targetedUser, "Sold");
            } else if (status.equalsIgnoreCase("Bought")) {
                //what the current user has bought
                items = parseClient.queryBoughtItemsOnUser(targetedUser);
            } else {
                items = new ArrayList<>();
            }
            return items;
        }

        @Override
        protected void onPostExecute(List<Item> items) {
            addAll(items);
            swipeContainer.setRefreshing(false);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            isRefresh = false;
            if(items.size() == 0) {
                Glide.with(getContext()).load(R.drawable.placeholder_transparent).into(ivNoItems);
            }
        }
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
        isViewCreated = true;
        if (isSeen && !isLoaded) {
            populateCatalog(getArguments().getString("id"), getArguments().getString("status"));
        }
        //if swipe container exists, must setOnRefreshListener here, not onCreateView or onCreate
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                populateCatalog(getArguments().getString("id"), getArguments().getString("status"));
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
            isSeen = true;
        if (isViewCreated && !isLoaded) {
            populateCatalog(getArguments().getString("id"), getArguments().getString("status"));
        }
    }

    public void populateCatalog(String id, String status) {
        isLoaded = true;
        String args[] = {id,status};
        new AsyncDataLoading().execute(args);
    }

}

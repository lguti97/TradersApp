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


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import teamcool.tradego.Clients.ParseClient;
import teamcool.tradego.Models.Item;
import teamcool.tradego.R;

public class SearchItemsFragment extends CatalogListFragment {

    List<Item> items;
    boolean isRefresh = false;

    @BindView(R.id.swipeContainerCatalog) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.ivNoItems) ImageView ivNoItems;

    private class AsyncDataLoading extends AsyncTask<Void,Void,List<Item>> {

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
        protected List<Item> doInBackground(Void... voids) {
            ParseClient parseClient = new ParseClient();
            String query = getArguments().getString("query");
            if(getArguments().getBoolean("hasFilter")) {
                String category = getArguments().getString("category");
                String sort = getArguments().getString("sort");
                items = parseClient.queryItemsOnFilteredQuery(query,category,sort,null);
            } else {
                items = parseClient.queryItemsOnName(query, false);
            }
            return items;
        }

        @Override
        protected void onPostExecute(List<Item> items) {
            addAll(items);
            swipeContainer.setRefreshing(false);
            isRefresh = false;
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (items.size() == 0) {
                Picasso.with(getContext()).load(R.drawable.placeholder_transparent).into(ivNoItems);
            }
            super.onPostExecute(items);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        populate();
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                populate();
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        super.onViewCreated(view, savedInstanceState);
        super.onViewCreated(view, savedInstanceState);
    }

    public static SearchItemsFragment newInstance(String query, ArrayList<String> filters) {
        SearchItemsFragment searchItemsFragment = new SearchItemsFragment();
        Bundle args = new Bundle();
        args.putString("query",query);
        args.putBoolean("hasFilter", filters!=null);
        if (filters != null) {
            args.putString("category",filters.get(0));
            args.putString("sort",filters.get(1));
        }
        searchItemsFragment.setArguments(args);
        return searchItemsFragment;
    }

    private void populate() {
        Log.d("DEBUG","-----------");
        new AsyncDataLoading().execute();
    }

}

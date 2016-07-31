package teamcool.tradego.Fragments;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import teamcool.tradego.Adapters.FriendsAdapter;
import teamcool.tradego.Clients.ParseClient;
import teamcool.tradego.Models.Friend;
import teamcool.tradego.R;

/**
 * Created by kshah97 on 7/8/16.
 */
public class FriendsListFragment extends Fragment {

    @BindView(R.id.rvFriends) RecyclerView rvFriends;
    @BindView(R.id.ivNoFriends) ImageView ivNoFriends;
    @BindView(R.id.swipeContainerFriends) SwipeRefreshLayout swipeContainer;

    List<Friend> friends = new ArrayList<>();
    private FriendsAdapter friendsAdapter;
    boolean isRefresh = false;

    private class AsyncDataLoading extends AsyncTask<Void,Void,List<Friend>> {

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
        protected List<Friend> doInBackground(Void... voids) {
            friends = new ArrayList<>();
            ParseClient parseClient = new ParseClient();
            if (getArguments() == null) {
                friends = parseClient.queryFriendsOnName(null);
            } else {
                friends = parseClient.queryFriendsOnName(getArguments().getString("query"));
            }
            return friends;
        }

        @Override
        protected void onPostExecute(List<Friend> friends) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            swipeContainer.setRefreshing(false);
            if (friends.size() == 0) {
                Glide.with(getContext()).load(R.drawable.placeholder_transparent).into(ivNoFriends);
            }
            friendsAdapter.clearAndAddAll(friends);
            super.onPostExecute(friends);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friends, container, false);
        ButterKnife.bind(this, v);
        rvFriends.setLayoutManager(new GridLayoutManager(getContext(),2));
        rvFriends.setAdapter(friendsAdapter);
        rvFriends.setHasFixedSize(true);
        rvFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Launch ProfileActivity
            }
        });
        //set listView's onItemLongClickListener, onItemClickListener, etc.
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friendsAdapter = new FriendsAdapter(friends);
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
        super.onViewCreated(view, savedInstanceState);

    }

    public static FriendsListFragment newInstance(String query, ArrayList<String> filters) {
        FriendsListFragment friendsListFragment = new FriendsListFragment();
        Bundle args = new Bundle();
        args.putString("query",query);
        if (filters != null) {
            //put more
        }
        friendsListFragment.setArguments(args);

        return friendsListFragment;
    }

    @Override
    public void onResume() {
        if(getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
        rvFriends.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
    } else {
        rvFriends.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
    }
        friendsAdapter.notifyDataSetChanged();
        super.onResume();
    }

    public void populate() {
        new AsyncDataLoading().execute();
    }
}
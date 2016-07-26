package teamcool.tradego.Fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    List<Friend> friends = new ArrayList<>();
    private FriendsAdapter friendsAdapter;

    private class AsyncDataLoading extends AsyncTask<Void,Void,List<Friend>> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
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
            progressDialog.dismiss();
            if (friends.size() == 0) {
                Picasso.with(getContext()).load(R.drawable.placeholder_transparent).into(ivNoFriends);
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


        //rvFriends.addOnScrollLisnener for endless scrolling
        //swipeContainer set on refresh listener
        //swipeContainer setColorSchemeResources to configure refreshing colors
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        populate();
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

    public void populate() {
        new AsyncDataLoading().execute();
    }
}
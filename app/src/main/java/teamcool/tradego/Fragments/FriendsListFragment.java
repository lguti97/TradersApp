package teamcool.tradego.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import teamcool.tradego.Adapters.FriendsAdapter;
import teamcool.tradego.Clients.ParseClient;
import teamcool.tradego.Models.Friend;
import teamcool.tradego.Models.Item;
import teamcool.tradego.R;

/**
 * Created by kshah97 on 7/8/16.
 */
public class FriendsListFragment extends Fragment {

    @BindView(R.id.rvFriends)
    RecyclerView rvFriends;
    //BindView to swipContainer SwipeRefreshLayout, toolbar

    private FriendsAdapter friendsAdapter;
    List<Friend> friends;
    ParseClient parseClient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friends, container, false);
        ButterKnife.bind(this, v);
        rvFriends.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        rvFriends.setAdapter(friendsAdapter);
        rvFriends.setHasFixedSize(true);
        //listViewHere.setAdapter(friendsAdapter);
        //set listView's onItemLongClickListener, onItemClickListener, etc.
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friends = new ArrayList();
        parseClient = new ParseClient();
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

    public void populate() {
        //swipeContainer.setRefreshing(false);
        friends = parseClient.queryFriendsOnName(null);
        friendsAdapter.clearAndAddAll(friends);

    }
}

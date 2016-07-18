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

import butterknife.BindView;
import butterknife.ButterKnife;
import teamcool.tradego.Adapters.FriendsAdapter;
import teamcool.tradego.R;

/**
 * Created by kshah97 on 7/8/16.
 */
public class FriendsListFragment extends Fragment {

    @BindView(R.id.rvFriends)
    RecyclerView rvFriends;
    //BindView to swipContainer SwipeRefreshLayout, toolbar

    private FriendsAdapter friendsAdapter;
    private ArrayList<ParseUser> friends;

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
        friendsAdapter = new FriendsAdapter(friends);


        //rvFriends.addOnScrollLisnener for endless scrolling
        //swipeContainer set on refresh listener
        //swipeContainer setColorSchemeResources to configure refreshing colors

        populate();
    }

    public void populate() {
        //swipeContainer.setRefreshing(false);
    }
}

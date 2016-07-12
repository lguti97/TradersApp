package teamcool.tradego.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import teamcool.tradego.R;
import teamcool.tradego.Models.User;

/**
 * Created by kshah97 on 7/8/16.
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //butterknife binding goes here:
        //@BindView(R.id.someId) Type someId;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            //set item's onClickListener to (this);
        }
    }

    private List<User> friends;

    public FriendsAdapter(List<User> friends) {
        this.friends = friends;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View friendsView = inflater.inflate(R.layout.friend_each, parent, false);
        ViewHolder viewHolder = new ViewHolder(friendsView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User friend = friends.get(position);
        //populate each item by setting its text and media
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public void clear() {
        friends.clear();
        notifyDataSetChanged();
    }

    public void clearAndAddAll(List<User> newFriends) {
        friends.addAll(newFriends);
        notifyDataSetChanged();
    }
}

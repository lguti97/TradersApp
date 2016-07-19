package teamcool.tradego.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import teamcool.tradego.Models.Friend;
import teamcool.tradego.R;

/**
 * Created by kshah97 on 7/8/16.
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //butterknife binding goes here:
        //@BindView(R.id.someId) Type someId;
        @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
        @BindView(R.id.tvUserName) TextView tvUsername;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            //set item's onClickListener to (this);
        }
    }

    private List<Friend> friends;

    public FriendsAdapter(List<Friend> friends) {
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
        Friend friend = friends.get(position);
        //populate each item by setting its text and media
        holder.tvUsername.setText(friend.getString("username"));
        holder.ivProfileImage.setImageResource(0);
//        Picasso.with(getContext()).load(friend.getString("profilePicUrl"))
//                .fit().centerInside()
//                .transform(new RoundedCornersTransformation(10, 10))
//                .into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public void clear() {
        friends.clear();
        notifyDataSetChanged();
    }

    public void clearAndAddAll(List<Friend> newFriends) {
        friends.addAll(newFriends);
        notifyDataSetChanged();
    }
}

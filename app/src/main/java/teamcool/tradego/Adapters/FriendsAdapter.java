package teamcool.tradego.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.parse.ParseUser;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import teamcool.tradego.Activities.ProfileActivity;
import teamcool.tradego.Clients.ParseClient;
import teamcool.tradego.Models.Friend;
import teamcool.tradego.Models.Item;
import teamcool.tradego.R;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {


    public static class ViewHolder extends RecyclerView.ViewHolder {


        //butterknife binding goes here:
        //@BindView(R.id.someId) Type someId;
        @BindView(R.id.ivProfile) ImageView ivProfile;
        @BindView(R.id.tvName) TextView tvName;
        @BindView(R.id.tvItems) TextView tvItems;
        View rootView;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            rootView = itemView;
        }
    }

    private List<Friend> friends;
    ParseClient parseClient;

    public FriendsAdapter(List<Friend> friends) {
        this.friends = friends;
    }

    Context context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View friendsView = inflater.inflate(R.layout.acquaintance_contact, parent, false);
        ViewHolder viewHolder = new ViewHolder(friendsView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Friend friend = friends.get(position);
        //populate each item by setting its text and media

        holder.tvName.setText(friend.getString("name"));
        holder.ivProfile.setImageResource(0);
        Glide.with(context)
                .load(friend.getProfile_url())
                .bitmapTransform(new CropCircleTransformation(context))
                .into(holder.ivProfile);

        parseClient = new ParseClient();
        final ParseUser friend_to_user = parseClient.queryUserBasedonFBid(friend.getUserID());
        parseClient.countNumItemsOnUser(friend_to_user, holder.tvItems ,"Items in Catalog: ", "");

        //Goes into detail mode for each friend.
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ProfileActivity.class);
                i.putExtra("objectId", friend_to_user.getObjectId());
                context.startActivity(i);
            }
        });
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
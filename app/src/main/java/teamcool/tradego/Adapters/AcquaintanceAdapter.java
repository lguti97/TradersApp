package teamcool.tradego.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import teamcool.tradego.Activities.DetailsActivity;
import teamcool.tradego.Clients.ParseClient;
import teamcool.tradego.ItemTouchHelperAdapter;
import teamcool.tradego.Models.Acquaintance;
import teamcool.tradego.Models.Friend;
import teamcool.tradego.R;
import teamcool.tradego.SimpleItemTouchHelperCallback;


/**
 * Created by lguti on 7/12/16.
 */
public class AcquaintanceAdapter extends RecyclerView.Adapter<AcquaintanceAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private ArrayList<Acquaintance> acquaintances;
    private Context context;
    private ParseClient parseclient;
    private Acquaintance acquaintance;
    private RecyclerView rvAcquaintances;
    private Friend friend;
    private Friend friendData;

    //ViewHolder gives access to our views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivProfile;
        View rootView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            ivProfile = (ImageView) itemView.findViewById(R.id.ivProfile);
            rootView = itemView;
        }

    }


    //Pass in acquaintance array into the adapter
    public AcquaintanceAdapter(Context context, ArrayList<Acquaintance> acquaintances, RecyclerView rvAcquaintances){
        this.context = context;
        this.acquaintances = acquaintances;
        this.rvAcquaintances = rvAcquaintances;

    }

    private Context getContext(){
        return context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //Inflate the custom layout
        View acquaintanceView = inflater.inflate(R.layout.acquaintance_contact, parent, false);

        //Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(acquaintanceView);
        return viewHolder;
    }

    // Populates data into acquaintance through this holder
    // Best to set the onAdd Here because position is already available
    @Override
    public void onBindViewHolder(AcquaintanceAdapter.ViewHolder holder, final int position) {
        //Let's see it can populate only based on DataBase Acquaintance
        parseclient = new ParseClient();
        //final Acquaintance acquaintance = parseclient.queryAcquaintancesofUser(ParseUser.getCurrentUser()).get(position);
        //Looks at each acquaintance in the ArrayList
        acquaintance = acquaintances.get(position);
        holder.tvName.setText(acquaintance.getName());
        holder.ivProfile.setImageResource(0);
        Glide.with(context)
                .load(acquaintance.getProfile_url())
                .bitmapTransform(new CropCircleTransformation(context))
                .into(holder.ivProfile);

    }


    @Override
    public int getItemCount() {
        return acquaintances.size();
    }



    //For the swiping and moving
    public void onItemDismiss(int position) {
        //Deleting acquaintance and then creating Friend object
        Log.d("DEBUG", "You swiped on position:" + position);
        acquaintance = acquaintances.get(position);
        final String idAcquaintance = acquaintance.getUserID();

        //Check if friend object is already in there
        friendData = parseclient.queryFriendOfUser(ParseUser.getCurrentUser(), idAcquaintance);
        if (friendData.getUserID().equals(idAcquaintance)) {
            //delete friend in database and instantiate the friend object again
            friendData.deleteInBackground();
            notifyDataSetChanged();
            friend = Friend.fromAcquaintance(acquaintance.getName(), acquaintance.getProfile_url(), idAcquaintance);
        }
        else {
            friend = Friend.fromAcquaintance(acquaintance.getName(), acquaintance.getProfile_url(), idAcquaintance);
        }
        Toast.makeText(getContext(), "You added " + friend.getName(), Toast.LENGTH_SHORT).show();
        acquaintances.remove(position);
        acquaintance.deleteInBackground();
        notifyItemRemoved(position);
        notifyDataSetChanged();

        //Gives user ability to remove from Friends and add the other user back to his/her acquaintance
        Snackbar.make(rvAcquaintances, "Friend added!", Snackbar.LENGTH_LONG)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Make parseObject again then notify adapter
                        //Queries for friend object just added
                        friend = parseclient.queryFriendOfUser(ParseUser.getCurrentUser(), idAcquaintance);
                        //Maps information to make Acquintance ParseObject
                        acquaintance = Acquaintance.fromFriend(friend.getName(), friend.getProfile_url(), idAcquaintance);
                        acquaintances.add(0, acquaintance);
                        //finally deletes object.
                        friend.deleteInBackground();
                        notifyItemInserted(0);
                        rvAcquaintances.scrollToPosition(0);


                    }
                })
                .setDuration(3000).show();

    }


    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(acquaintances, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(acquaintances, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }



}

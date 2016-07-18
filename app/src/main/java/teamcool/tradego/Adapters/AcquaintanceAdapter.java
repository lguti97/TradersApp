package teamcool.tradego.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import teamcool.tradego.Activities.DetailsActivity;
import teamcool.tradego.Clients.ParseClient;
import teamcool.tradego.Models.Acquaintance;
import teamcool.tradego.Models.Friend;
import teamcool.tradego.R;

/**
 * Created by lguti on 7/12/16.
 */
public class AcquaintanceAdapter extends RecyclerView.Adapter<AcquaintanceAdapter.ViewHolder> {
    private ArrayList<Acquaintance> acquaintances;
    private Context context;
    private ParseUser parseuser;
    private ParseClient parseclient;
    //ViewHolder gives access to our views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivProfile;
        ImageButton btnAdd;
        View rootView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            ivProfile = (ImageView) itemView.findViewById(R.id.ivProfile);
            btnAdd = (ImageButton) itemView.findViewById(R.id.btnAdd);
            rootView = itemView;
        }

    }

    //Pass in acquaintance array into the adapter
    public AcquaintanceAdapter(Context context, ArrayList<Acquaintance> acquaintances){
        this.context = context;
        this.acquaintances = acquaintances;
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
    public void onBindViewHolder(AcquaintanceAdapter.ViewHolder holder, int position) {
        //Let's see it can populate only based on DataBase Acquaintance

        final Acquaintance acquaintance = acquaintances.get(position);
        holder.tvName.setText(acquaintance.getName());
        holder.ivProfile.setImageResource(0);
        Glide.with(context)
                .load(acquaintance.getProfile_url())
                .into(holder.ivProfile);
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO. Query ParseUser by Name for now.
                //Transfer Data to FriendsObject so we can query from there.
                Friend friend;
                friend = Friend.fromAcquaintance(acquaintance.getName(), acquaintance.getProfile_url(), acquaintance.getUserID());
                Toast.makeText(getContext(), "You added" + friend.getName(), Toast.LENGTH_SHORT).show();


            }
        });




    }


    @Override
    public int getItemCount() {
        return acquaintances.size();
    }
}

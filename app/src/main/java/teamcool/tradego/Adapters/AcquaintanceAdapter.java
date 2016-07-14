package teamcool.tradego.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import teamcool.tradego.Models.Acquaintance;
import teamcool.tradego.R;

/**
 * Created by lguti on 7/12/16.
 */
public class AcquaintanceAdapter extends RecyclerView.Adapter<AcquaintanceAdapter.ViewHolder> {
    private ArrayList<Acquaintance> acquaintances;
    private Context context;
    //ViewHolder gives access to our views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfile;
        TextView tvName;
        ImageButton btnAdd;
        View rootView;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            ImageView ivProfile = ButterKnife.findById(itemView, R.id.ivProfile);
            TextView tvName = ButterKnife.findById(itemView, R.id.tvName);
            ImageButton btnAdd = ButterKnife.findById(itemView, R.id.tvName);
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

    @Override
    public void onBindViewHolder(AcquaintanceAdapter.ViewHolder holder, int position) {
        Acquaintance acquaintance = acquaintances.get(position);
        holder.rootView.setTag(acquaintance);
        TextView textView = holder.tvName;
        textView.setText(acquaintance.getName());
        ImageView imageView = holder.ivProfile;
        imageView.setImageResource(0);
        Glide.with(context)
                .load(acquaintance.getProfile_url())
                .into(imageView);

    }

    @Override
    public int getItemCount() {
        return acquaintances.size();
    }
}

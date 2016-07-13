package teamcool.tradego.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import teamcool.tradego.Models.Acquaintance;
import teamcool.tradego.R;

/**
 * Created by lguti on 7/12/16.
 */
public class AcquaintanceAdapter extends RecyclerView.Adapter<AcquaintanceAdapter.ViewHolder> {

    //ViewHolder gives access to our views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivProfile) ImageView ivProfile;
        @BindView(R.id.tvName) TextView tvName;
        @BindView(R.id.btnAdd) ImageButton btnAdd;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    private ArrayList<Acquaintance> acquaintances;

    //Pass in acquaintance array into the adapter
    public AcquaintanceAdapter(ArrayList<Acquaintance> acquaintances){
        this.acquaintances = acquaintances;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        Acquaintance acquaintance = acquaintances.get(position);
    }

    @Override
    public int getItemCount() {
        return acquaintances.size();
    }
}

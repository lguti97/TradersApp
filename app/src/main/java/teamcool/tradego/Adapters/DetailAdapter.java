package teamcool.tradego.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseUser;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import teamcool.tradego.Activities.DetailsActivity;
import teamcool.tradego.Clients.ParseClient;
import teamcool.tradego.Models.Item;
import teamcool.tradego.Models.User;
import teamcool.tradego.R;

/**
 * Created by lguti on 7/27/16.
 */
public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    public static final int ITEMDETAIL = 0;
    public static final int PRICE = 1;
    public static final int PROFILE = 2;

    private String[] mDataSet;
    private int[] mDataSetTypes;
    private Item item;
    private ParseUser user;
    private Context context;
    private ParseClient parseClient;

    private static final String EXTRA_PROTOCOL_VERSION = "com.facebook.orca.extra.PROTOCOL_VERSION";
    private static final String EXTRA_APP_ID = "com.facebook.orca.extra.APPLICATION_ID";
    private static final int PROTOCOL_VERSION = 20150314;
    private static final String YOUR_APP_ID = "528996547286241";
    private static final int SHARE_TO_MESSENGER_REQUEST_CODE = 1;
    private static final int REQUEST_CODE_SHARE_TO_MESSENGER = 15251;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View v) {
            super(v);
        }
    }

    public class ItemDetailViewHolder extends ViewHolder {
        @BindView(R.id.tvItemName) TextView tvItemName;
        @BindView(R.id.tvItemDescription) TextView tvItemDescription;
        public ItemDetailViewHolder (View v) {
            super(v);
            ButterKnife.bind(this,v);

        }
    }

    public class ItemPriceViewHolder extends ViewHolder {
        @BindView(R.id.tvPrice) TextView tvPrice;
        @BindView(R.id.tvItemNegotiable) TextView tvItemNegotiable;
        public ItemPriceViewHolder (View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public class SellerViewHolder extends ViewHolder {
        @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
        @BindView(R.id.tvName) TextView tvName;
        @BindView(R.id.btnMessenger) View btnMessenger;
        public SellerViewHolder (View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
    //This is gonna retrieve the item clas and then the 3 psuedoViewTypes.
    public DetailAdapter(String[] dataSet, int[] dataSetTypes, Item item, Context context) {
        this.mDataSet = dataSet;
        this.mDataSetTypes = dataSetTypes;
        this.item = item;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //so apparently all of this is based on ViewType.
        View v;
        if (viewType == ITEMDETAIL) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_item_details, parent, false);
            return new ItemDetailViewHolder(v);
        } else if (viewType == PRICE) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_price, parent, false);
            return new ItemPriceViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_seller, parent, false);
            return new SellerViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.getItemViewType() == ITEMDETAIL) {
            ItemDetailViewHolder holder1 = (ItemDetailViewHolder) holder;
            String itemName = item.getItem_name();
            String itemName1 = itemName.substring(0, 1).toUpperCase() + itemName.substring(1);
            holder1.tvItemName.setText(itemName1);
            String itemDescription = item.getDescription();
            String itemDescription1 = itemDescription.substring(0, 1).toUpperCase() + itemDescription.substring(1);
            holder1.tvItemDescription.setText(itemDescription1);
        } else if (holder.getItemViewType() == PRICE) {
            ItemPriceViewHolder holder1 = (ItemPriceViewHolder) holder;
            holder1.tvPrice.setText("$" + String.valueOf(item.getPrice()));
            holder1.tvItemNegotiable.setText("Negotiable: " + item.getNegotiable());
        } else {
            SellerViewHolder holder1 = (SellerViewHolder) holder;
            parseClient = new ParseClient();
            user = parseClient.queryUserBasedonFBid(item.getString("fbID"));
            //TODO. Get picURl from the user.

            holder1.ivProfileImage.setImageResource(0);
            Log.d("DEBUG", user.getString("profilePicUrl"));
            Glide.with(context)
                    .load(user.getString("profilePicUrl"))
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(holder1.ivProfileImage);
            holder1.tvName.setText(user.getString("username"));

            //Hardcoded messaging but all we need to is gather userName of person we want to message.
            holder1.btnMessenger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri messenger = Uri.parse((new StringBuilder("http://m.me/")).append(Uri.encode("100005948748513")).toString());
                    Intent i = new Intent(Intent.ACTION_VIEW, messenger);
                    view.getContext().startActivity(i);

                }
            });
        }
    }

    //Fuck so the problem here is that it's only gonna iterate once because it's one object. So how do we change this?
    //Do we just set the texts manually as datatypes? Then how the fuck do we send the images and shit?
    @Override
    public int getItemCount() {
        //I want it to iterate 3 times so it fills out each cardView.
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        //retrieves the view type of each item so we can inflate another cardView.
        //I can do a very hacky thing and trick the adapter into thinking that's there's 3 types
        //So I can inflate it "manually"
        return mDataSetTypes[position];
    }
}

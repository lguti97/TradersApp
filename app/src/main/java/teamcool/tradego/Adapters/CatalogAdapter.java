package teamcool.tradego.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import teamcool.tradego.Activities.DetailsActivity;
import teamcool.tradego.Models.Item;
import teamcool.tradego.R;

/**
 * Created by selinabing on 7/6/16.
 */
public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.ViewHolder> {


    public static class ViewHolder extends RecyclerView.ViewHolder {

        //butterknife binding goes here:
        //@BindView(R.id.tvNewsFeedItemName) TextView itemName;
        @BindView(R.id.ivNewsFeedDisplayImg) ImageView ivItemImage;
        @BindView(R.id.rlItemEach) RelativeLayout rlItemEach;
        @BindView(R.id.tvPrice) TextView tvPrice; 

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            //set item's onClickListener to (this);
        }
    }

    private List<Item> items;
    private Context context;


    public CatalogAdapter(List<Item> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View catalogView = inflater.inflate(R.layout.item_each, parent, false);
        ViewHolder viewHolder = new ViewHolder(catalogView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Item item = items.get(position);
        //populate each item by setting its text and media

        //set price to FREE, int, or double depending on raw value.
        String price;
        Double rawDoublePrice = item.getPrice();
        int truncatedPrice = rawDoublePrice.intValue();
        if (rawDoublePrice.equals((double)truncatedPrice)) {
            if (truncatedPrice == 0) {
                price = "Free";
            } else {
                price = "$" + truncatedPrice;
            }
        } else {
            price = "$" + rawDoublePrice;
        }
        holder.tvPrice.setText(price);

        if(item.getImage1() != null) {
            holder.ivItemImage.setImageBitmap(decodeBase64(item.getImage1()));
        }


        //on click listener to launch detail activity
        holder.rlItemEach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, DetailsActivity.class);
                i.putExtra("item_id", item.getObjectId());
                context.startActivity(i);



            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void clearAndAddAll(List<Item> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    public void insert(int index, Item item) {
        items.add(index,item);
        notifyItemInserted(index);
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}

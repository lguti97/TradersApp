package teamcool.tradego.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import teamcool.tradego.Clients.ParseClient;
import teamcool.tradego.Models.Item;
import teamcool.tradego.R;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.btnMessenger)
    View btnMessenger;
    Item item;
    String itemId;
    /*
    @BindView(R.id.tvItemDescription) TextView tvItemDescription;
    @BindView(R.id.tvItemName) TextView tvItemName;
    @BindView(R.id.tvItemStatus) TextView tvItemStatus;
    @BindView(R.id.etPrice) EditText etPrice;
    @BindView(R.id.tvItemNegotiable) EditText tvItemNegotiable;*/

    TextView tvItemDescription;
    TextView tvItemName;
    TextView tvItemStatus;
    TextView tvItemPrice;
    TextView tvItemNegotiable;
    TextView tvItemCategory;
    ImageView ivItem1;
    ImageView ivItem2;
    ImageView ivItem3;

    private static final int REQUEST_CODE_SHARE_TO_MESSENGER = 15251;
    ParseClient parseClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);

        itemId = getIntent().getStringExtra("item_id");



        final Activity activity = this;

        //when clicked, share a screenshot to messenger
        btnMessenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri contentUri = takeScreenshot();
                ShareToMessengerParams shareToMessengerParams = ShareToMessengerParams.newBuilder(contentUri, "image/jpeg").build();
                MessengerUtils.shareToMessenger(activity, REQUEST_CODE_SHARE_TO_MESSENGER, shareToMessengerParams);
            }
        });

        populateItemDetails();

    }

    private void populateItemDetails() {

        parseClient = new ParseClient();
        item = parseClient.queryItemBasedonObjectID(itemId);

        tvItemDescription = (TextView) findViewById(R.id.tvItemDescription);
        tvItemName = (TextView) findViewById(R.id.tvItemName);
        tvItemStatus = (TextView) findViewById(R.id.tvItemStatus);
        tvItemPrice = (TextView) findViewById(R.id.tvItemPrice);
        tvItemNegotiable = (TextView) findViewById(R.id.tvItemNegotiable);
        tvItemCategory = (TextView) findViewById(R.id.tvItemCategory);
        ivItem1 = (ImageView) findViewById(R.id.ivItem1);
        ivItem2 = (ImageView) findViewById(R.id.ivItem2);
        ivItem3 = (ImageView) findViewById(R.id.ivItem3);


        tvItemDescription.setText("Item description: " + item.getDescription());
        tvItemName.setText(item.getItem_name());
        tvItemStatus.setText("Status: " + item.getStatus());
        String price = String.valueOf(item.getPrice());
        tvItemPrice.setText("Price: " + price);
        tvItemNegotiable.setText("Negotiable: " + item.getNegotiable());
        tvItemCategory.setText("Category: " + item.getCategory());
        ivItem1.setImageBitmap(StringToBitMap(item.getImage1()));
        ivItem2.setImageBitmap(StringToBitMap(item.getImage2()));
        ivItem3.setImageBitmap(StringToBitMap(item.getImage3()));


    }


    public Uri takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        File imageFile = null;

        try {
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Uri.fromFile(imageFile);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_activity, menu);
        return true;
    }


    public void onEditItem(MenuItem item) {

        Intent i = new Intent(DetailsActivity.this, AddItemActivity.class);
        i.putExtra("item_id", itemId);
        startActivity(i);
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();

        }
        return super.onOptionsItemSelected(item);
    }
}

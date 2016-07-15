package teamcool.tradego.Activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
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

    @BindView(R.id.btnMessenger) View btnMessenger;
    Item item;
    /*
    @BindView(R.id.tvItemDescription) TextView tvItemDescription;
    @BindView(R.id.tvItemName) TextView tvItemName;
    @BindView(R.id.tvItemStatus) TextView tvItemStatus;
    @BindView(R.id.etPrice) EditText etPrice;
    @BindView(R.id.tvItemNegotiable) EditText tvItemNegotiable;*/

    TextView tvItemDescription;
    TextView tvItemName;
    TextView tvItemStatus;
    EditText etPrice;
    TextView tvItemNegotiable;
    TextView tvItemCategory;


    private static final int REQUEST_CODE_SHARE_TO_MESSENGER = 15251;
    ParseClient parseClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

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

        String itemId = getIntent().getStringExtra("item_id");
        parseClient = new ParseClient();
        item = parseClient.queryItemBasedonObjectID(itemId);

        tvItemDescription = (TextView) findViewById(R.id.tvItemDescription);
        tvItemName = (TextView) findViewById(R.id.tvItemName);
        tvItemStatus = (TextView) findViewById(R.id.tvItemStatus);
        etPrice = (EditText) findViewById(R.id.etPrice);
        tvItemNegotiable = (TextView) findViewById(R.id.tvItemNegotiable);
        tvItemCategory = (TextView) findViewById(R.id.tvItemCategory);


        tvItemDescription.setText(item.getDescription());
        tvItemName.setText(item.getItem_name());
        tvItemStatus.setText("Status: " + item.getStatus());
        etPrice.setText(String.valueOf(item.getPrice()));
        tvItemNegotiable.setText("Negotiable: " + item.getNegotiable());
        tvItemCategory.setText("Category: " + item.getCategory());

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


}

package teamcool.tradego.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Date;

import java.util.List;

import butterknife.ButterKnife;
import teamcool.tradego.Adapters.DetailAdapter;
import teamcool.tradego.Clients.ParseClient;
import teamcool.tradego.Fragments.AlertDeleteFragment;
import teamcool.tradego.Models.Item;
import teamcool.tradego.R;
import teamcool.tradego.URISliderView;

public class DetailsActivity extends AppCompatActivity {

    ParseClient parseClient;
    String ownerId = "";
    Item item;
    String itemId = "";



    public static final int ITEMDETAIL = 0;
    public static final int PRICE = 1;
    public static final int PROFILE = 2;

    private RecyclerView rvDetails;
    private DetailAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SliderLayout mSlider;
    private String[] mDataset = {"foo", "bar", "lol"};
    private int mDatasetTypes[] = {ITEMDETAIL, PRICE, PROFILE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mSlider = (SliderLayout) findViewById(R.id.slider);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        //Retrieves the Item that will be displayed in detail
        item = new Item();
        itemId = getIntent().getStringExtra("item_id");
        Log.d("DEBUG", itemId);
        parseClient = new ParseClient();
        item = parseClient.queryItemBasedonObjectID(itemId);

        //populates the slider
        populateSlider();

        //Setuping up Adapter
        rvDetails = (RecyclerView) findViewById(R.id.rvDetails);
        mLayoutManager = new LinearLayoutManager(DetailsActivity.this);
        rvDetails.setLayoutManager(mLayoutManager);
        //Pass in context here as well?
        mAdapter = new DetailAdapter(mDataset, mDatasetTypes, item, this);
        rvDetails.setAdapter(mAdapter);


        //For the images of the person.
        //ivItem1.setImageBitmap(decodeBase64(item.getImage1()));
        //ivItem2.setImageBitmap(decodeBase64(item.getImage2()));

    }

    //For Image Compression
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



    //Editing purposes.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        List<Item> items = parseClient.queryItemsOnUser(ParseUser.getCurrentUser());

        for(Item item: items) {
            String obj = item.getObjectId();
            if (obj.equals(itemId)) {
                getMenuInflater().inflate(R.menu.menu_detail_activity, menu);
                return true;
            }
        }
        return true;
    }


    public void onEditItem(MenuItem item) {
        Intent i = new Intent(DetailsActivity.this, AddItemActivity.class);
        i.putExtra("item_id", itemId);
        startActivity(i);
    }

    public void onDeleteItem(MenuItem item) {
        showAlertDialog();
    }

    public void showAlertDialog() {
        AlertDeleteFragment frag = AlertDeleteFragment.newInstance(true,item.getItem_name(),itemId);
        frag.show(getSupportFragmentManager(), "fragment_alert");
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
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }
        return super.onOptionsItemSelected(item);
    }


    public void populateSlider() {
        for (int i = 1; i < 3; i++) {
            if (i == 1) {
                final URISliderView uriSliderView = new URISliderView(this);
                //final TextSliderView textSliderView = new TextSliderView(this);
                // initialize a SliderLayout.
                //String image = item.getImage1();
                //Attempt to retrieve image from ParseFile
                ParseFile something = (ParseFile) item.get("item_photo1");
                something.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] data, ParseException e) {
                        if (e == null) {
                            Bitmap bmp = BitmapFactory
                                    .decodeByteArray(data, 0, data.length);
                            String URI = BitMapToString(bmp);


                            uriSliderView
                                    .description("Item1")
                                    .image(URI)
                                    .setScaleType(BaseSliderView.ScaleType.Fit)
                                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                        @Override
                                        public void onSliderClick(BaseSliderView slider) {
                                            //
                                        }
                                    });

                            //add your extra information
                            uriSliderView.bundle(new Bundle());
                            uriSliderView.getBundle()
                                    .putString("extra", "Item");

                            mSlider.addSlider(uriSliderView);
                        } else {
                            //Do nothing
                        }
                    }
                });
            }
            else if (i == 2){
                final URISliderView uriSliderView = new URISliderView(this);
                //final TextSliderView textSliderView = new TextSliderView(this);
                // initialize a SliderLayout.
                //String image = item.getImage1();
                //Attempt to retrieve image from ParseFile
                ParseFile something = (ParseFile) item.get("item_photo2");
                something.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] data, ParseException e) {
                        if (e == null) {
                            Bitmap bmp = BitmapFactory
                                    .decodeByteArray(data, 0, data.length);
                            String URI = BitMapToString(bmp);


                            uriSliderView
                                    .description("Item2")
                                    .image(URI)
                                    .setScaleType(BaseSliderView.ScaleType.Fit)
                                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                        @Override
                                        public void onSliderClick(BaseSliderView slider) {
                                            //
                                        }
                                    });

                            //add your extra information
                            uriSliderView.bundle(new Bundle());
                            uriSliderView.getBundle()
                                    .putString("extra", "Item");

                            mSlider.addSlider(uriSliderView);
                        } else {
                            //Do nothing
                        }
                    }
                });
            }

        }
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

}

package teamcool.tradego.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import teamcool.tradego.Clients.ParseClient;
import teamcool.tradego.Models.Item;
import teamcool.tradego.R;

public class AddItemActivity extends AppCompatActivity {

    int index; //this is just a manipulation to populate the different image views with the correct image

    @BindView(R.id.ivItem1) ImageView ivItem1;
    @BindView(R.id.ivItem2) ImageView ivItem2;
    @BindView(R.id.etPrice) EditText etPrice;
    @BindView(R.id.etItemName) EditText etItemName;
    @BindView(R.id.etItemDescription) EditText etItemDescription;
    @BindView(R.id.Add_New_Item) TextView header;
    ParseClient parseClient;


    String negotiable;
    String category;
    String status;
    String image_1;
    String image_2;
    String itemId;
    Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        ButterKnife.bind(this);
        //Take first image of the item to be sold


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);



        ivItem1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onLaunchCamera(view);
                index = 1;
            }
        });

        //Take the second image of the item to be sold
        ivItem2.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                onLaunchCamera(view);
                index = 2;
            }
        });




        //keyboard focus changing:
        etPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    hideKeyboard(view);
                }
            }
        });
        etItemName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    hideKeyboard(view);
                }
            }
        });
        etItemDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    hideKeyboard(view);
                }
            }
        });

        onCategorySpinner();
        onStatusSpinner();


        if (getIntent().getStringExtra("item_id") != null) {

            populateEditItem();

        }


    }

    private void populateEditItem() {

        parseClient = new ParseClient();
        header.setText("Edit Item!");
        itemId = getIntent().getStringExtra("item_id");
        item = parseClient.queryItemBasedonObjectID(itemId);
        etItemName.setText(item.getItem_name());
        String price = String.valueOf(item.getPrice());
        etPrice.setText(price);
        etItemDescription.setText(item.getDescription());
        ivItem1.setImageBitmap(decodeBase64(item.getImage1()));
        ivItem2.setImageBitmap(decodeBase64(item.getImage2()));

        //Bitmap takenImage_unscaled = BitmapFactory.decodeFile(item.getImage1());
        //Bitmap takenImage_unscaled2 = BitmapFactory.decodeFile(item.getImage2());
        //Bitmap takenImage = Bitmap.createScaledBitmap(takenImage_unscaled, 150, 150, true);
        //Bitmap takenImage2 = Bitmap.createScaledBitmap(takenImage_unscaled2, 150, 150, true);


        /*
            if (item.getNegotiable() == "Yes") {
                RadioButton yes = (RadioButton) findViewById(R.id.rbYes);
                yes.setChecked(true);
            } else if (item.getNegotiable() == "No") {
                RadioButton no = (RadioButton) findViewById(R.id.rbNo);
                no.setChecked(true);
            }

            */

    }


    public void onRadioButtonClicked(View view) {

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rbNo:
                if (checked)
                    // Do something
                negotiable = "No";
                    break;
            case R.id.rbYes:
                if (checked)
                    // Do something
                negotiable = "Yes";
                    break;
        }
    }


    public void onCategorySpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spCategory);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //set the string below to tvCategory
                category = parent.getItemAtPosition(position).toString();

            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

    }


    public void onStatusSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spStatus);
// Create an ArrayAdapter using the string array and a default spinner layout


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.status, android.R.layout.simple_spinner_item);


// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //set the string below to tvStatus
                status = parent.getItemAtPosition(position).toString();

            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

    }

    //Code below is all for Launching Camera

    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    String photoFileName = "PHOTO.jpg";

    public void onLaunchCamera(View view) {

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName)); // set the image file name

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri takenPhotoUri = getPhotoFileUri(photoFileName);
                // by this point we have the camera photo on disk
                Bitmap takenImage_unscaled = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                Bitmap takenImage = Bitmap.createScaledBitmap(takenImage_unscaled, 70, 70, true);

                // Load the taken image into a preview

                if(index==1) {

                    ivItem1.setImageBitmap(takenImage);
                    image_1 = encodeToBase64(takenImage, Bitmap.CompressFormat.JPEG, 100);
                }
                else if(index ==2) {

                    ivItem2.setImageBitmap(takenImage);
                    image_2 = encodeToBase64(takenImage, Bitmap.CompressFormat.JPEG, 100);
                }



            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Returns the Uri for a photo stored on disk given the fileName
    public Uri getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(APP_TAG, "failed to create directory");
            }

            // Return the file target for the photo based on filename
            return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
        }
        return null;
    }

    // Returns true if external storage for photos is available
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }


    public void onAddItemClick(View view) {

        if(getIntent().getStringExtra("item_id") != null) {

            image_1 = item.getImage1();
            image_2 = item.getImage2();

        }

        Double price;

        try {
            price = Double.parseDouble(etPrice.getText().toString());
        } catch (Exception e) {
            price = 0.0d;
        }

        if(etItemDescription.getText().toString() == null || category == null || etItemDescription.getText().toString() == null || status == null || price==0.0d) {
            Toast.makeText(this, "Please complete all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (image_1 == null && image_2 == null) {
            Toast.makeText(this, "Please add two images of this item", Toast.LENGTH_SHORT).show();
            return;
        }

        Item new_item = new Item(etItemName.getText().toString(),
                category, etItemDescription.getText().toString(),
                status, price, negotiable, image_1, image_2);

        ParseUser user = ParseUser.getCurrentUser();
        new_item.setOwner(user);



        if(getIntent().getStringExtra("item_id") != null) {
            parseClient.updateItem(getIntent().getStringExtra("item_id"), new_item);
            Toast.makeText(this, "Item Edited!", Toast.LENGTH_SHORT).show();
        }

        else {
            Toast.makeText(this, "Item Added!", Toast.LENGTH_SHORT).show();
            new_item.saveInBackground();}

        Intent i = new Intent(this, NewsFeedActivity.class);
        startActivity(i);
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();

        }
        return super.onOptionsItemSelected(item);
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

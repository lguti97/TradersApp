package teamcool.tradego.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import teamcool.tradego.Clients.ParseClient;
import teamcool.tradego.Fragments.AlertBuyerInfoFragment;
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
    @BindView(R.id.skipAddItem) Button skipAddItem;
    @BindView(R.id.spStatus) Spinner spStatus;
    @BindView(R.id.rbNo) RadioButton rbNo;
    @BindView(R.id.rbYes) RadioButton rbYes;

    ParseClient parseClient;
    ParseUser user;


    String negotiable;
    String category;
    String status;
    String image_1;
    String image_2;
    String itemId;
    Item item;
    boolean initial;
    ArrayAdapter<CharSequence> adapter;

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

        if (getIntent() != null) {
            initial = getIntent().getBooleanExtra("initial", false);
        }

        if (initial) {
            skipAddItem.setText(getResources().getString(R.string.skip));

            if (ParseUser.getCurrentUser().isNew()) {
                actionbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d3d3d3")));
                actionbar.setTitle("Getting Started");
            }
        }

        ivItem1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //onLaunchCamera(view);
                //onPickPhoto(view);
                startDialog(view);
                index = 1;
            }
        });

        //Take the second image of the item to be sold
        ivItem2.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                //onLaunchCamera(view);
                //onPickPhoto(view);
                startDialog(view);
                index = 2;
            }
        });

        //keyboard focus changing:
        etPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    hideKeyboard(view);
                }
            }
        });
        etItemName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    hideKeyboard(view);
                }
            }
        });
        etItemDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
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
        spStatus.setSelection(adapter.getPosition(item.getStatus()));
        if (item.getNegotiable().equalsIgnoreCase("Yes")) {
            rbYes.setSelected(true);
        } else if (item.getNegotiable().equalsIgnoreCase("No")) {
            rbNo.setSelected(true);
        }

        //Coverting image to bitmap.
        ivItem1.setImageBitmap(decodeBase64(item.getImage1()));
        ivItem2.setImageBitmap(decodeBase64(item.getImage2()));

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
        adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, R.layout.custom_spinner_category);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
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

        int array = R.array.status_add;

        if (getIntent().getStringExtra("item_id")!= null){
            array = R.array.status;
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,array, R.layout.custom_spinner_category);


// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
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



    public void onAddItemClick(View view) {

        if(getIntent().getStringExtra("item_id") != null) {
            image_1 = item.getImage1();
            image_2 = item.getImage2();
        }

        Double price;

        try {
            price = Double.parseDouble(etPrice.getText().toString());
        } catch (Exception e) {
            price = -15251.0;
        }

        if(etItemDescription.getText() == null || category == null || etItemDescription.getText().toString() == null || status == null || price < 0) {
            Toast.makeText(this, "Please complete all the fields with correct values", Toast.LENGTH_SHORT).show();
            return;
        }

        if (image_1 == null && image_2 == null) {
            Toast.makeText(this, "Please add two images of this item", Toast.LENGTH_SHORT).show();
            return;
        }

        //Retrieve fbID from the currentUser
        user = ParseUser.getCurrentUser();
        //Not sure of the syntax that must be used to retrieve the FB id but this is a start.
        String fbID = user.getString("fb_id");
        //Can't store this into the Item ParseObject...
        /*
        MUST CHANGE HERE.
         */

        byte[] data = image_1.getBytes();
        ParseFile file = new ParseFile("item_photo", data);
        file.saveInBackground();


        Item new_item = new Item(etItemName.getText().toString(),
                category, etItemDescription.getText().toString(),
                status, price, negotiable, image_1, image_2, fbID);


        new_item.setOwner(user);
        new_item.put("item_photo", file);
        new_item.saveInBackground();



        if(getIntent().getStringExtra("item_id") != null) {
            parseClient.updateItem(getIntent().getStringExtra("item_id"), new_item);
            Toast.makeText(this, "Item Edited!", Toast.LENGTH_SHORT).show();
        }

        else {
            Toast.makeText(this, "Item Added!", Toast.LENGTH_SHORT).show();
            new_item.saveInBackground();
        }

        if (!initial) {
            /*
            if (spStatus.getSelectedItem().toString().equals("Sold")) {
                AlertBuyerInfoFragment frag = AlertBuyerInfoFragment.newInstance();
                frag.show(getSupportFragmentManager(), "fragment_alert");
            }
            */
            finish();
        } else {
            Intent i = new Intent(AddItemActivity.this, NewsFeedActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
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

    public void skipAddItemActivity(View view) {
        Intent i = new Intent(AddItemActivity.this, NewsFeedActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }



    private void startDialog(final View view) {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                this);
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                       onPickPhoto(view);
                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        onLaunchCamera(view);
                    }
                });
        myAlertDialog.show();


    }



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
        int desiredWidth = 70;
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //So this is the URI that is retrieved when photo is taken.
                Uri takenPhotoUri = getPhotoFileUri(photoFileName);
                // by this point we have the camera photo on disk
                Bitmap takenImage_unscaled = BitmapFactory.decodeFile(takenPhotoUri.getPath());

                Bitmap takenImage = Bitmap.createScaledBitmap(takenImage_unscaled, 250, 250, true);
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

        else if(requestCode == PICK_PHOTO_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri photoUri = data.getData();
                // Do something with the photo based on Uri
                Bitmap selectedImage = null;
                try {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Load the selected image into a preview


                if(index==1) {

                    ivItem1.setImageBitmap(selectedImage);
                    image_1 = encodeToBase64(selectedImage, Bitmap.CompressFormat.JPEG, 100);
                }
                else if(index ==2) {

                    ivItem2.setImageBitmap(selectedImage);
                    image_2 = encodeToBase64(selectedImage, Bitmap.CompressFormat.JPEG, 100);
                }

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

    // PICK_PHOTO_CODE is a constant integer
    public final static int PICK_PHOTO_CODE = 1046;


    // Trigger gallery selection for a photo
    public void onPickPhoto(View view) {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }


}



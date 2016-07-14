package teamcool.tradego.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseUser;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import teamcool.tradego.Models.Item;
import teamcool.tradego.R;

public class AddItemActivity extends AppCompatActivity {

    int index; //this is just a manipulation to populate the different image views with the correct image

    @BindView(R.id.ivItem1) ImageView ivItem1;
    @BindView(R.id.ivItem2) ImageView ivItem2;
    @BindView(R.id.ivItem3) ImageView ivItem3;
    @BindView(R.id.etPrice) EditText etPrice;
    @BindView(R.id.etItemName) EditText etItemName;
    @BindView(R.id.etItemDescription) EditText etItemDescription;
    String negotiable;
    String category;
    String status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        ButterKnife.bind(this);
        //Take first image of the item to be sold

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


        //Take the third image of the item to be sold


        ivItem3.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                onLaunchCamera(view);
                index = 3;
            }
        });

        onCategorySpinner();
        onStatusSpinner();

    }


    public void onAddItemClick(View view) {


        Double price;

        try {price = Double.parseDouble(etPrice.getText().toString());} catch (Exception e) {
            price = 0.0d;
        }

        Item new_item = new Item(etItemName.getText().toString(),
                category, etItemDescription.getText().toString(),
                status,price);


        //User user = parseClient.getCurrentParseUser();
        //new_item.setOwner(user);

        ParseUser user = ParseUser.getCurrentUser();

        new_item.setOwner(user);

        new_item.saveInBackground();


//        // Save the post and return
//        new_item.saveInBackground(new SaveCallback() {
//
//
//            @Override
//            public void done(ParseException e) {
//                if (e == null) {
//                    setResult(RESULT_OK);
//                    finish();
//                } else {
//                    Toast.makeText(getApplicationContext(),
//                            "Error saving: " + e.getMessage(),
//                            Toast.LENGTH_SHORT)
//                            .show();
//                }
//            }
//
//        });
//
//
//
//
//        this.setResult(Activity.RESULT_OK);
//        this.finish();

        Toast.makeText(this, "Item Added!", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status, android.R.layout.simple_spinner_item);
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
    public String photoFileName = "photo.jpg";

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
                Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                // Load the taken image into a preview

                if(index==1) {

                    ivItem1.setImageBitmap(takenImage);

                }
                else if(index ==2) {

                    ivItem2.setImageBitmap(takenImage);

                }

                else if(index ==3) {
                    ivItem3.setImageBitmap(takenImage);
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

}

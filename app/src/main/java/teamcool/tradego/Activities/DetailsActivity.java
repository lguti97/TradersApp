package teamcool.tradego.Activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import teamcool.tradego.R;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.btnMessenger) View btnMessenger;

    private static final int REQUEST_CODE_SHARE_TO_MESSENGER = 15251;

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


    public void onVenmoClick(View view) {

        //Launch venmo app

    }
}

package teamcool.tradego.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import teamcool.tradego.R;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
    }




    public void onMessengerClick(View view) {
        //Launch the messenger app
    }

    public void onVenmoClick(View view) {

        //Launch venmo app

    }
}

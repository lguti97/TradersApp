package teamcool.tradego.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import teamcool.tradego.Clients.ParseClient;
import teamcool.tradego.Fragments.UserCatalogFragment;
import teamcool.tradego.R;

//import teamcool.tradego.ParseClient;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.tvItemsBought) TextView tvItemsBought;
    @BindView(R.id.tvItemsSold) TextView tvItemsSold;


    ParseUser user;
    ParseClient parseClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        parseClient = new ParseClient();


        user = ParseUser.getCurrentUser();

        populateUserHeader(user);

        if(savedInstanceState == null) {

            //Display user fragment within this activity
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, new UserCatalogFragment());
            ft.commit();
        }
    }


    //populate user header
    private void populateUserHeader(ParseUser user) {

        tvUserName.setText(user.getString("username"));
        Picasso.with(this).load(user.getString("profilePicUrl"))
                .fit().centerInside()
                .transform(new RoundedCornersTransformation(10, 10))
                .into(ivProfileImage);

        tvItemsBought.setText("Items Bought: " + "32");
        tvItemsSold.setText("Items sold: " + "56");

    }

}

package teamcool.tradego.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import teamcool.tradego.Fragments.UserCatalogFragment;
import teamcool.tradego.ParseClient;
import teamcool.tradego.R;
import teamcool.tradego.User;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.tvUserName) TextView tvUserName;
    User user;
    ParseClient parseClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        parseClient = new ParseClient();

        user = parseClient.getCurrentParseUser();

        populateUserHeader(user);

        if(savedInstanceState == null) {

            //Display user fragment within this activity
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, new UserCatalogFragment());
            ft.commit();
        }
    }

    //populate user header
    private void populateUserHeader(User user) {
        tvUserName.setText(user.getUsername());
        Picasso.with(this).load(user.getProfilePicURL())
                .fit().centerInside()
                .transform(new RoundedCornersTransformation(10, 10))
                .into(ivProfileImage);
    }
}

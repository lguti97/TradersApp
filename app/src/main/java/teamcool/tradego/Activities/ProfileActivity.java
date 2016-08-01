package teamcool.tradego.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import teamcool.tradego.Clients.ParseClient;
import teamcool.tradego.Fragments.AlertDeleteFragment;
import teamcool.tradego.Fragments.UserCatalogFragment;
import teamcool.tradego.R;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.tvItemsSold) TextView tvItemsSold;
    @BindView(R.id.tvNumFriends) TextView tvNumFriends;


    ParseUser user;
    ParseClient parseClient;
    String userObjId;
    UserCatalogFragment frag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);

        parseClient = new ParseClient();

        userObjId = getIntent().getStringExtra("objectId");
        user = parseClient.queryUserBasedonObjectID(userObjId);


        if(savedInstanceState == null) {
            populateUserHeader(user);
            frag = UserCatalogFragment.newInstance(userObjId,"Available");
            frag.setUserVisibleHint(true); //manually call it since viewpager is not present
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flProfileContainer, frag);
            ft.commit();
        }

        else {
            CharSequence name = savedInstanceState.getCharSequence("Name");
            CharSequence sold = savedInstanceState.getCharSequence("Sold");
            CharSequence friends = savedInstanceState.getCharSequence("Friends");
            String image = savedInstanceState.getString("image");
            frag = (UserCatalogFragment) getSupportFragmentManager().getFragment(savedInstanceState, "frag");
            frag.setUserVisibleHint(true); //manually call it since viewpager is not present
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flProfileContainer, frag);
            ft.commit();



            tvUserName.setText(name);
            tvItemsSold.setText(sold);
            tvNumFriends.setText(friends);
            Glide.with(this).load(image)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(ivProfileImage);
        }

    }

    //populate user header
    private void populateUserHeader(ParseUser user) {

        tvUserName.setText(user.getString("username"));

        Glide.with(this).load(user.getString("profilePicUrl"))
                .bitmapTransform(new CropCircleTransformation(this))
                .into(ivProfileImage);

        parseClient.countNumItemsOnStatus(user,"Sold",tvItemsSold,""," items sold");
        parseClient.countNumFriendsOfUser(user,tvNumFriends," friends");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("Name", tvUserName.getText());
        outState.putCharSequence("Sold", tvItemsSold.getText());
        outState.putCharSequence("Friends", tvNumFriends.getText());
        outState.putString("image", user.getString("profilePicUrl"));
        getSupportFragmentManager().putFragment(outState, "frag",frag );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (!userObjId.equals(ParseUser.getCurrentUser().getObjectId())) {
            getMenuInflater().inflate(R.menu.menu_profile_activity, menu);
        }
        return true;
    }

    public void onDeleteFriend(MenuItem item) {
        showAlertDialog();
    }

    public void showAlertDialog() {
        AlertDeleteFragment frag = AlertDeleteFragment.newInstance(false,tvUserName.getText().toString(),user.getString("user_id"));
        frag.show(getSupportFragmentManager(), "fragment_alert");
    }


}

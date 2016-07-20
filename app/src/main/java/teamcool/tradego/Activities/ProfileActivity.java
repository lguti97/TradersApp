package teamcool.tradego.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import teamcool.tradego.Clients.ParseClient;
import teamcool.tradego.Fragments.UserCatalogFragment;
import teamcool.tradego.R;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.tvItemsSold) TextView tvItemsSold;
    @BindView(R.id.tvItemsOnHold) TextView tvItemsonHold;
    @BindView(R.id.tvItemsAvailable) TextView tvItemsAvailable;


    ParseUser user;
    ParseClient parseClient;
    String userObjId;

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


        //Get the viewpager
        //Set the viewpager adapter for the pager
        //find the sliding tabstrip
        //attach the tabstrip to the viewpager

        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        vpPager.setAdapter(new CatalogPagerAdapter(getSupportFragmentManager()));

        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(vpPager);


        parseClient = new ParseClient();



        userObjId = getIntent().getStringExtra("objectId");
        user = parseClient.queryUserBasedonObjectID(userObjId);


        if(savedInstanceState == null) {
            populateUserHeader(user);

        }

        else {

            CharSequence name = savedInstanceState.getCharSequence("Name");
            CharSequence available = savedInstanceState.getCharSequence("Available");
            CharSequence sold = savedInstanceState.getCharSequence("Sold");
            CharSequence on_hold = savedInstanceState.getCharSequence("On Hold");
            tvUserName.setText(name);
            tvItemsAvailable.setText(available);
            tvItemsSold.setText(sold);
            tvItemsonHold.setText(on_hold);

            String image = savedInstanceState.getString("image");

            Picasso.with(this).load(image)
                    .resize(100, 0)
                    .transform(new RoundedCornersTransformation(10, 10))
                    .into(ivProfileImage);


        }



    }


    //populate user header
    private void populateUserHeader(ParseUser user) {

        tvUserName.setText(user.getString("username"));
        Picasso.with(this).load(user.getString("profilePicUrl"))
                .fit().centerInside()
                .transform(new RoundedCornersTransformation(10, 10))
                .into(ivProfileImage);

        tvItemsSold.setText("Items Sold: " + parseClient.countNumItemsOnStatus(user,"Sold"));
        tvItemsonHold.setText("Items on Hold: " + parseClient.countNumItemsOnStatus(user,"On hold"));
        tvItemsAvailable.setText("Items Available: " + parseClient.countNumItemsOnStatus(user,"Available"));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("Name", tvUserName.getText());
        outState.putCharSequence("Sold", tvItemsSold.getText());
        outState.putCharSequence("Available", tvItemsAvailable.getText());
        outState.putCharSequence("On Hold", tvItemsonHold.getText());
        outState.putString("image", user.getString("profilePicUrl"));
    }

    //return the order of the fragments in the viewpager
    public class CatalogPagerAdapter extends FragmentPagerAdapter {


        private String tabTitles[] = {"Available", "OnHold", "Sold"};

        public CatalogPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if(position==0) {
                return UserCatalogFragment.newInstance(userObjId,"Available");
            }

            else if (position == 1) {
                return UserCatalogFragment.newInstance(userObjId,"On hold");
            }
            else if (position == 2) {
                return UserCatalogFragment.newInstance(userObjId,"Sold");
            }
            else {
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

}

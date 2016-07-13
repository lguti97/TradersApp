package teamcool.tradego.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import teamcool.tradego.Clients.ParseClient;
import teamcool.tradego.Fragments.CategoriesTimelineFragment;
import teamcool.tradego.Fragments.TopTimelineFragment;
import teamcool.tradego.R;

public class NewsFeedActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.viewpager) ViewPager viewpager;
    @BindView(R.id.tabs) PagerSlidingTabStrip tabStrip;
    @BindView(R.id.newsfeed_drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.nvNewsFeedNavDrawer) NavigationView navDrawer;
    //NOTE: if getting reference to navDrawer's header is required
    // delete the header setting from xml
    // and add the header programmatically using "navDrawer.inflateHeaderView(R.layout.headers_xml_file)"
    // otherwise it would cause null pointer exceptions when used with recycler vieww!!!

    ParseClient parseClient;

    FragmentStatePagerAdapter fragmentStatePagerAdapter;

    ActionBarDrawerToggle drawerToggle;

    public class catalogPagerAdapter extends FragmentStatePagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabNames[] = {"Top", "Categories"};

        public catalogPagerAdapter(FragmentManager fragmentManager) { super(fragmentManager); }


        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new TopTimelineFragment();
            } else {
                // to be modified, to handle different categories
                return new CategoriesTimelineFragment();
            }
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabNames[position];
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        ButterKnife.bind(this);

        //configure toolbar
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        //viewpager setup
        fragmentStatePagerAdapter = new catalogPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(fragmentStatePagerAdapter); //may cause problems (?)
        tabStrip.setViewPager(viewpager);
        //set TabBackground and IndicatorColor later

        //setup navigation drawer
        setupNavDrawerTabs(navDrawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);

        //navigation drawer populating
        // using findviewbyid because this is in another xml layout file, ButterKnife causes runtime exception
        View headerView = navDrawer.getHeaderView(0);

        ImageView ivNavProfilePic = (ImageView) headerView.findViewById(R.id.ivNavProfilePic);
        TextView tvNavUserName = (TextView) headerView.findViewById(R.id.tvNavUserName);
        TextView tvNavNumFriends = (TextView) headerView.findViewById(R.id.tvNavNumFriends);
        TextView tvNavItemsSold = (TextView) headerView.findViewById(R.id.tvNavItemsSold);
        TextView tvNavItemsBought = (TextView) headerView.findViewById(R.id.tvNavItemsBought);
        ParseUser currUser = ParseUser.getCurrentUser();
        Picasso.with(this).load(currUser.getString("profilePicUrl")).fit().transform(new CropCircleTransformation()).into(ivNavProfilePic);
        tvNavUserName.setText(currUser.getString("username"));
        tvNavNumFriends.setText("122"+" friends"); //placeholder
        tvNavItemsSold.setText("150"+" items sold"); //placeholder
        tvNavItemsBought.setText("251"+" items bought"); //placeholder

        //initialize clients
        parseClient = new ParseClient();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_feed, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search_nf);
        SearchView searchView = (SearchView) searchItem.getActionView();

        //make search box always show by default
        // user dont have to tap on the icon to make a search
        //searchView.setIconifiedByDefault(false);

        //expand the SearchView
        searchItem.expandActionView();
        searchView.requestFocus();

        //set hint text
        searchView.setQueryHint("Search for an item...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent i = new Intent(getApplicationContext(),SearchActivity.class);
                i.putExtra("query",query);
                startActivity(i);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //To be added
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    public void setupNavDrawerTabs(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        selectDrawerItem(item);
                        return false;
                    }
                });
    }

    public void selectDrawerItem(MenuItem item) {
        Fragment fragment = null;
        Class fragmentClass = null;
        switch(item.getItemId()) {
            /*
            case R.id.nav_first_fragment:
                fragmentClass = SomeFragment.class;
                break;
            */
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //replace existing fragment
        //getSupportFragmentManager().beginTransaction().replace(R.id.flNewsfeedContainer, fragment).commit();

        //highlight the selected item
        item.setChecked(true);
        setTitle(item.getTitle());
        drawerLayout.closeDrawers();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
}

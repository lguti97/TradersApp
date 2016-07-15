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
import android.util.Log;
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
import teamcool.tradego.Fragments.CatalogListFragment;
import teamcool.tradego.Fragments.CategoriesTimelineFragment;
import teamcool.tradego.Fragments.TopTimelineFragment;
import teamcool.tradego.Fragments.UserCatalogFragment;
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

    private int selector;

    public class catalogPagerAdapter extends FragmentStatePagerAdapter {

        public catalogPagerAdapter(FragmentManager fragmentManager) { super(fragmentManager); }


        @Override
        public Fragment getItem(int position) {
            if (selector == 0) {
                if (position == 0)
                    return new TopTimelineFragment();
                else
                    return new CategoriesTimelineFragment();
            }
            else if (selector == 1) {
                String currentUserObjID = ParseUser.getCurrentUser().getObjectId();
                if (position == 0)
                    return UserCatalogFragment.newInstance(currentUserObjID,"Available");
                else if (position == 1)
                    return UserCatalogFragment.newInstance(currentUserObjID,"On hold");
                else
                    return UserCatalogFragment.newInstance(currentUserObjID,"Sold");
            }
            else
                return null;
        }


        @Override
        public int getCount() {
            if (selector == 0)
                return 2;
            else if (selector == 1)
                return 3;
            else
                return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String tab0Names[] = {"Top", "Categories"};
            String tab1Names[] = {"Available", "Onhold", "Sold"};
            if (selector == 0) {
                Log.d("DEBUG","count how many times 0 reached");
                return tab0Names[position];
            }
            else {
                Log.d("DEBUG","count how many times 1 reached");
                return tab1Names[position];
            }

            //return tabNames[position];
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        ButterKnife.bind(this);

        parseClient = new ParseClient();

        //configure toolbar
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        selector = 0;

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
        TextView tvNavItemsOnhold = (TextView) headerView.findViewById(R.id.tvNavItemsOnhold);
        ParseUser currUser = ParseUser.getCurrentUser();
        Picasso.with(this).load(currUser.getString("profilePicUrl")).fit().transform(new CropCircleTransformation()).into(ivNavProfilePic);
        tvNavUserName.setText(currUser.getString("username"));
        String numFriends = parseClient.countNumFriendsOfUser(currUser)+" friends";
        String numItemsSold = parseClient.countNumItemsSold(currUser)+" items sold";
        String numItemsOnhold = parseClient.countNumsItemsOnhold(currUser)+" items on hold";
        tvNavNumFriends.setText(numFriends);
        tvNavItemsSold.setText(numItemsSold);
        tvNavItemsOnhold.setText(numItemsOnhold);

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
            case R.id.nav_home_fragment:
                selector = 0;
                break;
            case R.id.nav_catalog_fragment:
                selector = 1;
                //fragmentStatePagerAdapter.upd
                fragmentClass = UserCatalogFragment.class;
                break;

        }
        tabStrip.setViewPager(viewpager);

        try {
            //fragment = (Fragment) fragmentClass.newInstance();
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

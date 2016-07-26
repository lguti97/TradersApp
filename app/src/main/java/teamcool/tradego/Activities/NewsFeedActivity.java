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
import teamcool.tradego.Fragments.FriendsListFragment;
import teamcool.tradego.Fragments.TopTimelineFragment;
import teamcool.tradego.Fragments.UserCatalogFragment;
import teamcool.tradego.Fragments.WishListFragment;
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

        final String tab0Names[] = {"All", "Sports", "Clothes", "Accessories", "Stationary", "Other"};
        final String tab1Names[] = {"Available", "Onhold", "Sold"};
        final String tab2Names[] = {"Friends"};
        final String tab3Names[] = {"Sold", "Bought", "Onhold"};
        final String tab4Names[] = {"Wishlist"};

        @Override
        public Fragment getItem(int position) {
            if (selector == R.id.nav_home_fragment) {
                if (position == 0) {
                    return new TopTimelineFragment();
                }
                else {
                    return CategoriesTimelineFragment.newInstance(tab0Names[position]);
                }
            }
            else if (selector == R.id.nav_catalog_fragment) {
                //user's own catalog
                String currentUserObjID = ParseUser.getCurrentUser().getObjectId();
                if (position == 0)
                    return UserCatalogFragment.newInstance(currentUserObjID,"Available");
                else if (position == 1)
                    return UserCatalogFragment.newInstance(currentUserObjID,"On hold");
                else
                    return UserCatalogFragment.newInstance(currentUserObjID,"Sold");
            }
            else if (selector == R.id.nav_friends_fragment) {
                return new FriendsListFragment();
            }
            else if (selector == R.id.nav_transaction_status_fragment) {
                String currentUserObjID = ParseUser.getCurrentUser().getObjectId();
                if (position == 0) //Sold
                    return UserCatalogFragment.newInstance(currentUserObjID,"Sold");
                else if (position == 1) //Bought
                    return UserCatalogFragment.newInstance(currentUserObjID,"Bought");
                else //on hold
                    return UserCatalogFragment.newInstance(currentUserObjID,"On hold");
            }
            else if (selector == R.id.nav_wishlist_fragment) {
                return new WishListFragment();
            }
            else
                return null;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            if (selector == R.id.nav_home_fragment)
                return tab0Names.length;
            else if (selector == R.id.nav_catalog_fragment)
                return tab1Names.length;
            else if (selector == R.id.nav_friends_fragment)
                return tab2Names.length;
            else if (selector == R.id.nav_transaction_status_fragment)
                return tab3Names.length;
            else if (selector == R.id.nav_wishlist_fragment)
                return tab4Names.length;
            else
                return 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (selector == R.id.nav_home_fragment) {
                return tab0Names[position];
            }
            else if (selector == R.id.nav_catalog_fragment) {
                return tab1Names[position];
            }
            else if (selector == R.id.nav_friends_fragment) {
                return tab2Names[position];
            }
            else if (selector == R.id.nav_transaction_status_fragment) {
                return tab3Names[position];
            }
            else {
                return tab4Names[position];
            }

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

        selector = R.id.nav_home_fragment;

        //viewpager setup
        fragmentStatePagerAdapter = new catalogPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(fragmentStatePagerAdapter); //may cause problems (?)

        tabStrip.setViewPager(viewpager);
        //set background and indicator color of tabstrip
        //tabStrip.setTabBackground(getResources().getColor(R.color.colorPrimary));
        tabStrip.setIndicatorColor(getResources().getColor(R.color.colorPrimary));
        //tabStrip.setDividerColor(getResources().getColor(R.color.colorPrimary));

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
        TextView tvNavItemsAvailable = (TextView) headerView.findViewById(R.id.tvNavItemsAvailable);


        ParseUser currUser = ParseUser.getCurrentUser();
        Picasso.with(this).load(currUser.getString("profilePicUrl")).fit().transform(new CropCircleTransformation()).into(ivNavProfilePic);
        tvNavUserName.setText(currUser.getString("username"));
        parseClient.countNumFriendsOfUser(currUser,tvNavNumFriends," friends");
        parseClient.countNumItemsOnStatus(currUser,"Sold",tvNavItemsSold," items sold");
        parseClient.countNumItemsOnStatus(currUser,"On hold",tvNavItemsOnhold," items on hold");
        parseClient.countNumItemsOnStatus(currUser,"Available",tvNavItemsAvailable," items available");

        //Launching profile activity when profile picture in the navigation drawer is clicked
        ivNavProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(NewsFeedActivity.this, ProfileActivity.class);
                i.putExtra("objectId",ParseUser.getCurrentUser().getObjectId());
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_feed, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search_nf);
        SearchView searchView = (SearchView) searchItem.getActionView();

        //make search box always show by default
        // user dont have to tap on the icon to make a search
        searchView.setIconifiedByDefault(true);

        //expand the SearchView
        searchItem.expandActionView();
        searchView.requestFocus();

        //TODO. Use of selector to be changed for better style
        //set hint text
        if (selector != R.id.nav_friends_fragment) {
            searchView.setQueryHint("Search for an item...");
        } else {
            searchView.setQueryHint("Search for a friend...");
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent i = new Intent(getApplicationContext(),SearchActivity.class);
                i.putExtra("query",query);
                i.putExtra("selector",selector);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
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
                        return true;
                    }
                });
    }


    public void selectDrawerItem(MenuItem item) {
        //TODO. Use of selector to be changed for better style
        switch(item.getItemId()) {
            case R.id.nav_home_fragment:
                selector = R.id.nav_home_fragment;
                break;
            case R.id.nav_catalog_fragment:
                selector = R.id.nav_catalog_fragment;
                break;
            case R.id.nav_friends_fragment:
                selector = R.id.nav_friends_fragment;
                break;
            case R.id.nav_transaction_status_fragment:
                selector = R.id.nav_transaction_status_fragment;
                break;
            case R.id.nav_wishlist_fragment:
                selector = R.id.nav_wishlist_fragment;
                break;
        }
        tabStrip.setViewPager(viewpager);
        fragmentStatePagerAdapter.notifyDataSetChanged();

        //highlight the selected item
        item.setChecked(true);
        setTitle(item.getTitle());
        drawerLayout.closeDrawers();
    }

    public void onCompose(View view) {
        if (selector == R.id.nav_friends_fragment) {
            Intent i = new Intent(NewsFeedActivity.this, FriendImportActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else {
            Intent i = new Intent(NewsFeedActivity.this, AddItemActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

}

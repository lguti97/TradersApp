package teamcool.tradego;

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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFeedActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.viewpager) ViewPager viewpager;
    @BindView(R.id.tabs) PagerSlidingTabStrip tabStrip;
    @BindView(R.id.newsfeed_drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.nvNewsFeedNavDrawer) NavigationView navDrawer;


    FragmentStatePagerAdapter fragmentStatePagerAdapter;


    public class catalogPagerAdapter extends FragmentStatePagerAdapter implements PagerSlidingTabStrip.IconTabProvider {
        final int PAGE_COUNT = 2;
        //if tab icons are needed:
        // private int tabIcons[] = {};
        private String tabNames[] = {"Top", "Categories"};

        public catalogPagerAdapter(FragmentManager fragmentManager) { super(fragmentManager); }


        //if tab icons are needed later..
        @Override
        public int getPageIconResId(int position) {
            return 0; //placeholder;
            //return tabIcons[position];
        }


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

        //viewpager setup
        fragmentStatePagerAdapter = new catalogPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(fragmentStatePagerAdapter); //may cause problems (?)
        tabStrip.setViewPager(viewpager);
        //set TabBackground and IndicatorColor later

        //setup navigation drawer
        setupNavDrawerTabs(navDrawer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_feed, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        //make search box always show by default
        // user dont have to tap on the icon to make a search
        searchView.setIconifiedByDefault(false);

        //expand the SearchView
        searchItem.expandActionView();
        searchView.requestFocus();

        //set hint text
        searchView.setQueryHint("Search for an item...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                //To be added
                return false;
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
        Class fragmentClass;
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
        getSupportFragmentManager().beginTransaction().replace(R.id.flcontent, fragment).commit();

        //highlight the selected item
        item.setChecked(true);
        setTitle(item.getTitle());
        drawerLayout.closeDrawers();
    }

}

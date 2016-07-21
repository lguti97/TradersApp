package teamcool.tradego.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.ButterKnife;
import teamcool.tradego.Fragments.FilterDialogFragment;
import teamcool.tradego.Fragments.FriendsListFragment;
import teamcool.tradego.Fragments.SearchItemsFragment;
import teamcool.tradego.R;

public class SearchActivity extends AppCompatActivity implements FilterDialogFragment.FilterDialogListener{

    int selector = 0;
    String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        query = getIntent().getStringExtra("query");
        selector = getIntent().getIntExtra("selector",0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(query);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (selector != 2) {
                ft.replace(R.id.flContainerSearch, SearchItemsFragment.newInstance(query,null));
            } else {
                ft.replace(R.id.flContainerSearch, FriendsListFragment.newInstance(query,null));
            }
            ft.commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem filterItem = menu.findItem(R.id.filter_search);
        filterItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showFilterDialog();
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void showFilterDialog(){
        FragmentManager fm = getSupportFragmentManager();
        FilterDialogFragment filterDF = FilterDialogFragment.newInstance("Filter");
        filterDF.show(fm, "filter_fragment_dialog");
    }

    @Override
    public void onFinishDialog(ArrayList<String> res) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (selector != 2) {
            ft.replace(R.id.flContainerSearch, SearchItemsFragment.newInstance(query,res));
        } else {
            ft.replace(R.id.flContainerSearch, FriendsListFragment.newInstance(query,res));
        }
        ft.commit();
    }
}

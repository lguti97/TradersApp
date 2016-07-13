package teamcool.tradego.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import teamcool.tradego.Adapters.CatalogAdapter;
import teamcool.tradego.Models.Item;
import teamcool.tradego.R;

/**
 * Created by selinabing on 7/6/16.
 */
public class CatalogListFragment extends Fragment {

    @BindView(R.id.rvItems)
    RecyclerView rvItems;
    //BindView to swipContainer SwipeRefreshLayout, toolbar

    private CatalogAdapter catalogAdapter;
    //private ArrayList<Item> items;
    private ArrayList<Item> items;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_catalog_list, container, false);
        ButterKnife.bind(this, v);
        rvItems.setAdapter(catalogAdapter);
        rvItems.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        rvItems.setHasFixedSize(true);
        //listViewHere.setAdapter(catalogAdapter);
        //set listView's onItemLongClickListener, onItemClickListener, etc.
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = new ArrayList<>();
        catalogAdapter = new CatalogAdapter(items);

        //set layout to StaggeredGrid

        //rvItems.addOnScrollLisnener for endless scrolling
        //swipeContainer set on refresh listener
        //swipeContainer setColorSchemeResources to configure refreshing colors
    }

    public void addAll(List<Item> items) {
        catalogAdapter.clearAndAddAll(items);
    }

    public void addItem (int index, Item item) {
        catalogAdapter.insert(index,item);
    }

}

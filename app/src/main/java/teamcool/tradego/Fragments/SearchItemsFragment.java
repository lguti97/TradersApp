package teamcool.tradego.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import teamcool.tradego.Clients.ParseClient;
import teamcool.tradego.Models.Item;
import teamcool.tradego.R;

public class SearchItemsFragment extends CatalogListFragment {

    List<Item> items;
    ParseClient parseClient;

    @BindView(R.id.swipeContainerCatalog) SwipeRefreshLayout swipeContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseClient = new ParseClient();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog_list, container, false);
        ButterKnife.bind(this,view);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        populate();
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populate();
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        super.onViewCreated(view, savedInstanceState);
        super.onViewCreated(view, savedInstanceState);
    }

    public static SearchItemsFragment newInstance(String query) {
        SearchItemsFragment searchItemsFragment = new SearchItemsFragment();
        Bundle args = new Bundle();
        args.putString("query",query);
        searchItemsFragment.setArguments(args);
        return searchItemsFragment;
    }

    private void populate() {
        String query = getArguments().getString("query");
        items = parseClient.queryItemsOnName(query,false);
        addAll(items);
        swipeContainer.setRefreshing(false);
    }

}

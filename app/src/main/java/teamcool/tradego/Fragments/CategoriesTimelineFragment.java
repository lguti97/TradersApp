package teamcool.tradego.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import teamcool.tradego.Clients.ParseClient;
import teamcool.tradego.Models.Item;
import teamcool.tradego.R;

/**
 * Created by selinabing on 7/8/16.
 */
public class CategoriesTimelineFragment extends CatalogListFragment {

    List<Item> items;
    ParseClient parseClient;

    @BindView(R.id.swipeContainerCatalog) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.ivNoItems) ImageView ivNoItems;

    public CategoriesTimelineFragment() {

    }

    public static CategoriesTimelineFragment newInstance (String category) {
        CategoriesTimelineFragment frag = new CategoriesTimelineFragment();
        Bundle args = new Bundle();
        args.putString("category",category);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
        //if swipe container exists, must setOnRefreshListener here, not onCreateView or onCreate
        populate(getArguments().getString("category"));
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populate(getArguments().getString("category"));
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        super.onViewCreated(view, savedInstanceState);
        super.onViewCreated(view, savedInstanceState);
    }

    public void populate(String category) {
        items = parseClient.queryItemsOnCategory(category);
        addAll(items);
        swipeContainer.setRefreshing(false);
        Log.d("DEBUG","reached for debugging - CATEGORY : "+items.size());
        if (items.size() == 0) {
            Picasso.with(getContext()).load(R.drawable.ic_home).into(ivNoItems);
            Log.d("DEBUG","reached category frag - TBDELETED");
        }
    }

}

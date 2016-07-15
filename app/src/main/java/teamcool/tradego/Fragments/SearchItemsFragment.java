package teamcool.tradego.Fragments;

import android.os.Bundle;

import java.util.List;

import teamcool.tradego.Clients.ParseClient;
import teamcool.tradego.Models.Item;

public class SearchItemsFragment extends CatalogListFragment {

    List<Item> items;
    ParseClient parseClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseClient = new ParseClient();
        populate();
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
        items = parseClient.queryItemsInDatabaseOnName(query);
        addAll(items);
    }

}

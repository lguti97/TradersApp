package teamcool.tradego.Fragments;

import android.os.Bundle;

public class SearchItemsFragment extends CatalogListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //populate();
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
        //
    }

}

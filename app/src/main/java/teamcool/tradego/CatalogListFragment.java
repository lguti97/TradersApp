package teamcool.tradego;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by selinabing on 7/6/16.
 */
public class CatalogListFragment extends Fragment {

    //BindView(R.id.listViewHere) TypeOfListView listViewHere;
    private CatalogAdapter catalogAdapter;
    private ArrayList<Item> items;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog_list, container, false);
        ButterKnife.bind(this,view);
        //listViewHere.setAdapter(catalogAdapter);
        //set listView's onItemLongClickListener, onItemClickListener, etc.
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = new ArrayList<>();
        catalogAdapter = new CatalogAdapter(items);
    }
}

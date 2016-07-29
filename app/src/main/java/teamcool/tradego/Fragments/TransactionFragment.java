package teamcool.tradego.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.parse.ParseUser;

import teamcool.tradego.Clients.ParseClient;
import teamcool.tradego.R;

/**
 * Created by kshah97 on 7/29/16.
 *
 */


public class TransactionFragment extends Fragment {

    ParseClient parseClient;
    ParseUser user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseClient = new ParseClient();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user = ParseUser.getCurrentUser();


        int sold = parseClient.countNumItemsOnStatus(user, "Sold");
        int on_hold = parseClient.countNumItemsOnStatus(user, "On hold");
        int not_sold = parseClient.countNumItemsOnStatus(user, "Available");
        int bought = parseClient.countNumItemsBought(user);

        GraphView graph = (GraphView) getView().findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(new DataPoint[] {

                new DataPoint(0, sold),
                new DataPoint(1, on_hold),
                new DataPoint(2, not_sold),
                new DataPoint(3, 4),
                new DataPoint(4, 5)

               });
        graph.addSeries(series);



        series.setSpacing(50);
        // draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"Sold", "On hold", "Not Sold", "Bought", "Friends"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

    }
}

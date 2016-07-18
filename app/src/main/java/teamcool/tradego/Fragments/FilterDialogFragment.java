package teamcool.tradego.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import teamcool.tradego.R;

/**
 * Created by selinabing on 7/15/16.
 */
public class FilterDialogFragment extends DialogFragment implements View.OnClickListener {

    public interface FilterDialogListener {
        void onFinishDialog();
    }

    @BindView(R.id.btnFilterBack) Button btnFilterBack;
    @BindView(R.id.btnSubmitFilter) Button btnSubmitFilter;

    public FilterDialogFragment () {

    }

    public static FilterDialogFragment newInstance(String title) {
        FilterDialogFragment frag = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putString("title",title);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_fragment_dialog, container, false);
        ButterKnife.bind(this,view);

        btnFilterBack.setOnClickListener(this);
        btnSubmitFilter.setOnClickListener(this);

        getDialog().setTitle(getArguments().getString("title"));
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnFilterBack:
                backWithoutResults();
                break;
            case R.id.btnSubmitFilter:
                sendBackResults();
        }
    }

    public void backWithoutResults(){
        FilterDialogListener listener = (FilterDialogListener) getActivity();
        listener.onFinishDialog(); //null
        dismiss();
    }

    public void sendBackResults() {
        FilterDialogListener listener = (FilterDialogListener) getActivity();
        listener.onFinishDialog(); //to be changed
        dismiss();
    }

}

package teamcool.tradego.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import teamcool.tradego.R;

/**
 * Created by selinabing on 7/15/16.
 */
public class FilterDialogFragment extends DialogFragment implements View.OnClickListener {

    public interface FilterDialogListener {
        void onFinishDialog(ArrayList<String> res);
    }

    @BindView(R.id.spnrCategoryType) Spinner spnrCategoryType;
    @BindView(R.id.spnrSortOrder) Spinner spnrSortOrder;
    @BindView(R.id.etOwner) EditText etOwner;
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

        hideKeyboard(view);

        btnFilterBack.setOnClickListener(this);
        btnSubmitFilter.setOnClickListener(this);

        getDialog().setTitle(getArguments().getString("title"));

        /*
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    Log.d("DEBUG","life");
                } else {
                    Log.d("DEBUG","everything will be okay");
                }
                hideKeyboard(view);
            }
        });
        */

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        etOwner.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    Log.d("DEBUG","keyboard focus changed - et");
                    hideKeyboard(view);
                } else {
                    Log.d("DEBUG","keyboard focus changed - et - b");
                }
            }
        });

        getView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    Log.d("DEBUG","life");
                } else {
                    Log.d("DEBUG","everything will be okay");
                }
                hideKeyboard(view);
            }
        });
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        hideKeyboard(getView());
        super.onActivityCreated(savedInstanceState);
    }

    public void hideKeyboard(View view) {
        Log.d("DEBUG","hiding keyboard in hideKeyboard");
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public void backWithoutResults(){
        FilterDialogListener listener = (FilterDialogListener) getActivity();
        listener.onFinishDialog(null);
        dismiss();
    }

    public void sendBackResults() {
        FilterDialogListener listener = (FilterDialogListener) getActivity();
        ArrayList<String> res = new ArrayList<>();
        res.add(spnrCategoryType.getSelectedItem().toString());
        res.add(spnrSortOrder.getSelectedItem().toString());
        res.add(etOwner.toString());
        listener.onFinishDialog(res);
        dismiss();
    }

}

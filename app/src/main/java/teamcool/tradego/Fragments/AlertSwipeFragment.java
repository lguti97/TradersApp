package teamcool.tradego.Fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import teamcool.tradego.Activities.FriendImportActivity;
import teamcool.tradego.R;

/**
 * Created by lguti on 7/26/16.
 */
public class AlertSwipeFragment extends DialogFragment {
    public AlertSwipeFragment() {
        // Empty constructor required for DialogFragment
    }

    public static AlertSwipeFragment newInstance(String title) {
        AlertSwipeFragment frag = new AlertSwipeFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title = getArguments().getString("title");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        //One way to inflate own custom XML file.
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_alert, null);
        alertDialogBuilder.setView(view);
        TextView tvAlert = (TextView) view.findViewById(R.id.tvAlert);
        if(tvAlert.getParent()!=null)
            ((ViewGroup)tvAlert.getParent()).removeView(tvAlert);

        alertDialogBuilder.setTitle(title);
        //alertDialogBuilder.setMessage("Swipe right to add Friends");
        alertDialogBuilder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //DO something
            }
        });
        alertDialogBuilder.setView(tvAlert);
        return alertDialogBuilder.create();
    }

}

package teamcool.tradego.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import teamcool.tradego.Clients.ParseClient;
import teamcool.tradego.R;

public class AlertDeleteFragment extends DialogFragment {

    public AlertDeleteFragment() {

    }

    public static AlertDeleteFragment newInstance(boolean isItem, String identifier, String id) {
        AlertDeleteFragment frag = new AlertDeleteFragment();
        Bundle args = new Bundle();
        args.putBoolean("isItem",isItem);
        args.putString("identifier",identifier);
        args.putString("id",id);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final ParseClient parseClient = new ParseClient();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Delete?");
        alertDialogBuilder.setMessage(String.format("Are you sure you want to delete %s?",getArguments().getString("identifier")));
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (getArguments().getBoolean("isItem")) {
                    parseClient.deleteItem(getArguments().getString("id"));
                } else {
                    parseClient.deleteFriend(getArguments().getString("id"));
                }
                getActivity().finish();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        return alertDialogBuilder.create();
    }
}

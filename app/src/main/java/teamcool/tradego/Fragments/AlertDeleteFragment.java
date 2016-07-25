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

    public static AlertDeleteFragment newInstance(String itemId) {
        AlertDeleteFragment frag = new AlertDeleteFragment();
        Bundle args = new Bundle();
        args.putString("itemId",itemId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final ParseClient parseClient = new ParseClient();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Delete?");
        alertDialogBuilder.setMessage("Are you sure you want to delete this item?");
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                parseClient.deleteItem(getArguments().getString("itemId"));
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

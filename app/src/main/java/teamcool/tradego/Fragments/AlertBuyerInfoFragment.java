package teamcool.tradego.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import teamcool.tradego.Clients.ParseClient;

/**
 * Created by selinabing on 8/1/16.
 */
public class AlertBuyerInfoFragment extends DialogFragment {


    public AlertBuyerInfoFragment() {

    }

    public static AlertBuyerInfoFragment newInstance() {
        AlertBuyerInfoFragment frag = new AlertBuyerInfoFragment();
        Bundle args = new Bundle();
        //
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final ParseClient parseClient = new ParseClient();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Buyer Info");
        alertDialogBuilder.setMessage(String.format("Who bought this item?",getArguments().getString("identifier")));
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

package at.benjaminpotzmann.odermanager.deliveryclient.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import at.benjaminpotzmann.odermanager.deliveryclient.R;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Address;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Town;

/**
 * Created by Giymo11 on 25.02.14.
 */
public class CreateAddressDialogFragment extends DialogFragment {

    public static final String ARG_TOWN = "town";

    public interface CreateAddressDialogListener {
        public void onCreateAddress(Address address);
    }

    // Use this instance of the interface to deliver action events
    private CreateAddressDialogListener listener;
    private Town town;
    private EditText street;

    public static CreateAddressDialogFragment newInstance(Town town) {
        CreateAddressDialogFragment fragment = new CreateAddressDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TOWN, town);
        fragment.setArguments(args);
        return fragment;
    }

    // Override the Fragment.onAttach() method to instantiate the CreateAddressDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the CreateAddressDialogListener so we can send events to the host
            listener = (CreateAddressDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement CreateAddressDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // you have to use getArguments() instead of savedInstanceState!
        town = (Town) getArguments().getSerializable(ARG_TOWN);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_createaddress, null);

        TextView townView = (TextView) view.findViewById(R.id.createaddress_town);
        townView.setText(town.toString());

        street = (EditText) view.findViewById(R.id.createaddress_street);

        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_createaddress)
                .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            // Send the positive button event back to the host activity
                            listener.onCreateAddress(new Address(0, town.getId(), parseStreet(street.getText()), parseHouseNumber(street.getText())));
                        } catch (IllegalArgumentException ex) {
                            Toast.makeText(getActivity(), R.string.error_createaddress, Toast.LENGTH_LONG).show();
                        }

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        dismiss();
                    }
                })
                .setView(view);

        return builder.create();
    }

    private static String parseStreet(CharSequence text) {
        String[] strings = text.toString().split(" ");
        if (strings.length <= 1)
            throw new IllegalArgumentException("The supplied text has to contain street as well as house-number!");
        String s = "";
        for (int i = 0; i < strings.length - 1; ++i)
            s = s + strings[i];
        return s;
    }

    private static String parseHouseNumber(CharSequence text) {
        String[] strings = text.toString().split(" ");
        if (strings.length <= 1)
            throw new IllegalArgumentException("The supplied text has to contain street as well as house-number!");
        return strings[strings.length - 1];
    }
}

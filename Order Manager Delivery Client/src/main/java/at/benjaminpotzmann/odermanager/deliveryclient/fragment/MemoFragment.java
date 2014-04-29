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
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Order;

/**
 * Created by Gizmo on 2014-04-29.
 */
public class MemoFragment extends DialogFragment {

    public static final String ARG_ORDER = "order";

    public interface MemoDialogListener {
        public void onSave(String memoForCustomer);
    }

    // Use this instance of the interface to deliver action events
    private MemoDialogListener listener;
    private Order order;

    public static MemoFragment newInstance(Order order) {
        MemoFragment fragment = new MemoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ORDER, order);
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
            listener = (MemoDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement CreateAddressDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // you have to use getArguments() instead of savedInstanceState!
        order = (Order) getArguments().getSerializable(ARG_ORDER);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_memo, null);

        TextView memo = (TextView) view.findViewById(R.id.memo);
        final EditText reply = (EditText) view.findViewById(R.id.memo_reply);

        memo.setText(order.getMemoForPock());
        if (order.getMemoForCustomer() != null && !order.getMemoForCustomer().equals(""))
            reply.setText(order.getMemoForCustomer());

        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Memo")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            // Send the positive button event back to the host activity
                            listener.onSave(reply.getText().toString());
                        } catch (IllegalArgumentException ex) {
                            Toast.makeText(getActivity(), R.string.error_savememo, Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        dismiss();
                    }
                })
                .setView(view);

        return builder.create();
    }
}

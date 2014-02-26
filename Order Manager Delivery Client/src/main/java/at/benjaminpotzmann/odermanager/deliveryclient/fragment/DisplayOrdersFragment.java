package at.benjaminpotzmann.odermanager.deliveryclient.fragment;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import at.benjaminpotzmann.odermanager.deliveryclient.R;
import at.benjaminpotzmann.odermanager.deliveryclient.adapter.OrderAdapter;
import at.benjaminpotzmann.odermanager.deliveryclient.dao.DaoStub;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Address;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Order;
import at.benjaminpotzmann.odermanager.deliveryclient.helper.PriceFormatHelper;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link at.benjaminpotzmann.odermanager.deliveryclient.fragment.DisplayOrdersFragment.OnFragmentInteractionListener}
 * interface.
 */
public class DisplayOrdersFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ADDRESS = "address";

    /**
     * The fragment's ListView/GridView.
     */
    private ListView listView;
    private TextView textView;
    private Address address;
    private OnFragmentInteractionListener listener;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private OrderAdapter adapter;

    public static DisplayOrdersFragment newInstanceForAddress(Address address) {
        DisplayOrdersFragment fragment = new DisplayOrdersFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ADDRESS, address);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DisplayOrdersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            address = (Address) getArguments().getSerializable(ARG_ADDRESS);
            adapter = new OrderAdapter(getActivity(), R.layout.item_order, address);
        }
    }

    private double calcSum(Address address) {
        double sum = 0;
        List<Order> orders = DaoStub.getInstance().getOrdersForAddress(address);
        for (Order order : orders) {
            sum += order.getTotalPrice();
        }
        return sum;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_displayorders, container, false);

        // Set the adapter
        listView = (ListView) view.findViewById(android.R.id.list);

        textView = (TextView) view.findViewById(R.id.displayorders_sum);
        updateSum();

        // Set OnItemClickListener so we can be notified on item clicks
        //listView.setOnItemClickListener(this);

        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                updateSum();
            }
        });

        Button deliverButton = new Button(getActivity());
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            deliverButton.setTextAppearance(getActivity(), android.R.attr.borderlessButtonStyle);
        } else {
            deliverButton.setTextAppearance(getActivity(), android.R.attr.textAppearanceLarge);
        }
        int horizontalPaddingInPixels = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        int verticalPaddingInPixels = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
        deliverButton.setPadding(horizontalPaddingInPixels, verticalPaddingInPixels, horizontalPaddingInPixels, verticalPaddingInPixels);
        deliverButton.setText(R.string.displayorders_deliver);
        deliverButton.setBackgroundColor(getResources().getColor(android.R.color.white));
        deliverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaoStub.getInstance().setDeliveredForAddress(address);
                Toast.makeText(getActivity(), "" + address + " delivered", Toast.LENGTH_SHORT).show();
                listener.onFragmentInteraction();
            }
        });

        listView.addFooterView(deliverButton);

        ((AdapterView<ListAdapter>) listView).setAdapter(adapter);


        return view;
    }

    private void updateSum() {
        textView.setText("" + PriceFormatHelper.format(calcSum(address)));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    /*@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != listener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            listener.onFragmentInteraction((Order) parent.getItemAtPosition(position));
        }
    }*/

    public void addOrder(Order order) {
        adapter.add(order);
    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction();
    }

}

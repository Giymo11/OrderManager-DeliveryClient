package at.benjaminpotzmann.odermanager.deliveryclient.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import at.benjaminpotzmann.odermanager.deliveryclient.R;
import at.benjaminpotzmann.odermanager.deliveryclient.dao.AddressDaoStub;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Address;


/**
 * Created by Giymo11 on 11.02.14.
 * The Fragment used to display the customers' addresses
 */
public class CustomerFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String ARG_TOWN = "town";

    /**
     * @param address The town which this Fragment displays. Only Zipcode and Location are used.
     * @return The initialized Fragment
     */
    public static CustomerFragment newInstanceForTown(Address address) {
        CustomerFragment fragment = new CustomerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TOWN, address);
        fragment.setArguments(args);
        return fragment;
    }

    private Address town;
    private ListAdapter adapter;
    private AbsListView listView;
    private OnFragmentInteractionListener listener;
    private AddressDaoStub dao = new AddressDaoStub();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            town = (Address) getArguments().getSerializable(ARG_TOWN);

        adapter = new ArrayAdapter<Address>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, dao.getAddressesForTown(town));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_product, container, false);

        // Set the adapter
        listView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) listView).setAdapter(adapter);

        // Set OnItemClickListener so we can be notified on item clicks
        listView.setOnItemClickListener(this);

        return view;
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != listener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            listener.onFragmentInteraction((Address) parent.getItemAtPosition(position));
        }
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
        public void onFragmentInteraction(Address address);
    }
}

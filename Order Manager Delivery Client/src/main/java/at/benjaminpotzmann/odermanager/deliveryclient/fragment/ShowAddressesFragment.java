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

import java.util.Comparator;

import at.benjaminpotzmann.odermanager.deliveryclient.R;
import at.benjaminpotzmann.odermanager.deliveryclient.adapter.AddressAdapter;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Address;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Town;
import at.benjaminpotzmann.odermanager.deliveryclient.helper.NumberAwareStringComparator;
import at.benjaminpotzmann.odermanager.deliveryclient.services.CachingService;


/**
 * Created by Giymo11 on 11.02.14.
 * The Fragment used to display the customers' addresses
 */
public class ShowAddressesFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String ARG_TOWN = "town";

    /**
     * @param town The town which this Fragment displays. Only Zipcode and Location are used.
     * @return The initialized Fragment
     */
    public static ShowAddressesFragment newInstanceForTown(Town town) {
        ShowAddressesFragment fragment = new ShowAddressesFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TOWN, town);
        fragment.setArguments(args);
        return fragment;
    }

    public class AddressComparator implements Comparator<Address> {
        @Override
        public int compare(Address lhs, Address rhs) {
            int result = 0;
            result = lhs.getTown().getZipcode() - rhs.getTown().getZipcode();
            if (result == 0) {
                result = lhs.getTown().getName().toLowerCase().compareTo(rhs.getTown().getName().toLowerCase());
                if (result == 0) {
                    result = lhs.getStreet().toLowerCase().compareTo(rhs.getStreet().toLowerCase());
                    if (result == 0)
                        result = NumberAwareStringComparator.INSTANCE.compare(lhs.getHouseNr().toLowerCase(), rhs.getHouseNr().toLowerCase());
                }
            }
            return result;
        }
    }

    private Town town;
    private ArrayAdapter<Address> adapter;
    private AbsListView listView;
    private OnFragmentInteractionListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            town = (Town) getArguments().getSerializable(ARG_TOWN);

        /*adapter = new ArrayAdapter<Address>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, CachingService.getInstance().getAddressesForTownId(town.getId()));*/

        adapter = new AddressAdapter(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, CachingService.getInstance().getAddressesForTownId(town.getId()));

        adapter.sort(new AddressComparator());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_address, container, false);

        // Set the adapter
        listView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) listView).setAdapter(adapter);

        // Set OnItemClickListener so we can be notified on item clicks
        listView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        listView.invalidate();
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
            listener.onAddressPicked((Address) parent.getItemAtPosition(position));
        }
    }

    public void addAddress(Address address) {
        adapter.add(address);
        adapter.sort(new AddressComparator());
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
        public void onAddressPicked(Address address);
    }
}

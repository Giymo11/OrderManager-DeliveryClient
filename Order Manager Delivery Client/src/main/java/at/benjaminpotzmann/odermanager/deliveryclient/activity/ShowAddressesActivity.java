package at.benjaminpotzmann.odermanager.deliveryclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import java.util.List;

import at.benjaminpotzmann.odermanager.deliveryclient.R;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Address;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Town;
import at.benjaminpotzmann.odermanager.deliveryclient.fragment.CreateAddressDialogFragment;
import at.benjaminpotzmann.odermanager.deliveryclient.fragment.ShowAddressesFragment;
import at.benjaminpotzmann.odermanager.deliveryclient.services.CachingService;

public class ShowAddressesActivity extends ActionBarActivity implements ActionBar.OnNavigationListener, ShowAddressesFragment.OnFragmentInteractionListener, CreateAddressDialogFragment.CreateAddressDialogListener {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * current dropdown position.
     */
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
    private List<Town> townList;
    private ShowAddressesFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showaddresses);

        townList = CachingService.getInstance().getTowns();

        // Set up the action bar to show a dropdown list.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        // Specify a SpinnerAdapter to populate the dropdown list.
        SpinnerAdapter adapter = new ArrayAdapter<Town>(actionBar.getThemedContext(), android.R.layout.simple_list_item_1, android.R.id.text1, townList);

        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(adapter, this);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore the previously serialized current dropdown position.
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getSupportActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Serialize the current dropdown position.
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
                getSupportActionBar().getSelectedNavigationIndex());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItemCompat.setShowAsAction(
                menu.add("Add Address").setIcon(android.R.drawable.ic_menu_add).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        addAddress(item);
                        return true;
                    }
                }),
                MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    private void addAddress(MenuItem item) {
        CreateAddressDialogFragment dialogFragment = CreateAddressDialogFragment.newInstance(
                townList.get(getSupportActionBar().getSelectedNavigationIndex()));
        dialogFragment.show(getSupportFragmentManager(), "create_address");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        // When the given dropdown item is selected, show its contents in the
        // container view.
        fragment = ShowAddressesFragment.newInstanceForTown(CachingService.getInstance().getTowns().get(position));
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

        return true;
    }

    @Override
    public void onAddressPicked(Address address) {
        //Toast.makeText(this, address.toString(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, DisplayOrdersActivity.class);
        intent.putExtra(DisplayOrdersActivity.EXTRA_ADDRESS, address);
        startActivity(intent);
    }

    @Override
    public void onCreateAddress(Address address) {
        CachingService.getInstance().addAddress(address);
        if (fragment != null)
            fragment.addAddress(address);
        onAddressPicked(address);
    }
}

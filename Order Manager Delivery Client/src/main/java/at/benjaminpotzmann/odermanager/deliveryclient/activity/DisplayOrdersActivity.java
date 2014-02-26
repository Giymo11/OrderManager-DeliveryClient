package at.benjaminpotzmann.odermanager.deliveryclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import at.benjaminpotzmann.odermanager.deliveryclient.R;
import at.benjaminpotzmann.odermanager.deliveryclient.dao.DaoStub;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Address;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Order;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Product;
import at.benjaminpotzmann.odermanager.deliveryclient.fragment.DisplayOrdersFragment;

public class DisplayOrdersActivity extends ActionBarActivity implements DisplayOrdersFragment.OnFragmentInteractionListener {

    private static final String TAG = "deliveryclient.DisplayOrdersActivity";
    public static String EXTRA_ADDRESS = "address";
    private Address address;
    private DisplayOrdersFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayorders);

        address = (Address) getIntent().getSerializableExtra(EXTRA_ADDRESS);
        fragment = DisplayOrdersFragment.newInstanceForAddress(address);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_orders, menu);

        MenuItemCompat.setShowAsAction(
                menu.add("Add Product").setIcon(android.R.drawable.ic_menu_add).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        addProduct(item);
                        return true;
                    }
                }),
                MenuItemCompat.SHOW_AS_ACTION_ALWAYS);

        MenuItemCompat.setShowAsAction(
                menu.add(R.string.undo).setIcon(android.R.drawable.ic_menu_revert).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        DaoStub.getInstance().undoDelivery(address);
                        fragment.notifyDataSetChanged();
                        return true;
                    }
                }),
                MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public void addProduct(MenuItem item) {
        Toast.makeText(this, "Please pick a product?", Toast.LENGTH_LONG).show();
        startActivityForResult(new Intent(this, PickProductActivity.class), PickProductActivity.REQUEST_PRODUCTS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PickProductActivity.REQUEST_PRODUCTS && resultCode == PickProductActivity.RESULT_OK) {
            Product product = (Product) data.getSerializableExtra(PickProductActivity.EXTRA_PRODUCT);
            Log.d(TAG, product.toString());
            Order order = DaoStub.getInstance().addProductForAddress(product, address);
            if (order != null) {
                Log.d(TAG, order.toFullString());
                fragment.addOrder(order);
            } else
                fragment.notifyDataSetChanged();
        }
    }

    @Override
    public void onFragmentInteraction() {
        finish();
    }
}

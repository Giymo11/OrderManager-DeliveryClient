package at.benjaminpotzmann.odermanager.deliveryclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import at.benjaminpotzmann.odermanager.deliveryclient.R;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Product;
import at.benjaminpotzmann.odermanager.deliveryclient.fragment.PickProductFragment;

public class PickProductActivity extends ActionBarActivity implements PickProductFragment.OnFragmentInteractionListener {

    public static final int REQUEST_PRODUCTS = 1;
    public static final String EXTRA_PRODUCT = "product";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickproduct);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PickProductFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pick_product, menu);
        return true;
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
    public void onProductPicked(Product product) {
        Intent result = new Intent(EXTRA_PRODUCT);
        result.putExtra(EXTRA_PRODUCT, product);
        setResult(RESULT_OK, result);
        finish();
    }
}

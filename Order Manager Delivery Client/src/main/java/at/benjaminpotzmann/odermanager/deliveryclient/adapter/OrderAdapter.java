package at.benjaminpotzmann.odermanager.deliveryclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import at.benjaminpotzmann.odermanager.deliveryclient.R;
import at.benjaminpotzmann.odermanager.deliveryclient.dao.DaoStub;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Address;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Order;
import at.benjaminpotzmann.odermanager.deliveryclient.helper.PriceFormatHelper;

/**
 * Created by Giymo11 on 21.02.14.
 */
public class OrderAdapter extends ArrayAdapter<Order> {

    private static final String TAG = "OrderAdapter";
    private Address address = null;
    private int resource;

    public OrderAdapter(Context context, int resource, List<Order> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    public OrderAdapter(Context context, int resource, Address address) {
        super(context, resource, DaoStub.getInstance().getOrdersForAddress(address));
        this.address = address;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(resource, null);
        }
        final Order order = this.getItem(position);

        final TextView name = (TextView) convertView.findViewById(R.id.displayorders_list_ordername);
        final TextView singleprice = (TextView) convertView.findViewById(R.id.displayorders_list_singleprice);
        final TextView quantity = (TextView) convertView.findViewById(R.id.displayorders_list_quantity);
        final Button more = (Button) convertView.findViewById(R.id.displayorders_list_more);
        final Button less = (Button) convertView.findViewById(R.id.displayorders_list_less);
        final CheckBox delivered = (CheckBox) convertView.findViewById(R.id.displayorders_list_delivered);
        final RelativeLayout background = (RelativeLayout) convertView.findViewById(R.id.displayorders_list_background);

        name.setText(order.getProduct().getName());
        singleprice.setText("á " + PriceFormatHelper.format(order.getProduct().getPrice()));
        //Don't delete the "" + as it is necessary, otherwise it thinks it is a resource id and fails.
        quantity.setText("" + order.getQuantity());
        quantity.setTag(order.getQuantity());
        background.setBackgroundColor(getContext().getResources().getColor(android.R.color.background_light));
        delivered.setChecked(order.isDelivered());

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.increaseQuantity();
                updateBackgroundColor(order, quantity, background);
                notifyDataSetChanged();
            }
        });

        less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.decreaseQuantity();
                updateBackgroundColor(order, quantity, background);
                notifyDataSetChanged();
            }
        });

        delivered.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                order.setDelivered(isChecked);
            }
        });

        return convertView;
    }

    private void updateBackgroundColor(Order order, TextView quantity, RelativeLayout background) {
        if (order.getQuantity() == (Integer) quantity.getTag())
            background.setBackgroundColor(getContext().getResources().getColor(android.R.color.background_light));
        else
            background.setBackgroundColor(getContext().getResources().getColor(R.color.om_bg_orange_light));
    }
}

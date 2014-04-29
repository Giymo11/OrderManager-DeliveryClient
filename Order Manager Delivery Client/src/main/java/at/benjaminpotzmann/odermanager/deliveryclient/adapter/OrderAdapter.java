package at.benjaminpotzmann.odermanager.deliveryclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import at.benjaminpotzmann.odermanager.deliveryclient.R;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Address;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.OrderItem;
import at.benjaminpotzmann.odermanager.deliveryclient.helper.PriceFormatHelper;
import at.benjaminpotzmann.odermanager.deliveryclient.services.CachingService;

/**
 * Created by Giymo11 on 21.02.14.
 */
public class OrderAdapter extends ArrayAdapter<OrderItem> {

    private static final String TAG = "OrderAdapter";
    private Address address = null;
    private int resource;

    public OrderAdapter(Context context, int resource, List<OrderItem> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    public OrderAdapter(Context context, int resource, Address address) {
        super(context, resource, CachingService.getInstance().getOrderItemsForOrder(CachingService.getInstance().getOrderForAddress(address.getId()).getId()));
        this.address = address;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(resource, null);
        }
        final OrderItem order = this.getItem(position);

        final TextView name = (TextView) convertView.findViewById(R.id.displayorders_list_ordername);
        final TextView originalOrder = (TextView) convertView.findViewById(R.id.displayorders_list_singleprice);
        final TextView quantity = (TextView) convertView.findViewById(R.id.displayorders_list_quantity);
        final Button more = (Button) convertView.findViewById(R.id.displayorders_list_more);
        final Button less = (Button) convertView.findViewById(R.id.displayorders_list_less);
        final RelativeLayout background = (RelativeLayout) convertView.findViewById(R.id.displayorders_list_background);

        name.setText(CachingService.getInstance().getProductForId(order.getProductid()).getTitle());
        originalOrder.setText(getContext().getResources().getString(R.string.orderadapter_ordered) + ": " + order.getOrdered() + " á " + PriceFormatHelper.format(CachingService.getInstance().getProductForId(order.getProductid()).getPrice()));
        // Don't delete the "" + as it is necessary, otherwise it thinks it is a resource id and fails.
        quantity.setText("" + showQuantity(order));
        updateBackgroundColor(order, background);

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.setDelivered(showQuantity(order) + 1);
                updateBackgroundColor(order, background);
                notifyDataSetChanged();
            }
        });

        less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.setDelivered(showQuantity(order) - 1);
                updateBackgroundColor(order, background);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private void updateBackgroundColor(OrderItem order, RelativeLayout background) {
        if (order.getOrdered() == order.getDelivered() || order.getDelivered() == OrderItem.NOT_DELIVERED)
            background.setBackgroundColor(getContext().getResources().getColor(android.R.color.background_light));
        else
            background.setBackgroundColor(getContext().getResources().getColor(R.color.om_bg_orange_light));
    }

    private int showQuantity(OrderItem order) {
        if (order.getDelivered() != OrderItem.NOT_DELIVERED)
            return order.getDelivered();
        else
            return order.getOrdered();
    }
}

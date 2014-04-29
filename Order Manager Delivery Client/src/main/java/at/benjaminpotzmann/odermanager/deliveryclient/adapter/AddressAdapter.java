package at.benjaminpotzmann.odermanager.deliveryclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import at.benjaminpotzmann.odermanager.deliveryclient.R;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Address;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Order;
import at.benjaminpotzmann.odermanager.deliveryclient.services.CachingService;

/**
 * Created by Gizmo on 2014-04-29.
 */
public class AddressAdapter extends ArrayAdapter<Address> {

    private int resource;

    public AddressAdapter(Context context, int resource, int textViewResourceId, List<Address> objects) {
        super(context, resource, textViewResourceId, objects);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(resource, null);
        }
        final Address address = this.getItem(position);

        TextView text = (TextView) convertView.findViewById(android.R.id.text1);
        text.setText(address.getStreet() + " " + address.getHouseNr());

        if (CachingService.getInstance().isAddressOrdering(address.getId())) {
            Order order = CachingService.getInstance().getOrderForAddress(address.getId());
            if (order.isDelivered())
                text.setBackgroundColor(getContext().getResources().getColor(R.color.holo_green_light));
            /* else if (order.getMemoForPock() != null && !order.getMemoForPock().equals(""))
                text.setBackgroundColor(getContext().getResources().getColor(R.color.om_bg_orange_light));
            */
            else
                text.setBackgroundColor(getContext().getResources().getColor(android.R.color.white));
        } else
            text.setBackgroundColor(getContext().getResources().getColor(android.R.color.darker_gray));

        return convertView;
    }
}

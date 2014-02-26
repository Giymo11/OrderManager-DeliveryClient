package at.benjaminpotzmann.odermanager.deliveryclient.dto;

import java.io.Serializable;

import at.benjaminpotzmann.odermanager.deliveryclient.helper.PriceFormatHelper;

/**
 * Created by Giymo11 on 20.02.14.
 * The DTO for a order
 */
public class Order implements Serializable {

    public static final int NOT_DELIVERED = -1;

    private Address address;
    private Product product;
    private int ordered;
    private int delivered;

    public Order(Address address, Product product, int ordered) {
        this.address = address;
        this.product = product;
        this.ordered = ordered;
        this.delivered = NOT_DELIVERED;
    }

    public int getDelivered() {
        return delivered;
    }

    public void setDelivered(int delivered) {
        this.delivered = delivered;
    }

    public Address getAddress() {
        return address;
    }

    public Product getProduct() {
        return product;
    }

    public int getOrdered() {
        return ordered;
    }

    public int increaseDelivered() {
        if (delivered == NOT_DELIVERED)
            delivered = ordered;
        return ++delivered;
    }

    public int decreaseDelivered() {
        if (delivered > 0)
            return --delivered;
        else
            return 0;
    }

    public void setOrdered(int ordered) {
        this.ordered = ordered;
    }

    public String toFullString() {
        return "Order{" +
                "address=" + address +
                ", product=" + product +
                ", ordered=" + ordered +
                ", delivered=" + delivered +
                '}';
    }

    @Override
    public String toString() {
        return "" + ordered + " mal " + product.getName() + " á " + product.getPrice() + " = " + PriceFormatHelper.format(getTotalPrice());
    }

    public double getTotalPrice() {
        return PriceFormatHelper.round((delivered == NOT_DELIVERED ? ordered : delivered) * product.getPrice());
    }
}

package at.benjaminpotzmann.odermanager.deliveryclient.dto;

import java.io.Serializable;

import at.benjaminpotzmann.odermanager.deliveryclient.helper.PriceFormatHelper;

/**
 * Created by Giymo11 on 20.02.14.
 * The DTO for a order
 */
public class Order implements Serializable {

    private Address address;
    private Product product;
    private int quantity;
    private boolean delivered = false;

    public Order(Address address, Product product, int quantity) {
        this.address = address;
        this.product = product;
        this.quantity = quantity;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public Address getAddress() {
        return address;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public int increaseQuantity() {
        return ++quantity;
    }

    public int decreaseQuantity() {
        if (quantity > 0)
            return --quantity;
        else
            return 0;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String toFullString() {
        return "Order{" +
                "address=" + address +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public String toString() {
        return "" + quantity + " mal " + product.getName() + " á " + product.getPrice() + " = " + PriceFormatHelper.format(product.getPrice() * quantity);
    }

    public double getTotalPrice() {
        return PriceFormatHelper.round(quantity * product.getPrice());
    }
}

package at.benjaminpotzmann.odermanager.deliveryclient.dto;

import java.io.Serializable;

/**
 * Created by Giymo11 on 20.02.14.
 */
public class Order implements Serializable {

    private Address address;
    private Product product;
    private int quantity;

    public Order(Address address, Product product, int quantity) {
        this.address = address;
        this.product = product;
        this.quantity = quantity;
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

    @Override
    public String toString() {
        return "Order{" +
                "address=" + address +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}

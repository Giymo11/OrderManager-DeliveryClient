package at.benjaminpotzmann.odermanager.deliveryclient.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Giymo11 on 20.02.14.
 * The DTO for a order
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

    public String toFullString() {
        return "Order{" +
                "address=" + address +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public String toString() {
        return "" + quantity + " mal " + product.getName() + " á " + product.getPrice() + " = " + format(product.getPrice() * quantity);
    }

    private static double round(double value) {
        BigDecimal biiig = new BigDecimal(value);
        biiig.setScale(2, BigDecimal.ROUND_HALF_UP);
        return biiig.doubleValue();
    }

    private static String format(double value) {
        return String.format("%.2f", round(value));
    }

    public double getTotalPrice() {
        return round(quantity * product.getPrice());
    }
}

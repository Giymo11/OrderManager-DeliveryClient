package at.benjaminpotzmann.odermanager.deliveryclient.dto;

import java.io.Serializable;

/**
 * Created by Giymo11 on 20.02.14.
 */
public class Product implements Serializable {

    private Category category;
    private String name;
    private double price;

    public Product(Category category, String name, double price) {
        this.category = category;
        this.name = name;
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "category=" + category +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}

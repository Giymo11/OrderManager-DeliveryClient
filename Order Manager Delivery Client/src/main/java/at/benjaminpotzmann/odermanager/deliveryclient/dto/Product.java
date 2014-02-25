package at.benjaminpotzmann.odermanager.deliveryclient.dto;

import java.io.Serializable;

import at.benjaminpotzmann.odermanager.deliveryclient.helper.PriceFormatHelper;

/**
 * Created by Giymo11 on 20.02.14.
 * The DTO for a product
 */
public class Product implements Serializable {

    private Category category;
    private String name;
    private double price;

    public Product(String name, double price, Category category) {
        this.category = category;
        this.name = name;
        this.price = price;
    }

    public Product(String name, double price) {
        this.category = null;
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
        return name + " á " + PriceFormatHelper.format(price) + "\nCategory " + category;
    }

    public String toFullString() {
        return "Product{" +
                "category=" + category +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Product.class != o.getClass()) return false;

        Product product = (Product) o;

        if (Double.compare(product.price, price) != 0) return false;
        if (category != null ? !category.equals(product.category) : product.category != null)
            return false;
        return !(name != null ? !name.equals(product.name) : product.name != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = category != null ? category.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}

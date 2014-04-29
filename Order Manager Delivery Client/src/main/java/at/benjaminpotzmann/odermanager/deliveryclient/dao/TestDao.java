package at.benjaminpotzmann.odermanager.deliveryclient.dao;

import java.util.ArrayList;
import java.util.List;

import at.benjaminpotzmann.odermanager.deliveryclient.dto.Address;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Category;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Order;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.OrderItem;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Product;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Town;

/**
 * Created by Gizmo on 2014-04-29.
 */
public class TestDao implements DaoInterface {

    private static TestDao instance;

    private TestDao() {

    }

    public static TestDao getInstance() {
        if (instance == null) {
            instance = new TestDao();
        }
        return instance;
    }

    @Override
    public List<Town> getTowns() {
        List<Town> towns = new ArrayList<Town>();

        towns.add(new Town(7503, "Großpetersdorf"));
        towns.add(new Town(7551, "Stegersbach"));
        towns.add(new Town(7551, "Bocksdorf"));
        towns.add(new Town(7533, "Ollersdorf"));

        for (int i = 0; i < towns.size(); ++i) {
            towns.get(i).setId(i + 1);
        }

        return towns;
    }

    @Override
    public List<Address> getAddressesForTownId(int id) {
        List<Address> addresses = new ArrayList<Address>();

        if (id == 1) {
            addresses.add(new Address(1, id, "Burgerstrasse", "6"));
        } else if (id == 2) {
            addresses.add(new Address(2, id, "Badsiedlung", "11/7"));
            addresses.add(new Address(3, id, "Badsiedlung", "11/8"));
            addresses.add(new Address(4, id, "Badsiedlung", "12/7"));
            addresses.add(new Address(5, id, "Hauptstraße", "25"));
        } else if (id == 3) {
            addresses.add(new Address(6, id, "Zickenbergen", "34"));
        } else if (id == 4) {
            addresses.add(new Address(7, id, "Neudauer Landstraße", "26"));
        }

        return addresses;
    }

    @Override
    public Order getCurrentOrderForAddressId(int id) {
        return new Order(id, 1, id, "Bitte an die Tür hängen", null, false);
    }

    @Override
    public List<OrderItem> getOrderItemsForOrderId(int id) {
        List<OrderItem> orderItems = new ArrayList<OrderItem>();

        orderItems.add(new OrderItem(id * 10 + 1, id, 1, 3, OrderItem.NOT_DELIVERED));
        orderItems.add(new OrderItem(id * 10 + 2, id, 4, 2, OrderItem.NOT_DELIVERED));

        return orderItems;
    }

    @Override
    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<Category>();

        categories.add(new Category(1, "Brote"));
        categories.add(new Category(2, "Gebäck"));
        categories.add(new Category(3, "Süßes"));

        return categories;
    }

    @Override
    public List<Product> getProductsForCategoryId(int id) {
        List<Product> products = new ArrayList<Product>();
        String pic = "noimage.png";

        if (id == 1) {
            products.add(new Product(1, id, 1, "Roggenbrot 0.5 kg", "Das Klassische Roggenbrot im handlichen Format", 0.8f, 1, true));
            products.add(new Product(2, id, 2, "Roggenbrot 1 kg", "Das Klassische Roggenbrot", 1.4f, 1, true));
            products.add(new Product(3, id, 3, "Mischbrot 0.5 kg", "Ein Mischbrot aus Weizen und Roggen", 1.2f, 1, true));
        } else if (id == 2) {
            products.add(new Product(4, id, 4, "Zimtschnecke", "Die zimtige Spezialität des Hauses", 0.9f, 1, true));
            products.add(new Product(5, id, 5, "Krapfen", "mit Marillenmarmelade gefüllt!", 0.5f, 1, true));
        } else if (id == 3) {
            products.add(new Product(6, id, 6, "Pizzastangerl", "Die perfekte Jause", 0.6f, 1, true));
            products.add(new Product(7, id, 7, "Kipferl", "Für Zwischendurch", 0.7f, 1, true));
        }

        return products;
    }
}

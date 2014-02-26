package at.benjaminpotzmann.odermanager.deliveryclient.dao;

import java.util.ArrayList;
import java.util.List;

import at.benjaminpotzmann.odermanager.deliveryclient.dto.Address;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Category;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Order;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Product;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Town;

/**
 * A sample dao for
 */
public class DaoStub {
    private static List<Address> addresses;
    private static List<Town> towns;
    private static List<Order> orders;
    private static List<Product> products;
    private static List<Category> categories;

    private static DaoStub instance = null;

    static {
        towns = new ArrayList<Town>();
        towns.add(new Town(7551, "Stegersbach"));
        towns.add(new Town(7551, "Bocksdorf"));
        towns.add(new Town(7533, "Ollersdorf"));
        towns.add(new Town(1020, "Wien"));

        addresses = new ArrayList<Address>();
        addresses.add(new Address(towns.get(0), "Badsiedlung", "11/7"));
        addresses.add(new Address(towns.get(0), "Badsiedlung", "12/7"));
        addresses.add(new Address(towns.get(0), "Badsiedlung", "13/7"));
        addresses.add(new Address(towns.get(0), "Badsfdsfdlung", "14/7"));
        addresses.add(new Address(towns.get(1), "Zickenberg", "37"));
        addresses.add(new Address(towns.get(1), "Zickenberg", "36"));
        addresses.add(new Address(towns.get(1), "Gickenberg", "35"));
        addresses.add(new Address(towns.get(1), "Gickenberg", "34"));
        addresses.add(new Address(towns.get(2), "Neudauer Landstraße", "29"));
        addresses.add(new Address(towns.get(2), "Neudauer Landstraße", "28"));
        addresses.add(new Address(towns.get(2), "Geudauer Landstraße", "27"));
        addresses.add(new Address(towns.get(2), "Geudauer Landstraße", "26"));
        addresses.add(new Address(towns.get(3), "Nestroygasse", "1/10"));

        categories = new ArrayList<Category>();
        categories.add(new Category("Brote"));
        categories.add(new Category("Suesses"));
        categories.add(new Category("Gebaeck"));

        products = new ArrayList<Product>();
        products.add(new Product("Mischbrot 1kg", 1.0, categories.get(0)));
        products.add(new Product("Roggenbrot 1kg", 0.8, categories.get(0)));
        products.add(new Product("Zimtschnecke", 1.5, categories.get(1)));
        products.add(new Product("Krapfen", 0.99, categories.get(1)));
        products.add(new Product("Laugenstangerl", 0.7, categories.get(2)));
        products.add(new Product("Kipferl", 0.45, categories.get(2)));

        orders = new ArrayList<Order>();

        orders.add(new Order(addresses.get(0), products.get(0), 2));
        orders.add(new Order(addresses.get(0), products.get(2), 2));
        orders.add(new Order(addresses.get(0), products.get(4), 2));

        orders.add(new Order(addresses.get(1), products.get(1), 4));

        orders.add(new Order(addresses.get(2), products.get(3), 3));
        orders.add(new Order(addresses.get(2), products.get(5), 1));


        orders.add(new Order(addresses.get(4), products.get(0), 2));
        orders.add(new Order(addresses.get(4), products.get(2), 2));
        orders.add(new Order(addresses.get(4), products.get(4), 2));

        orders.add(new Order(addresses.get(5), products.get(1), 4));

        orders.add(new Order(addresses.get(6), products.get(3), 3));
        orders.add(new Order(addresses.get(6), products.get(5), 1));


        orders.add(new Order(addresses.get(8), products.get(0), 2));
        orders.add(new Order(addresses.get(8), products.get(2), 2));
        orders.add(new Order(addresses.get(8), products.get(4), 2));

        orders.add(new Order(addresses.get(9), products.get(1), 4));

        orders.add(new Order(addresses.get(10), products.get(3), 3));
        orders.add(new Order(addresses.get(10), products.get(5), 1));

    }

    private DaoStub() {

    }

    public static DaoStub getInstance() {
        if (instance == null)
            instance = new DaoStub();
        return instance;
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Address> getAddressesForTown(Town town) {
        List<Address> selection = new ArrayList<Address>();
        for (Address address : addresses) {
            if (address.getTown().equals(town))
                selection.add(address);
        }
        return selection;
    }

    public List<Order> getOrdersForAddress(Address address) {
        List<Order> selection = new ArrayList<Order>();
        for (Order order : orders) {
            if (address.equals(order.getAddress()))
                selection.add(order);
        }
        return selection;
    }

    public List<Address> getAddressesForTour(int tourId) {
        return addresses;
    }

    public List<Town> getTowns() {
        return towns;
    }

    /**
     * Adds an order.
     *
     * @param product the product which is to be ordered
     * @param address the address the order belongs to
     * @return the added order, null if an order of this parameters already existed.
     */
    public Order addProductForAddress(Product product, Address address) {
        Order selection = null;
        for (Order order : orders) {
            if (order.getAddress().equals(address) && order.getProduct().equals(product)) {
                selection = order;
                break;
            }
        }
        if (selection == null) {
            selection = new Order(address, product, 0);
            selection.setDelivered(1);
            orders.add(selection);
            return selection;
        } else {
            selection.increaseDelivered();
            return null;
        }
    }

    public void addAddress(Address address) {
        addresses.add(address);
    }

    public void setDeliveredForAddress(Address address) {
        List<Order> orders = getOrdersForAddress(address);
        for (Order order : orders) {
            if (order.getDelivered() == Order.NOT_DELIVERED)
                order.setDelivered(order.getOrdered());
        }
    }

    public void undoDelivery(Address address) {
        List<Order> orders = getOrdersForAddress(address);
        for (Order order : orders) {
            order.setDelivered(Order.NOT_DELIVERED);
        }
    }
}

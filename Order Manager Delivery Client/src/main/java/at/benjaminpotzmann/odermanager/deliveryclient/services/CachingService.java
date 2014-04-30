package at.benjaminpotzmann.odermanager.deliveryclient.services;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.benjaminpotzmann.odermanager.deliveryclient.dao.DaoInterface;
import at.benjaminpotzmann.odermanager.deliveryclient.dao.NetworkDao;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Address;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Category;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Order;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.OrderItem;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Product;
import at.benjaminpotzmann.odermanager.deliveryclient.dto.Town;

/**
 * Created by Gizmo on 2014-04-29.
 */
public class CachingService {

    private static CachingService instance;
    private static DaoInterface dao;

    private static Map<Integer, Town> townMap = new HashMap<Integer, Town>();
    private static Map<Integer, Address> addressMap = new HashMap<Integer, Address>();
    private static Map<Integer, Order> orderMap = new HashMap<Integer, Order>();
    private static Map<Integer, OrderItem> orderItemMap = new HashMap<Integer, OrderItem>();
    private static Map<Integer, Category> categoryMap = new HashMap<Integer, Category>();
    private static Map<Integer, Product> productMap = new HashMap<Integer, Product>();

    private static int tempId = 0;


    private CachingService() {
        dao = NetworkDao.getInstance();
        getDataFromServer();
    }

    public static CachingService getInstance() {
        if (instance == null) {
            instance = new CachingService();
        }
        return instance;
    }

    public void getDataFromServer() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                for (Category category : dao.getCategories()) {
                    categoryMap.put(category.getId(), category);
                }
                for (Product product : dao.getProducts()) {
                    productMap.put(product.getId(), product);
                }

                for (Town town : dao.getTowns()) {
                    townMap.put(town.getId(), town);
                    for (Address address : dao.getAddressesForTownId(town.getId())) {
                        addressMap.put(address.getId(), address);
                        Order order = dao.getCurrentOrderForAddressId(address.getId());
                        orderMap.put(order.getId(), order);
                        for (OrderItem orderItem : dao.getOrderItemsForOrderId(order.getId())) {
                            orderItemMap.put(orderItem.getId(), orderItem);
                        }
                    }
                }
                return null;
            }
        }.execute();
    }

    public List<Town> getTowns() {
        return new ArrayList<Town>(townMap.values());
    }

    public Address addAddress(Address address) {
        address.setId(--tempId);
        addressMap.put(address.getId(), address);
        return address;
    }

    public Town getTown(int townid) {
        return townMap.get(townid);
    }

    public List<Address> getAddressesForTownId(int townId) {
        List<Address> addresses = new ArrayList<Address>();

        for (Address address : addressMap.values()) {
            if (address.getTownid() == townId)
                addresses.add(address);
        }

        return addresses;
    }

    public Order getOrderForAddress(int addressId) {
        for (Order order : orderMap.values()) {
            if (order.getAddressid() == addressId)
                return order;
        }

        Order order = new Order(tempId, 1, addressId, null, null, false);
        orderMap.put(order.getId(), order);
        return order;
    }

    public boolean isAddressOrdering(int addressId) {
        for (Order order : orderMap.values()) {
            if (order.getAddressid() == addressId)
                if (!getOrderItemsForOrder(order.getId()).isEmpty())
                    return true;
        }
        return false;
    }

    public void setDeliveredForAddress(int addressId) {
        Order order = getOrderForAddress(addressId);

        order.setDelivered(true);
        for (OrderItem orderItem : getOrderItemsForOrder(order.getId())) {
            if (orderItem.getDelivered() == OrderItem.NOT_DELIVERED)
                orderItem.setDelivered(orderItem.getOrdered());
        }
    }

    public List<OrderItem> getOrderItemsForOrder(int orderId) {
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        for (OrderItem orderItem : orderItemMap.values()) {
            if (orderItem.getOrderid() == orderId)
                orderItems.add(orderItem);
        }
        return orderItems;
    }

    public Product getProductForId(int productid) {
        return productMap.get(productid);
    }

    public List<Product> getProducts() {
        return new ArrayList<Product>(productMap.values());
    }

    public Category getCategoryForId(int categoryID) {
        return categoryMap.get(categoryID);
    }

    public OrderItem createOrderItem(int productId, int addressId) {
        OrderItem orderItem = new OrderItem(--tempId, getOrderForAddress(addressId).getId(), productId, 0, 1);
        orderItemMap.put(orderItem.getId(), orderItem);
        return orderItem;
    }

    public void undoDelivery(int addressId) {
        Order order = getOrderForAddress(addressId);

        order.setDelivered(false);
        for (OrderItem orderItem : getOrderItemsForOrder(order.getId())) {
            orderItem.setDelivered(OrderItem.NOT_DELIVERED);
        }
    }
}

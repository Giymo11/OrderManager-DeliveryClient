package at.benjaminpotzmann.odermanager.deliveryclient.dao;

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
public interface DaoInterface {
    List<Town> getTowns();

    List<Address> getAddressesForTownId(int id);

    Order getCurrentOrderForAddressId(int id);

    List<OrderItem> getOrderItemsForOrderId(int id);

    List<Category> getCategories();

    List<Product> getProductsForCategoryId(int id);
}

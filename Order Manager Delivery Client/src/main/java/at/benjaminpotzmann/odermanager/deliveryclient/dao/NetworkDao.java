package at.benjaminpotzmann.odermanager.deliveryclient.dao;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
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
public class NetworkDao implements DaoInterface {

    private static NetworkDao instance;
    private static CharBuffer buffer;
    private static DefaultHttpClient defaultClient;

    private NetworkDao() {
        buffer = CharBuffer.allocate(32 * 1024);
        defaultClient = new DefaultHttpClient();
    }

    public static NetworkDao getInstance() {
        if (instance == null) {
            instance = new NetworkDao();
        }
        return instance;
    }

    private synchronized String getJsonString(String path) {
        try {
            // Create a new HTTP Client
            // Setup the get request
            HttpGet httpGetRequest = new HttpGet("http://10.0.0.1:8080/ordermanager/rest" + path);

            // Execute the request in the client
            HttpResponse httpResponse = defaultClient.execute(httpGetRequest);
            // Grab the response
            if (httpResponse.getEntity() == null)
                return null;

            BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            String read = reader.readLine();
            if (read == null)
                throw new NullPointerException();

            Log.d("NetworkDao", "" + read);

            return read;
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // In your production code handle any errors and catch the individual exceptions
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Town> getTowns() {

        List<Town> towns = new ArrayList<Town>();

        try {
            JSONArray array = new JSONArray(getJsonString("/towns"));
            JSONObject json;

            int id, plz;
            String name;

            for (int i = 0; i < array.length(); ++i) {
                json = array.getJSONObject(i);
                id = json.getInt("id");
                plz = json.getInt("plz");
                name = json.getString("name");
                towns.add(new Town(id, plz, name));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return towns;
    }

    @Override
    public List<Address> getAddressesForTownId(int townId) {

        List<Address> addresses = new ArrayList<Address>();

        try {
            JSONArray array = new JSONArray(getJsonString("/addresses/" + townId));
            JSONObject json;

            int id, townid;
            String street, houseNr;

            for (int i = 0; i < array.length(); ++i) {
                json = array.getJSONObject(i);
                id = json.getInt("id");
                townid = json.getInt("townid");
                street = json.getString("street");
                houseNr = json.getString("houseNr");
                addresses.add(new Address(id, townid, street, houseNr));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return addresses;
    }

    @Override
    public Order getCurrentOrderForAddressId(int addressId) {
        try {
            String jsonString = getJsonString("/orders/" + addressId);
            if (jsonString == null)
                throw new NullPointerException("The path " + "/orders/" + addressId + " gave no response from the server!");

            JSONObject json = new JSONObject(jsonString);

            int id, tourid, addressid;
            String memoForPock, memoForCustomer;
            boolean delivered;

            id = json.getInt("id");
            tourid = json.getInt("tourid");
            addressid = json.getInt("addressid");
            memoForPock = json.getString("memoForPock");
            memoForCustomer = json.getString("memoForCustomer");
            delivered = json.getBoolean("delivered");

            return new Order(id, tourid, addressid, memoForPock, memoForCustomer, delivered);
        } catch (NullPointerException ex) {
            if (ex.getMessage() != null)
                Log.d("NetworkDao", ex.getMessage());
            else
                ex.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<OrderItem> getOrderItemsForOrderId(int orderId) {

        List<OrderItem> orderItems = new ArrayList<OrderItem>();

        try {
            JSONArray array = new JSONArray(getJsonString("/orderitems/" + orderId));
            JSONObject json;

            int id, orderid, productid, ordered, delivered;

            for (int i = 0; i < array.length(); ++i) {
                json = array.getJSONObject(i);

                id = json.getInt("id");
                orderid = json.getInt("orderid");
                productid = json.getInt("productid");
                ordered = json.getInt("ordered");
                delivered = json.getInt("delivered");

                orderItems.add(new OrderItem(id, orderid, productid, ordered, delivered));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orderItems;
    }

    @Override
    public List<Category> getCategories() {

        List<Category> categories = new ArrayList<Category>();

        try {
            JSONArray array = new JSONArray(getJsonString("/categories"));
            JSONObject json;

            int id;
            String name;

            for (int i = 0; i < array.length(); ++i) {
                json = array.getJSONObject(i);

                id = json.getInt("id");
                name = json.getString("name");

                categories.add(new Category(id, name));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return categories;
    }

    @Override
    public List<Product> getProducts() {

        List<Product> products = new ArrayList<Product>();

        try {
            JSONArray array = new JSONArray(getJsonString("/products"));
            JSONObject json;

            int id, categoryID, priority, pictureID;
            String title, description, picture;
            float price;
            boolean visible;

            for (int i = 0; i < array.length(); ++i) {
                json = array.getJSONObject(i);

                id = json.getInt("id");
                categoryID = json.getInt("categoryID");
                priority = json.getInt("priority");
                pictureID = json.getInt("pictureID");
                picture = null;
                title = json.getString("title");
                description = json.getString("description");
                price = (float) json.getDouble("price");
                visible = json.getBoolean("visible");

                products.add(new Product(id, categoryID, priority, title, description, price, null, pictureID, visible));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return products;
    }
}

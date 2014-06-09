package at.benjaminpotzmann.odermanager.deliveryclient.dao;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private static DefaultHttpClient defaultClient;
    private static String ip = "http://10.0.0.4:8080/ordermanager/rest";

    private NetworkDao() {
        defaultClient = new DefaultHttpClient();
    }

    public static NetworkDao getInstance() {
        if (instance == null) {
            instance = new NetworkDao();
        }
        return instance;
    }

    @Override
    public String getIp() {
        return ip;
    }

    @Override
    public void setIp(String ip) {
        NetworkDao.ip = ip;
    }

    private synchronized String getJsonString(String path) {
        try {
            // Create a new HTTP Client
            // Setup the get request
            HttpGet httpGetRequest = new HttpGet(ip + path);

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

    private void postJsonString(String path, String json) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(ip + path);

        Log.d("NetworkDao.postJson", json);

        try {
            // Add your data
            httppost.setEntity(new StringEntity(json));
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");

            // Execute HTTP Post Request
            httpclient.execute(httppost);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Town> getTowns() {

        List<Town> towns = new ArrayList<Town>();

        try {
            JSONArray array = new JSONArray(getJsonString("/towns"));

            for (int i = 0; i < array.length(); ++i) {
                towns.add(getTown(array.getJSONObject(i)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return towns;
    }

    private Town getTown(JSONObject json) throws JSONException {
        int id, plz;
        String name;

        id = json.getInt("id");
        plz = json.getInt("plz");
        name = json.getString("name");

        return new Town(id, plz, name);
    }

    @Override
    public List<Address> getAddressesForTownId(int townId) {

        List<Address> addresses = new ArrayList<Address>();

        try {
            JSONArray array = new JSONArray(getJsonString("/addresses/" + townId));

            for (int i = 0; i < array.length(); ++i) {
                addresses.add(getAddress(array.getJSONObject(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addresses;
    }

    private Address getAddress(JSONObject json) throws JSONException {
        int id, townid;
        String street, houseNr;

        id = json.getInt("id");
        townid = json.getInt("townid");
        street = json.getString("street");
        houseNr = json.getString("houseNr");

        return new Address(id, townid, street, houseNr);
    }

    @Override
    public void postAddress(final Address address) {
        final JSONObject json = new JSONObject();

        try {
            json.put("id", address.getId());
            json.put("townid", address.getTownid());
            json.put("street", address.getStreet());
            json.put("houseNr", address.getHouseNr());

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    postJsonString("/addresses/" + address.getId(), json.toString());
                    return null;
                }
            }.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Order getCurrentOrderForAddressId(int addressId) {
        try {
            String jsonString = getJsonString("/orders/" + addressId);
            if (jsonString == null)
                throw new NullPointerException("The path " + "/orders/" + addressId + " gave no response from the server!");

            return getOrder(new JSONObject(jsonString));

        } catch (NullPointerException ex) {
            if (ex.getMessage() != null)
                Log.d("NetworkDao", ex.getMessage());
            else
                ex.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Order getOrder(JSONObject json) throws JSONException {
        int id, tourid, addressid;
        String memoForPock, memoForCustomer;
        boolean delivered;

        id = json.getInt("id");
        tourid = json.getInt("tourid");
        addressid = json.getInt("addressid");
        memoForPock = json.getString("memoForPock");
        if (memoForPock.equals("null"))
            memoForPock = null;
        memoForCustomer = json.getString("memoForCustomer");
        if (memoForCustomer.equals("null"))
            memoForCustomer = null;
        delivered = json.getBoolean("delivered");

        return new Order(id, tourid, addressid, memoForPock, memoForCustomer, delivered);
    }

    @Override
    public List<OrderItem> getOrderItemsForOrderId(int orderId) {

        List<OrderItem> orderItems = new ArrayList<OrderItem>();

        try {
            JSONArray array = new JSONArray(getJsonString("/orderitems/" + orderId));

            for (int i = 0; i < array.length(); ++i) {
                orderItems.add(getOrderItem(array.getJSONObject(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderItems;
    }

    private OrderItem getOrderItem(JSONObject json) throws JSONException {
        int id, orderid, productid, ordered, delivered;

        id = json.getInt("id");
        orderid = json.getInt("orderid");
        productid = json.getInt("productid");
        ordered = json.getInt("ordered");
        delivered = json.getInt("delivered");

        return new OrderItem(id, orderid, productid, ordered, delivered);
    }

    @Override
    public void postOrderItems(final int addressId, List<OrderItem> orderItems) {

        final JSONArray array = new JSONArray();

        try {
            for (OrderItem orderItem : orderItems) {
                JSONObject json = new JSONObject();

                json.put("id", orderItem.getId());
                json.put("orderid", orderItem.getOrderid());
                json.put("productid", orderItem.getProductid());
                json.put("ordered", orderItem.getOrdered());
                json.put("delivered", orderItem.getDelivered());

                array.put(json);
            }

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    postJsonString("/orderitems/" + addressId, array.toString());
                    return null;
                }
            }.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postMemo(final int addressId, final String memo) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                postPlainString("/orders/" + addressId, memo);
                return null;
            }
        }.execute();
    }

    private void postPlainString(String path, String s) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(ip + path);

        Log.d("NetworkDao.postString", s);

        try {
            // Add your data
            httppost.setEntity(new StringEntity(s));
            httppost.setHeader("Accept", "text/plain");
            httppost.setHeader("Content-type", "text/plain");

            // Execute HTTP Post Request
            httpclient.execute(httppost);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Category> getCategories() {

        List<Category> categories = new ArrayList<Category>();

        try {
            JSONArray array = new JSONArray(getJsonString("/categories"));

            for (int i = 0; i < array.length(); ++i) {
                categories.add(getCategory(array.getJSONObject(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }

    private Category getCategory(JSONObject json) throws JSONException {
        int id;
        String name;

        id = json.getInt("id");
        name = json.getString("name");

        return new Category(id, name);
    }

    @Override
    public List<Product> getProducts() {

        List<Product> products = new ArrayList<Product>();

        try {
            JSONArray array = new JSONArray(getJsonString("/products"));

            for (int i = 0; i < array.length(); ++i) {
                products.add(getProduct(array.getJSONObject(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    private Product getProduct(JSONObject json) throws JSONException {
        int id, categoryID, priority, pictureID;
        String title, description, picture;
        float price;
        boolean visible;

        id = json.getInt("id");
        categoryID = json.getInt("categoryID");
        priority = json.getInt("priority");
        pictureID = json.getInt("pictureID");
        picture = null;
        title = json.getString("title");
        description = json.getString("description");
        price = (float) json.getDouble("price");
        visible = json.getBoolean("visible");

        return new Product(id, categoryID, priority, title, description, price, null, pictureID, visible);
    }
}

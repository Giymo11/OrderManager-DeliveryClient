package at.benjaminpotzmann.odermanager.deliveryclient.services;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.CharBuffer;

/**
 * An AsyncTask implementation for performing GETs on the Hypothetical REST APIs.
 */
public class GetTask extends AsyncTask<Void, Void, String> {

    private String path;
    private RestTaskCallback mCallback;
    private static DefaultHttpClient defaultClient;
    private CharBuffer buffer;

    /**
     * Creates a new instance of GetTask with the specified URL and callback.
     *
     * @param path     The URL for the REST API.
     * @param callback The callback to be invoked when the HTTP request
     *                 completes.
     */
    public GetTask(String path, RestTaskCallback callback) {
        this.path = path;
        this.mCallback = callback;
        buffer = CharBuffer.allocate(32 * 1024);
    }

    @Override
    protected String doInBackground(Void... voids) {
        String response = null;
        try {
            // Create a new HTTP Client
            // Setup the get request
            HttpGet httpGetRequest = new HttpGet("http://192.168.43.150:8080/rest" + path);

            // Execute the request in the client
            HttpResponse httpResponse = defaultClient.execute(httpGetRequest);
            // Grab the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
            reader.read(buffer);

            response = buffer.toString();

        } catch (Exception e) {
            // In your production code handle any errors and catch the individual exceptions
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        mCallback.onTaskComplete(result);
        super.onPostExecute(result);
    }
}
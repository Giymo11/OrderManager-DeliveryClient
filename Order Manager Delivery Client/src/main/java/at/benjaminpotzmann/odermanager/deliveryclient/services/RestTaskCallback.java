package at.benjaminpotzmann.odermanager.deliveryclient.services;

/**
 * Created by Benjamin Potzmann on 30.04.2014.
 */

/**
 * Class definition for a callback to be invoked when the HTTP request
 * representing the REST API Call completes.
 */
public interface RestTaskCallback {
    /**
     * Called when the HTTP request completes.
     *
     * @param result The result of the HTTP request.
     */
    public void onTaskComplete(String result);
}

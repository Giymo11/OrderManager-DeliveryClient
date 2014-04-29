package at.benjaminpotzmann.odermanager.deliveryclient.dao;

/**
 * Created by Gizmo on 2014-04-29.
 */
public class NetworkDao {

    private static NetworkDao instance;

    private NetworkDao() {

    }

    public static NetworkDao getInstance() {
        if (instance == null) {
            instance = new NetworkDao();
        }
        return instance;
    }
}

package at.benjaminpotzmann.odermanager.deliveryclient.interfaces;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 19.02.14
 * Time: 16:29
 * To change this template use File | Settings | File Templates.
 */
public interface Identifiable extends Serializable {
    void setId(int id);

    int getId();
}

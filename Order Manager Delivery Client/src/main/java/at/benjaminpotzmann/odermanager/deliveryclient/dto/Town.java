package at.benjaminpotzmann.odermanager.deliveryclient.dto;

import java.io.Serializable;

/**
 * Created by Giymo11 on 20.02.14.
 */
public class Town implements Serializable {

    private int zipcode;
    private String location;

    public Town(int zipcode, String location) {
        this.zipcode = zipcode;
        this.location = location;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "" + zipcode + " " + location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Town.class != o.getClass()) return false;

        Town town = (Town) o;

        if (zipcode != town.zipcode) return false;
        if (location != null ? !location.equals(town.location) : town.location != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = zipcode;
        result = 31 * result + (location != null ? location.toLowerCase().hashCode() : 0);
        return result;
    }
}

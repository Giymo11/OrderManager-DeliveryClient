package at.benjaminpotzmann.odermanager.deliveryclient.dto;

import java.io.Serializable;

/**
 * The DTO representing the address of one customer who ordered something.
 */
public class Address implements Serializable {
    private int zipCode;
    private String town;
    private String street;
    private String number;

    public Address(int zipCode, String town, String street, String number) {
        this.zipCode = zipCode;
        this.town = town;
        this.street = street;
        this.number = number;
    }

    @Override
    public String toString() {
        if (street == null && number == null)
            return "" + zipCode + " " + town;
        return "" + street + " " + number + "\n" + zipCode + " " + town;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}

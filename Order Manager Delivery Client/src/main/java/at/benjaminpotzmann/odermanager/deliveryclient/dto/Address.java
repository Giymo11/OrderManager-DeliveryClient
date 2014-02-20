package at.benjaminpotzmann.odermanager.deliveryclient.dto;

import java.io.Serializable;

/**
 * The DTO representing the address of one customer who ordered something.
 */
public class Address implements Serializable {

    private Town town;
    private String street;
    private String number;

    public Address(Town town, String street, String number) {
        this.town = town;
        this.street = street;
        this.number = number;
    }

    @Override
    public String toString() {
        return "" + street + " " + number + "\n" + town;
    }

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Address.class != o.getClass()) return false;

        Address address = (Address) o;

        if (number != null ? !number.equals(address.number) : address.number != null) return false;
        if (street != null ? !street.equals(address.street) : address.street != null) return false;
        if (town != null ? !town.equals(address.town) : address.town != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = town != null ? town.hashCode() : 0;
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (number != null ? number.hashCode() : 0);
        return result;
    }
}

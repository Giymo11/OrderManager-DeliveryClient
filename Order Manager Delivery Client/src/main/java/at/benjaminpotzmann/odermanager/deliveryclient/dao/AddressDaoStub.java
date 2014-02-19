package at.benjaminpotzmann.odermanager.deliveryclient.dao;

import java.util.ArrayList;
import java.util.List;

import at.benjaminpotzmann.odermanager.deliveryclient.dto.Address;

/**
 * A sample dao for
 */
public class AddressDaoStub {
    private List<Address> addresses;
    private static List<Address> towns;
    private static List<String> townNames;

    static {
        towns = new ArrayList<Address>();
        towns.add(new Address(7551, "Stegersbach", null, null));
        towns.add(new Address(7551, "Bocksdorf", null, null));
        towns.add(new Address(7533, "Ollersdorf", null, null));

        townNames = new ArrayList<String>();
        for (Address town : towns) {
            townNames.add(town.toString());
        }
    }

    public AddressDaoStub() {
        addresses = new ArrayList<Address>();
        addresses.add(new Address(7551, "Stegersbach", "Badsiedlung", "11/7"));
        addresses.add(new Address(7551, "Stegersbach", "Badsiedlung", "12/7"));
        addresses.add(new Address(7551, "Stegersbach", "Badsiedlung", "13/7"));
        addresses.add(new Address(7551, "Stegersbach", "Badsfdsfdlung", "14/7"));
        addresses.add(new Address(7551, "Bocksdorf", "Zickenberg", "37"));
        addresses.add(new Address(7551, "Bocksdorf", "Zickenberg", "36"));
        addresses.add(new Address(7551, "Bocksdorf", "Gickenberg", "35"));
        addresses.add(new Address(7551, "Bocksdorf", "Gickenberg", "34"));
        addresses.add(new Address(7533, "Ollersdorf", "Neudauer Landstraße", "29"));
        addresses.add(new Address(7533, "Ollersdorf", "Neudauer Landstraße", "28"));
        addresses.add(new Address(7533, "Ollersdorf", "Geudauer Landstraße", "27"));
        addresses.add(new Address(7533, "Ollersdorf", "Geudauer Landstraße", "26"));
        addresses.add(new Address(1020, "Wien", "Nestroygasse", "1/10"));
    }

    public List<Address> getAddressesForTown(Address town) {
        List<Address> selection = new ArrayList<Address>();
        for (Address address : addresses) {
            if (address.getZipCode() == town.getZipCode() && address.getTown().equals(town.getTown()))
                selection.add(address);
        }
        return selection;
    }

    public List<Address> getAddressesForTour(int tourId) {
        return addresses;
    }

    public static List<Address> getTowns() {
        return towns;
    }

    public static List<String> getTownNames() {
        return townNames;
    }
}

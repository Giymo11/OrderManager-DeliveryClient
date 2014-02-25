package at.benjaminpotzmann.odermanager.deliveryclient.helper;

import java.io.Serializable;
import java.math.BigDecimal;

public class PriceFormatHelper implements Serializable {
    public static double round(double value) {
        return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static String format(double value) {
        return String.format("€ %.2f", PriceFormatHelper.round(value));
    }
}
package com.digicash.paymentkitlibrary.util;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;


/**
 * Util class for currency operations.
 */
public class CurrencyFormatter {

    private Locale locale;
    private String MINUS = "\u2212";

    public CurrencyFormatter() {
        this.locale = new Locale("en", "ar_TN");
    }

    /**
     * Gets currency symbol from ISO-4217 code parametrized with locale.
     *
     * @param currencyCode a three letter ISO-4217 code
     * @return currency symbol.
     */
    public String getCurrencySymbol(String currencyCode) {
        return Currency.getInstance(currencyCode).getSymbol(locale);
    }

    /**
     * Gets the decimal places precision for currency.
     *
     * @param currencyCode a three letter ISO-4217 code
     * @return decimal places
     */
    public int getFractionDigits(String currencyCode) {
        return Currency.getInstance(currencyCode).getDefaultFractionDigits();
    }

    /**
     * Method to parse a string representing an amount into a long value.
     *
     * @param amount Value to be parsed.
     * @return The long value for the amount
     */
    public Long parseAmountAsLong(String amount) throws ParseException {
        try {
            return ((BigDecimal) getDecimalFormat(false).parse(amount))
                    .setScale(getPrecision(), RoundingMode.UNNECESSARY)
                    .movePointRight(getPrecision())
                    .longValueExact();
        } catch (ArithmeticException | NumberFormatException e) {
            throw new ParseException(e.getMessage(), 0);
        }
    }

    /**
     * Method to parse a string representing an amount into a long value. Rounds output value down
     * if necessary. Used in recalculation during unit switching if the new unit has less precision
     * than previous one.
     *
     * @param amount Value to be parsed.
     * @return The long value for the amount
     */
    public Long parseAmountAsLongWithRounding(String amount) throws ParseException {
        try {
            return ((BigDecimal) getDecimalFormat(false).parse(amount))
                    .setScale(getPrecision(), RoundingMode.DOWN)
                    .movePointRight(getPrecision())
                    .longValueExact();
        } catch (ArithmeticException | NumberFormatException e) {
            throw new ParseException(e.getMessage(), 0);
        }
    }

    /**
     * Method to format a long value, using the defined decimal formatter.
     *
     * @param amount Value to be formatted.
     * @return A string representing the formatted amount.
     */
    public String formatDinarAmount(long amount) {
        DecimalFormat f = getDecimalFormat(false);
        String formatted = f.format(new BigDecimal(amount).movePointLeft(getPrecision()));
        if (formatted.contains("(") && formatted.contains(")")) {
            //for some reason NumberFormat is parsing negative values like ($12) instead of -$12
            formatted = MINUS + formatted.replaceAll("\\(", "").replaceAll("\\)", "");
        }
        return formatted;
    }

    /**
     * Method to format a long value, using the defined decimal formatter.
     *
     * @param amount Value to be formatted.
     * @return A string representing the formatted amount.
     */
    public String formatAmount(long amount) {
        DecimalFormat f = getDecimalFormat(false);
        int precision = getPrecision();
        String formatted = f.format(new BigDecimal(amount).movePointLeft(precision));
        String pattern = "0.000";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        //TODO: Fix Hard Coded Values
        amount = amount / 100;
        BigDecimal value = new BigDecimal(amount).movePointLeft(precision - 2);
        //BigDecimal value = new BigDecimal(amount).movePointLeft(precision);
        //value = value.setScale(getPrecision(), RoundingMode.DOWN);

        formatted = decimalFormat.format(value);
        if (formatted.contains("(") && formatted.contains(")")) {
            //for some reason NumberFormat is parsing negative values like ($12) instead of -$12
            formatted = MINUS + formatted.replaceAll("\\(", "").replaceAll("\\)", "");
        }


        return formatted;
    }

    /**
     * Private method to return the amount decimal format
     *
     * @return An instance of DecimalFormat already set
     */
    public DecimalFormat getDecimalFormat(boolean withSymbol) {
        DecimalFormat f;
        if (withSymbol) {
            f = (DecimalFormat) DecimalFormat.getCurrencyInstance(locale);
            f.setCurrency(Currency.getInstance("TND"));
        } else {
            f = (DecimalFormat) DecimalFormat.getInstance(locale);
            DecimalFormatSymbols decimalFormatSymbols = f.getDecimalFormatSymbols();
            decimalFormatSymbols.setCurrencySymbol("");
            f.setDecimalFormatSymbols(decimalFormatSymbols);
        }
        f.setMinimumFractionDigits(getPrecision());
        f.setMaximumFractionDigits(getPrecision());
        f.setGroupingUsed(false);
        f.setParseBigDecimal(true);
        return f;
    }

    private static int getPrecision() {
        int baseDigits = Currency.getInstance("TND").getDefaultFractionDigits();
        return baseDigits;
    }
}

package com.michael.utils.number;

/**
 * @author Michael
 */
public class DoubleUtils {
    public static double add(Double... args) {
        double data = 0d;
        for (Double d : args) {
            if (d != null) {
                data += d;
            }
        }
        return data;
    }
}

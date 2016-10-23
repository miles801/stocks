package com.michael.utils.string;

import org.springframework.util.Assert;

import java.util.Random;

/**
 * @author Michael
 */
public class RandomUtils {
    private static char[] chars = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    public static String generate(int length) {
        Assert.isTrue(length > 0 && length < 100, "长度只能是0-99之间!");
        String v = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            v += chars[random.nextInt(62)];
        }
        return v;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(generate(6));
        }
    }
}

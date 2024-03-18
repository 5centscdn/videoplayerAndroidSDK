package com.fivecentscdnanalytics.utils;

import java.security.SecureRandom;
import java.util.Random;

public class ViewIDGenerator {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private final Random random;
    private final char[] symbols;
    private final char[] buffer;

    public ViewIDGenerator() {
        this(new SecureRandom());
    }

    public ViewIDGenerator(Random random) {
        this.random = random;
        this.symbols = CHARACTERS.toCharArray();
        this.buffer = new char[23];
    }

    public String generate() {
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = symbols[random.nextInt(symbols.length)];
        }
        return new String(buffer);
    }

//    public static void main(String[] args) {
//        RandomStringGenerator generator = new RandomStringGenerator();
//        String randomString = generator.generateRandomString();
//        System.out.println(randomString);
//    }
}

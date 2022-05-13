package com.spring.angular.reddit.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class KeyGenerationUtil {
    private static final List<Integer> VALID_ID_CHARS = new ArrayList<>();

    static {
        IntStream.rangeClosed('0', '9').forEach(VALID_ID_CHARS::add); // 0-9
        IntStream.rangeClosed('A', 'Z').forEach(VALID_ID_CHARS::add); // A-Z
        IntStream.rangeClosed('a', 'z').forEach(VALID_ID_CHARS::add); // a-z
    }

    public static String generateUniqueIdentifier() {
        int length = 16;
        StringBuilder sb = new StringBuilder();
        new SecureRandom().ints(length, 0, VALID_ID_CHARS.size()).map(VALID_ID_CHARS::get)
                .forEach(s -> sb.append((char) s));
        return sb.toString();
    }
}

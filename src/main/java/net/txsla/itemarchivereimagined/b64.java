package net.txsla.itemarchivereimagined;

import java.util.Base64;

public class b64 {

    public static String encode(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    public static String decode(String input) {
        return new String(Base64.getDecoder().decode(input));
    }
}

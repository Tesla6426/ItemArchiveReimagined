package net.txsla.itemarchivereimagined;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;

public class hash {
    public static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());

            // Convert to hex string
            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available?", e);
        }
    }
    public static String getUUID(ItemStack item) {
        // add item name and type, then hash

        // handle null item
        ItemMeta meta = item.getItemMeta(); String name;
        if (meta == null) name = "null-" + item.getType();
        else name = meta.getDisplayName();

        return sha256(name + item.getType());
    }
}

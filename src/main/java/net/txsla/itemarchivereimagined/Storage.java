package net.txsla.itemarchivereimagined;

import net.txsla.itemarchivereimagined.DataTypes.Archive;
import net.txsla.itemarchivereimagined.DataTypes.Vault;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Storage {
    // this class is to separate the storage of vaults and archives from the rest of the code simply for readability
    public static Map<String, Archive> archives = new ConcurrentHashMap<>();
    public static Map<String, Vault> vaults = new ConcurrentHashMap<>();
    public static ItemArchiveReimagined server;
    public static void saveToFile(List<String> lines, Path targetPath) throws IOException {
        Path tempFile = Files.createTempFile(targetPath.getParent(), "temp-", ".tmp");

        char[] all = toCharArray(lines);
        try (BufferedWriter writer = Files.newBufferedWriter(tempFile, StandardCharsets.UTF_8)) {
            writer.write(all);
        }

        Files.move(tempFile, targetPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    }
    public static char[] toCharArray(List<String> lines) {
        int total = 0;
        for (String s : lines) {
            total += s.length() + 1; // newline
        }
        char[] result = new char[total];
        int pos = 0;
        for (String s : lines) {
            int len = s.length();
            s.getChars(0, len, result, pos);
            pos += len;
            result[pos++] = '\n';
        }
        return result;
    }
}

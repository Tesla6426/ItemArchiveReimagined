package net.txsla.itemarchivereimagined;

import net.txsla.itemarchivereimagined.DataTypes.Archive;
import net.txsla.itemarchivereimagined.DataTypes.Vault;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Storage {
    // this class is to separate the storage of vaults and archives from the rest of the code simply for readability
    public static Map<String, Archive> archives = new ConcurrentHashMap<>();
    public static Map<String, Vault> vaults = new ConcurrentHashMap<>();
    public static void saveToFile(List<String> lines, Path targetPath) throws IOException {
        Path tempFile = Files.createTempFile(targetPath.getParent(), "temp-", ".tmp");
        try (BufferedWriter writer = Files.newBufferedWriter(tempFile)) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
        Files.move(tempFile, targetPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    }
}

package net.txsla.itemarchivereimagined.Commands;

import net.txsla.itemarchivereimagined.DataTypes.Item;
import net.txsla.itemarchivereimagined.DataTypes.Vault;
import net.txsla.itemarchivereimagined.ItemConverter;
import net.txsla.itemarchivereimagined.Storage;
import net.txsla.itemarchivereimagined.b64;
import net.txsla.itemarchivereimagined.deserialize;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class loadoldarchive implements CommandExecutor, TabExecutor {
    // open a vault directly bypassing the archive interface
    public static Path old = new File("C:\\Users\\txsla\\Desktop\\archves\\old\\ARCHIVE.ar").toPath();
    public static List<String> old_file;
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
    {
        // this bit is hardcoded, triggered by command
        // remove access later, as this is for DEV ONLY
        Vault vault = Storage.vaults.get(args[0]);

        // get all lines in archive file
        loadFileIntoRAM();

        // cycle through all lines of .ar file
        new Thread(() -> {
            Item current_item; String[] data; int items_added = 0;
            for (String encoded_item : old_file) {
                try {
                    // decode item
                    data = encoded_item.split("¦");
                    current_item = new Item(ItemConverter.toItemStackOld(b64.decode(data[5])), data[2]);

                    // add to vault
                    if (vault.addItem(current_item)) {
                        String displayName = current_item.getItemStack().getItemMeta().getDisplayName();
                        sender.sendMessage("Item " + (displayName.length() > 64 ? displayName.substring(0, 63) : displayName) + " added to vault");
                        items_added++;
                    } else sender.sendMessage("Item rejected for duplicate");

                } catch (Exception e) {
                    // print error if failed
                    sender.sendMessage("Failed to process item!");
                    System.out.println("Failed to process item! ");
                    e.printStackTrace();
                }
            }
            sender.sendMessage(items_added + " items successfully added to vault " + args[0] + "!");

            // save vault
            vault.saveItemsToRam();
            vault.saveRamToFile();
        }).start();
        return true;
    }
    public void loadFileIntoRAM() {
        try {
            old_file = Files.readAllLines(old, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Failed to load Archive file " + old);
            e.printStackTrace();
        }
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String str, @NotNull String[] args) {
        if (Storage.vaults == null) return new ArrayList<>();
        return new ArrayList<>(Storage.vaults.keySet());
    }
}

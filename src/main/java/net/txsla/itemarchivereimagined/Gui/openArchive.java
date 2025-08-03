package net.txsla.itemarchivereimagined.Gui;

import net.txsla.itemarchivereimagined.DataTypes.Archive;
import net.txsla.itemarchivereimagined.DataTypes.Page;
import net.txsla.itemarchivereimagined.DataTypes.Placeholder;
import net.txsla.itemarchivereimagined.Storage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class openArchive {

    public static void open(Player p, String archive_name, int number) {
        // this method assumes valid input, validate user input at command

        // add player to tracker
        Storage.gui_tracker.put(p.getName(), archive_name + "-open-" + number);

        // get archive and page
        Archive archive = Storage.archives.get(archive_name);
        Page page = archive.getPage(number);

        // get the starting index for the vault populators
        int vaultIndex = 0;
        for (int i = 1; i < number - 1; i++) vaultIndex += archive.getPagePopulators(i);

        Inventory inventory;
        // process placeholders and add them to inventory
        if (page.getSize() < 7) {
            inventory = Bukkit.createInventory(null, page.getSize() * 9, page.getName());
        }
        String[] format = page.getFormat();


        // DEBUG REMOVE LATER
        System.out.println(Arrays.toString(format));
        System.out.println(page.getName());

        int slot = 0;
        for (String placeholder : format) {
            if (inventory.getSize() < slot) break; // terminate if loop tries to add too many items
            switch (placeholder) {
                case "air":
                    // air
                    inventory.setItem(slot, Storage.air.getItemStack());
                    break;
                case "vault":
                    // add item from vault (main)
                    inventory.setItem(slot, Storage.vaults.get(archive_name + "-main").getItem(vaultIndex).getItemStack());
                    vaultIndex++;
                    break;
                default:
                    // add placeholder
                    inventory.setItem(slot, archive.getPlaceholder(placeholder).getItem());
                    break;
            }
            slot++;
        }

        p.openInventory(inventory);
        // re-add player to tracker in case of lag de-sync
        Storage.gui_tracker.put(p.getName(), archive_name + "-open-" + number);
    }
}

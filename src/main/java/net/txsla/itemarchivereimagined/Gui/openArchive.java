package net.txsla.itemarchivereimagined.Gui;

import net.txsla.itemarchivereimagined.DataTypes.Archive;
import net.txsla.itemarchivereimagined.DataTypes.Page;
import net.txsla.itemarchivereimagined.Storage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import java.util.Arrays;

public class openArchive {
    public static void open(Player p, String archive_name, int number) {
        // this method assumes valid input, validate user input at command

        // add player to tracker
        Storage.gui_tracker.put(p.getName(), archive_name + "¦open¦none¦" + number);

        // get archive and page
        Archive archive = Storage.archives.get(archive_name);
        Page page = archive.getPage(number);

        // get the starting index for the vault populators
        int vaultIndex = Storage.vaults.get(archive_name + "-main").getSize();
        for (int i = 1; i < number; i++) vaultIndex -= archive.getPagePopulators(i); // off by one error

        // Get page format
        Inventory inventory = loadInv.create(page.getSize(), page.getName().replaceAll("%page%", ""+number));
        String[] format = page.getFormat();


        // process placeholders and add them to inventory
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
                    vaultIndex--;
                    break;
                default:
                    // add placeholder
                    try {
                        inventory.setItem(slot, archive.getPlaceholder(placeholder).getItem());
                    } catch (Exception e) {/*ignore*/}
                    break;
            }
            slot++;
        }

        p.openInventory(inventory);
        // re-add player to tracker in case of lag de-sync
        Storage.gui_tracker.put(p.getName(), archive_name + "¦open¦none¦" + number);
    }

}

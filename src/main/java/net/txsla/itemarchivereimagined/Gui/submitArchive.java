package net.txsla.itemarchivereimagined.Gui;

import net.txsla.itemarchivereimagined.DataTypes.Archive;
import net.txsla.itemarchivereimagined.DataTypes.Page;
import net.txsla.itemarchivereimagined.DataTypes.Placeholder;
import net.txsla.itemarchivereimagined.Storage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class submitArchive {
    public static void openMenu(Player p, String archive_name) {

        Storage.gui_tracker.put(p.getName(), archive_name + "¦submit¦none¦0");

        Archive archive = Storage.archives.get(archive_name);
        Page page = archive.getPage(0); // page zero is submit

        // Get page format
        Inventory inventory = loadInv.create(page.getSize(), page.getName().replaceAll("%page%", "0"));
        String[] format = page.getFormat();

        // process placeholders and add them to inventory
        int slot = 0;
        for (String placeholder : format) {
            if (inventory.getSize() < slot) break; // terminate if loop tries to add too many items
            switch (placeholder) {
                case "air":
                case "vault":
                    // vault slots are the save slots on the submit page
                    inventory.setItem(slot, Storage.air.getItemStack());
                    break;
                default:
                    // add placeholder
                    inventory.setItem(slot, archive.getPlaceholder(placeholder).getItem());
                    break;
            }
            slot++;
        }

        p.openInventory(inventory);

        Storage.gui_tracker.put(p.getName(), archive_name + "¦submit¦none¦0");
        // actual submission code is in the listener
    }
}

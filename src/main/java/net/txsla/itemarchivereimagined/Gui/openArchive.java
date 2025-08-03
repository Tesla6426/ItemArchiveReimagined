package net.txsla.itemarchivereimagined.Gui;

import net.txsla.itemarchivereimagined.DataTypes.Archive;
import net.txsla.itemarchivereimagined.DataTypes.Page;
import net.txsla.itemarchivereimagined.DataTypes.Placeholder;
import net.txsla.itemarchivereimagined.Storage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
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
        }else {
            // custom gui sizes, allows designer to choose from any inventory.
            switch (page.getSize()) {
                case 7:
                    inventory = Bukkit.createInventory(null, InventoryType.ANVIL, page.getName());
                    break;
                case 8:
                    inventory = Bukkit.createInventory(null, InventoryType.BARREL, page.getName());
                    break;
                case 9:
                    inventory = Bukkit.createInventory(null, InventoryType.BEACON, page.getName());
                    break;
                case 10:
                    inventory = Bukkit.createInventory(null, InventoryType.BLAST_FURNACE, page.getName());
                    break;
                case 11:
                    inventory = Bukkit.createInventory(null, InventoryType.BREWING, page.getName());
                    break;
                case 12:
                    inventory = Bukkit.createInventory(null, InventoryType.CARTOGRAPHY, page.getName());
                    break;
                case 13:
                    inventory = Bukkit.createInventory(null, InventoryType.CHEST, page.getName());
                    break;
                case 14:
                    inventory = Bukkit.createInventory(null, InventoryType.CHISELED_BOOKSHELF, page.getName());
                    break;
                case 15:
                    inventory = Bukkit.createInventory(null, InventoryType.COMPOSTER, page.getName());
                    break;
                case 16:
                    inventory = Bukkit.createInventory(null, InventoryType.CRAFTER, page.getName());
                    break;
                case 17:
                    inventory = Bukkit.createInventory(null, InventoryType.CRAFTING, page.getName());
                    break;
                case 18:
                    inventory = Bukkit.createInventory(null, InventoryType.CREATIVE, page.getName());
                    break;
                case 19:
                    inventory = Bukkit.createInventory(null, InventoryType.DECORATED_POT, page.getName());
                    break;
                case 20:
                    inventory = Bukkit.createInventory(null, InventoryType.DISPENSER, page.getName());
                    break;
                case 21:
                    inventory = Bukkit.createInventory(null, InventoryType.DROPPER, page.getName());
                    break;
                case 22:
                    inventory = Bukkit.createInventory(null, InventoryType.ENCHANTING, page.getName());
                    break;
                case 23:
                    inventory = Bukkit.createInventory(null, InventoryType.ENDER_CHEST, page.getName());
                    break;
                case 24:
                    inventory = Bukkit.createInventory(null, InventoryType.FURNACE, page.getName());
                    break;
                case 25:
                    inventory = Bukkit.createInventory(null, InventoryType.GRINDSTONE, page.getName());
                    break;
                case 26:
                    inventory = Bukkit.createInventory(null, InventoryType.HOPPER, page.getName());
                    break;
                case 27:
                    inventory = Bukkit.createInventory(null, InventoryType.JUKEBOX, page.getName());
                    break;
                case 28:
                    inventory = Bukkit.createInventory(null, InventoryType.LECTERN, page.getName());
                    break;
                case 29:
                    inventory = Bukkit.createInventory(null, InventoryType.LOOM, page.getName());
                    break;
                case 30:
                    inventory = Bukkit.createInventory(null, InventoryType.MERCHANT, page.getName());
                    break;
                case 31:
                    inventory = Bukkit.createInventory(null, InventoryType.PLAYER, page.getName());
                    break;
                case 32:
                    inventory = Bukkit.createInventory(null, InventoryType.SHULKER_BOX, page.getName());
                    break;
                case 33:
                    inventory = Bukkit.createInventory(null, InventoryType.SMITHING, page.getName());
                    break;
                case 34:
                    inventory = Bukkit.createInventory(null, InventoryType.SMOKER, page.getName());
                    break;
                case 35:
                    inventory = Bukkit.createInventory(null, InventoryType.STONECUTTER, page.getName());
                    break;
                case 36:
                    inventory = Bukkit.createInventory(null, InventoryType.WORKBENCH, page.getName());
                    break;
                default: // default to a max sized inventory
                    inventory = Bukkit.createInventory(null, 54, page.getName());
            }
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

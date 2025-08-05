package net.txsla.itemarchivereimagined.Gui;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class loadInv {
    public static Inventory create(int size, String name) {
        Inventory inventory;
        // process placeholders and add them to inventory
        if (size < 7) {
            inventory = Bukkit.createInventory(null, size * 9, name);
        }else {
            // custom gui sizes, allows designer to choose from any inventory.
            switch (size) {
                case 7:
                    inventory = Bukkit.createInventory(null, InventoryType.ANVIL, name);
                    break;
                case 8:
                    inventory = Bukkit.createInventory(null, InventoryType.BARREL, name);
                    break;
                case 9:
                    inventory = Bukkit.createInventory(null, InventoryType.BEACON, name);
                    break;
                case 10:
                    inventory = Bukkit.createInventory(null, InventoryType.BLAST_FURNACE, name);
                    break;
                case 11:
                    inventory = Bukkit.createInventory(null, InventoryType.BREWING, name);
                    break;
                case 12:
                    inventory = Bukkit.createInventory(null, InventoryType.CARTOGRAPHY, name);
                    break;
                case 13:
                    inventory = Bukkit.createInventory(null, InventoryType.CHEST, name);
                    break;
                case 14:
                    inventory = Bukkit.createInventory(null, InventoryType.CHISELED_BOOKSHELF, name);
                    break;
                case 15:
                    inventory = Bukkit.createInventory(null, InventoryType.COMPOSTER, name);
                    break;
                case 16:
                    inventory = Bukkit.createInventory(null, InventoryType.CRAFTER, name);
                    break;
                case 17:
                    inventory = Bukkit.createInventory(null, InventoryType.CRAFTING, name);
                    break;
                case 18:
                    inventory = Bukkit.createInventory(null, InventoryType.CREATIVE, name);
                    break;
                case 19:
                    inventory = Bukkit.createInventory(null, InventoryType.DECORATED_POT, name);
                    break;
                case 20:
                    inventory = Bukkit.createInventory(null, InventoryType.DISPENSER, name);
                    break;
                case 21:
                    inventory = Bukkit.createInventory(null, InventoryType.DROPPER, name);
                    break;
                case 22:
                    inventory = Bukkit.createInventory(null, InventoryType.ENCHANTING, name);
                    break;
                case 23:
                    inventory = Bukkit.createInventory(null, InventoryType.ENDER_CHEST, name);
                    break;
                case 24:
                    inventory = Bukkit.createInventory(null, InventoryType.FURNACE, name);
                    break;
                case 25:
                    inventory = Bukkit.createInventory(null, InventoryType.GRINDSTONE, name);
                    break;
                case 26:
                    inventory = Bukkit.createInventory(null, InventoryType.HOPPER, name);
                    break;
                case 27:
                    inventory = Bukkit.createInventory(null, InventoryType.JUKEBOX, name);
                    break;
                case 28:
                    inventory = Bukkit.createInventory(null, InventoryType.LECTERN, name);
                    break;
                case 29:
                    inventory = Bukkit.createInventory(null, InventoryType.LOOM, name);
                    break;
                case 30:
                    inventory = Bukkit.createInventory(null, InventoryType.MERCHANT, name);
                    break;
                case 31:
                    inventory = Bukkit.createInventory(null, InventoryType.PLAYER, name);
                    break;
                case 32:
                    inventory = Bukkit.createInventory(null, InventoryType.SHULKER_BOX, name);
                    break;
                case 33:
                    inventory = Bukkit.createInventory(null, InventoryType.SMITHING, name);
                    break;
                case 34:
                    inventory = Bukkit.createInventory(null, InventoryType.SMOKER, name);
                    break;
                case 35:
                    inventory = Bukkit.createInventory(null, InventoryType.STONECUTTER, name);
                    break;
                case 36:
                    inventory = Bukkit.createInventory(null, InventoryType.WORKBENCH, name);
                    break;
                default: // default to a max sized inventory
                    inventory = Bukkit.createInventory(null, 54, name);
            }
        }
        return inventory;
    }
}

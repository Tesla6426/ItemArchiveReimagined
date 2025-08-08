package net.txsla.itemarchivereimagined.Gui;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class loadInv {
    public static Inventory create(int size, String name) {
        Inventory inventory;
        // process placeholders and add them to inventory
        if (size < 7) {
            inventory = Bukkit.createInventory(null, size * 9, MiniMessage.miniMessage().deserialize(name));
        }else {
            // custom gui sizes, allows designer to choose from any inventory.
            switch (size) {
                case 7:
                    inventory = Bukkit.createInventory(null, InventoryType.ANVIL, MiniMessage.miniMessage().deserialize(name));
                    break;
                case 8:
                    inventory = Bukkit.createInventory(null, InventoryType.BARREL, MiniMessage.miniMessage().deserialize(name));
                    break;
                case 9:
                    inventory = Bukkit.createInventory(null, InventoryType.BEACON, MiniMessage.miniMessage().deserialize(name));
                    break;
                case 10:
                    inventory = Bukkit.createInventory(null, InventoryType.BLAST_FURNACE, MiniMessage.miniMessage().deserialize(name));
                    break;
                case 11:
                    inventory = Bukkit.createInventory(null, InventoryType.BREWING, MiniMessage.miniMessage().deserialize(name));
                    break;
                case 12:
                    inventory = Bukkit.createInventory(null, InventoryType.CARTOGRAPHY, MiniMessage.miniMessage().deserialize(name));
                    break;
                case 13:
                    inventory = Bukkit.createInventory(null, InventoryType.CHEST, MiniMessage.miniMessage().deserialize(name));
                    break;
                case 14:
                    inventory = Bukkit.createInventory(null, InventoryType.CHISELED_BOOKSHELF, MiniMessage.miniMessage().deserialize(name));
                    break;
                case 15:
                    inventory = Bukkit.createInventory(null, InventoryType.COMPOSTER, MiniMessage.miniMessage().deserialize(name));
                    break;
                case 16:
                    inventory = Bukkit.createInventory(null, InventoryType.CRAFTER, MiniMessage.miniMessage().deserialize(name));
                    break;
                case 17:
                    inventory = Bukkit.createInventory(null, InventoryType.CRAFTING, MiniMessage.miniMessage().deserialize(name));
                    break;
                case 18:
                    inventory = Bukkit.createInventory(null, InventoryType.CREATIVE, MiniMessage.miniMessage().deserialize(name));
                    break;
                case 19:
                    inventory = Bukkit.createInventory(null, InventoryType.DECORATED_POT, MiniMessage.miniMessage().deserialize(name));
                    break;
                case 20:
                    inventory = Bukkit.createInventory(null, InventoryType.DISPENSER, MiniMessage.miniMessage().deserialize(name));
                    break;
                case 21:
                    inventory = Bukkit.createInventory(null, InventoryType.DROPPER, MiniMessage.miniMessage().deserialize(name));
                    break;
                case 22:
                    inventory = Bukkit.createInventory(null, InventoryType.ENCHANTING, MiniMessage.miniMessage().deserialize(name));
                    break;
                case 23:
                    inventory = Bukkit.createInventory(null, InventoryType.ENDER_CHEST, MiniMessage.miniMessage().deserialize(name));
                    break;
                case 24:
                    inventory = Bukkit.createInventory(null, InventoryType.FURNACE, MiniMessage.miniMessage().deserialize(name));
                    break;
                case 25:
                    inventory = Bukkit.createInventory(null, InventoryType.GRINDSTONE, MiniMessage.miniMessage().deserialize(name));
                    break;
                case 26:
                    inventory = Bukkit.createInventory(null, InventoryType.HOPPER, MiniMessage.miniMessage().deserialize(name));
                    break;
                case 27:
                    inventory = Bukkit.createInventory(null, InventoryType.JUKEBOX, MiniMessage.miniMessage().deserialize(name));
                    break;
                case 28:
                    inventory = Bukkit.createInventory(null, InventoryType.LECTERN, MiniMessage.miniMessage().deserialize(name));
                    break;
                case 29:
                    inventory = Bukkit.createInventory(null, InventoryType.LOOM, MiniMessage.miniMessage().deserialize(name));
                    break;
                case 30:
                    inventory = Bukkit.createInventory(null, InventoryType.MERCHANT, MiniMessage.miniMessage().deserialize(name));
                    break;
                case 31:
                    inventory = Bukkit.createInventory(null, InventoryType.PLAYER, MiniMessage.miniMessage().deserialize(name));
                    break;
                case 32:
                    inventory = Bukkit.createInventory(null, InventoryType.SHULKER_BOX, MiniMessage.miniMessage().deserialize(name));
                    break;
                case 33:
                    inventory = Bukkit.createInventory(null, InventoryType.SMITHING, MiniMessage.miniMessage().deserialize(name));
                    break;
                case 34:
                    inventory = Bukkit.createInventory(null, InventoryType.SMOKER, MiniMessage.miniMessage().deserialize(name));
                    break;
                case 35:
                    inventory = Bukkit.createInventory(null, InventoryType.STONECUTTER, MiniMessage.miniMessage().deserialize(name));
                    break;
                case 36:
                    inventory = Bukkit.createInventory(null, InventoryType.WORKBENCH, MiniMessage.miniMessage().deserialize(name));
                    break;
                default: // default to a max sized inventory
                    inventory = Bukkit.createInventory(null, 54, MiniMessage.miniMessage().deserialize(name));
            }
        }
        return inventory;
    }
}

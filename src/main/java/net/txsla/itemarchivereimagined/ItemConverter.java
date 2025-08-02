package net.txsla.itemarchivereimagined;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemConverter {
    public static String toString(ItemStack savedItem) {
        YamlConfiguration itemConfig = new YamlConfiguration();
        itemConfig.set("item", savedItem);
        String serialized = itemConfig.saveToString();
        return serialized;
    }
    public static ItemStack toItemStack(String serialized) {
        ItemStack restoredItem;
        YamlConfiguration restoreConfig = new YamlConfiguration();
        try {
            restoreConfig.loadFromString(serialized);
            restoredItem = restoreConfig.getItemStack("item");
        } catch (Exception e) {
            ItemStack nullItem = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
            ItemMeta nullItemMeta = nullItem.getItemMeta();
            //nullItemMeta.setMaxStackSize(1);
            nullItemMeta.setDisplayName(ChatColor.RED + "Loading Item...");
            nullItem.setItemMeta(nullItemMeta);
            return nullItem;
        }

        if (restoredItem.getType().toString().equals(Material.AIR.toString())) {
            //System.out.println("serialized to AIR\n" + serialized);
        }

        return restoredItem;
    }
}

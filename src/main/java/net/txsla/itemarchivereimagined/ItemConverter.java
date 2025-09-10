package net.txsla.itemarchivereimagined;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.txsla.itemarchivereimagined.Gui.editArchive;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ItemConverter {
    public static String toString(ItemStack saveItem) {
        if (saveItem.isEmpty() || saveItem.getType().isAir()) {
            System.out.println("Cannot serialize Air!");
            return "null_item";
        }

        try {
            byte[] nbt = saveItem.serializeAsBytes();                    // Paper API
            return Base64.getEncoder().encodeToString(nbt);
        } catch (Exception e) {
            System.out.println("Error serializing item: \n" + e);
            return "failed_to_serialize";
        }
    }
    public static ItemStack toItemStack(String serialized) {
        try {
            byte[] nbt = Base64.getDecoder().decode(serialized);
            return ItemStack.deserializeBytes(nbt);
        } catch (Exception e) {
            return Storage.red_glass.getItemStack();
        }
    }
    public static ItemStack toItemStackOld(String serialized) {
        ItemStack restoredItem;
        YamlConfiguration restoreConfig = new YamlConfiguration();
        try {
            restoreConfig.loadFromString(serialized);
            restoredItem = restoreConfig.getItemStack("item");
        } catch (Exception e) {
            ItemStack nullItem = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
            ItemMeta nullItemMeta = nullItem.getItemMeta();
            nullItemMeta.setMaxStackSize(1);
            nullItemMeta.setDisplayName(ChatColor.RED + "Loading Item...");
            nullItem.setItemMeta(nullItemMeta);
            return nullItem;
        }
        return restoredItem;
    }


}

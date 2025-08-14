package net.txsla.itemarchivereimagined;

import net.txsla.itemarchivereimagined.Gui.editArchive;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        try {
            // Step 1: Load raw YAML into Map using SnakeYAML
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(serialized);

            // Step 2: Dive into item.meta and repair any binary strings
            Object itemObj = data.get("item");
            if (itemObj instanceof Map) {
                Map<String, Object> itemMap = (Map<String, Object>) itemObj;
                Object metaObj = itemMap.get("meta");
                if (metaObj instanceof Map) {
                    Map<String, Object> metaMap = (Map<String, Object>) metaObj;

                    Object dn = metaMap.get("display-name");
                    if (dn instanceof byte[]) {
                        String fixedName = new String((byte[]) dn, java.nio.charset.StandardCharsets.UTF_8);
                        metaMap.put("display-name", fixedName);
                    }
                    // Optional: also repair lore or other fields if they are byte[]
                    Object lore = metaMap.get("lore");
                    if (lore instanceof List) {
                        List list = (List) lore;
                        for (int i = 0; i < list.size(); i++) {
                            Object line = list.get(i);
                            if (line instanceof byte[]) {
                                list.set(i, new String((byte[]) line, java.nio.charset.StandardCharsets.UTF_8));
                            }
                        }
                    }
                }
            }

            // Step 3: Dump repaired YAML back to string
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yamlOut = new Yaml(options);
            String fixedSerialized = yamlOut.dump(data);

            // Step 4: Let Bukkit parse the repaired YAML
            YamlConfiguration restoreConfig = new YamlConfiguration();
            restoreConfig.loadFromString(fixedSerialized);
            return restoreConfig.getItemStack("item");

        } catch (Exception e) {
            // Fallback
            ItemStack nullItem = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
            ItemMeta nullItemMeta = nullItem.getItemMeta();
            nullItemMeta.setDisplayName(ChatColor.RED + "Loading Item...");
            nullItem.setItemMeta(nullItemMeta);
            return nullItem;
        }
    }
}

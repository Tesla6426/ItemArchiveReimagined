package net.txsla.itemarchivereimagined;

import net.txsla.itemarchivereimagined.Commands.submitRaw;
import net.txsla.itemarchivereimagined.DataTypes.Item;
import net.txsla.itemarchivereimagined.DataTypes.Placeholder;
import net.txsla.itemarchivereimagined.Gui.editArchive;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class ItemArchiveReimagined extends JavaPlugin {

    // for development  - remove later

    public static boolean debug = true;

    @Override
    public void onEnable() {
        Storage.server = this;

        // load archives
        load.archives();

        // load debug items
        List<String> list = new ArrayList<>(); ItemMeta meta;

        Storage.air = new Item(new ItemStack(Material.AIR), "herobrine");

        ItemStack glass = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        meta = glass.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Error: Item not found!");
        glass.setItemMeta(meta);
        Storage.red_glass = new Item(glass, "herobrine");

        Storage.invalid_placeholder = new Placeholder("invalid", 0, "none", Storage.red_glass.getItemStack());;

        Storage.grass = new ItemStack(Material.GRASS_BLOCK);
        meta = Storage.grass.getItemMeta();
        meta.setDisplayName("Default Placeholder Item");
        list.add("Use command /edit <archive> placeholder modify <placeholder> item");
        list.add("to set placeholder item to item in your hand.");
        meta.setLore(list);
        Storage.grass.setItemMeta(meta);
        list.clear();

        editArchive.vault_placeholder = new ItemStack(Material.STRUCTURE_VOID);
        meta = editArchive.vault_placeholder.getItemMeta();
        meta.setDisplayName("Vault_Populator");
        list.add("On pages 1+, this placeholder is replaced by items from the archive's vault");
        list.add("On page 0 (submission page), this placeholder is used as the submission slots");
        meta.setLore(list);
        editArchive.vault_placeholder.setItemMeta(meta);
        list.clear();

        editArchive.air_placeholder = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        meta = editArchive.air_placeholder.getItemMeta();
        meta.setDisplayName("Air_Populator");
        list.add("This item is used to represent air when viewing a page in debug or edit mode");
        meta.setLore(list);
        editArchive.air_placeholder.setItemMeta(meta);
        list.clear();

        editArchive.swap_view_1 = new ItemStack(Material.ITEM_FRAME);
        meta = editArchive.swap_view_1.getItemMeta();
        meta.setDisplayName("Page View");
        list.add("Click to swap to raw view");
        meta.setLore(list);
        editArchive.swap_view_1.setItemMeta(meta);
        list.clear();

        editArchive.swap_view_2 = new ItemStack(Material.GLOW_ITEM_FRAME);
        meta = editArchive.swap_view_2.getItemMeta();
        meta.setDisplayName("Raw View");
        list.add("Click to swap to page view");
        meta.setLore(list);
        editArchive.swap_view_2.setItemMeta(meta);
        list.clear();

        editArchive.null_slot = new ItemStack(Material.IRON_BARS);
        meta = editArchive.null_slot.getItemMeta();
        meta.setDisplayName("Null Slot");
        list.add("This slot does not exist");
        meta.setLore(list);
        editArchive.null_slot.setItemMeta(meta);
        list.clear();

        editArchive.reload_selection = new ItemStack(Material.CONDUIT);
        meta = editArchive.reload_selection.getItemMeta();
        meta.setDisplayName("Reload Placeholder Selection");
        list.add("In case you accidentally delete one");
        meta.setLore(list);
        editArchive.reload_selection.setItemMeta(meta);
        list.clear();

        editArchive.scroll_right = new ItemStack(Material.OAK_BUTTON);
        meta = editArchive.scroll_right.getItemMeta();
        meta.setDisplayName("Scroll Right");
        editArchive.scroll_right.setItemMeta(meta);
        list.clear();

        editArchive.scroll_left = new ItemStack(Material.OAK_BUTTON);
        meta = editArchive.scroll_left.getItemMeta();
        meta.setDisplayName("Scroll Left");
        editArchive.scroll_left.setItemMeta(meta);
        list.clear();

        // register commands
        getCommand("create").setExecutor(new net.txsla.itemarchivereimagined.Commands.create() );
        getCommand("raw").setExecutor(new net.txsla.itemarchivereimagined.Commands.raw() );
        getCommand("search").setExecutor(new net.txsla.itemarchivereimagined.Commands.search() );
        getCommand("open").setExecutor(new net.txsla.itemarchivereimagined.Commands.open() );
        getCommand("submit").setExecutor(new net.txsla.itemarchivereimagined.Commands.submit() );
        getCommand("edit").setExecutor(new net.txsla.itemarchivereimagined.Commands.edit() );

        // dev / testing
        getCommand("loadoldarchive").setExecutor(new net.txsla.itemarchivereimagined.Commands.loadoldarchive() );
        getCommand("openDemoInventory").setExecutor(new net.txsla.itemarchivereimagined.Commands.openDemoInventory() );
        getCommand("submitRaw").setExecutor(new submitRaw() );


        getServer().getPluginManager().registerEvents(new Listener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

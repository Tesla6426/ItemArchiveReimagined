package net.txsla.itemarchivereimagined;

import net.txsla.itemarchivereimagined.Commands.submitRaw;
import net.txsla.itemarchivereimagined.DataTypes.Item;
import net.txsla.itemarchivereimagined.DataTypes.Placeholder;
import net.txsla.itemarchivereimagined.DataTypes.Sound;
import net.txsla.itemarchivereimagined.Gui.editArchive;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class ItemArchiveReimagined extends JavaPlugin {
    @Override
    public void onEnable() {
        Storage.server = this;

        // load archives
        load.archives();

        // load default items
        load_items_init();

        // register commands
        getCommand("create-archive").setExecutor(new net.txsla.itemarchivereimagined.Commands.create() );
        getCommand("raw").setExecutor(new net.txsla.itemarchivereimagined.Commands.raw() );
        getCommand("search").setExecutor(new net.txsla.itemarchivereimagined.Commands.search() );
        getCommand("open").setExecutor(new net.txsla.itemarchivereimagined.Commands.open() );
        getCommand("submit").setExecutor(new net.txsla.itemarchivereimagined.Commands.submit() );
        getCommand("edit").setExecutor(new net.txsla.itemarchivereimagined.Commands.edit() );
        getCommand("remove-item").setExecutor(new net.txsla.itemarchivereimagined.Commands.removeItem() );



        getCommand("count-items").setExecutor(new net.txsla.itemarchivereimagined.Commands.count() );
        getCommand("add-editor").setExecutor(new net.txsla.itemarchivereimagined.Commands.addEditor() );
        //getCommand("review-items").setExecutor(new net.txsla.itemarchivereimagined.Commands.reviewItems() );
        getCommand("submit-ban").setExecutor(new net.txsla.itemarchivereimagined.Commands.submitBan() );



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



    public static void load_items_init() {
        // load debug items
        List<String> list = new ArrayList<>(); ItemMeta meta;

        Storage.air = new Item(new ItemStack(Material.AIR), "herobrine");

        ItemStack glass = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        meta = glass.getItemMeta();
        meta.setDisplayName("§cError: Item not found!");
        glass.setItemMeta(meta);
        Storage.red_glass = new Item(glass, "herobrine");

        Storage.invalid_placeholder = new Placeholder("invalid", 0, "none", new Sound("null").serialize(), Storage.red_glass.getItemStack());;

        Storage.grass = new ItemStack(Material.GRASS_BLOCK);
        meta = Storage.grass.getItemMeta();
        meta.setDisplayName("Default Placeholder Item");
        list.add("§7Use command /edit <archive> placeholder modify <placeholder> item");
        list.add("§7to set placeholder item to item in your hand.");
        meta.setLore(list);
        Storage.grass.setItemMeta(meta);
        list.clear();

        editArchive.vault_placeholder = new ItemStack(Material.STRUCTURE_VOID);
        meta = editArchive.vault_placeholder.getItemMeta();
        meta.setDisplayName("§8Vault_Populator");
        list.add("§7On pages 1+, this placeholder is replaced by items from the archive's vault");
        list.add("§7On page 0 (submission page), this placeholder is used as the submission slots");
        meta.setLore(list);
        editArchive.vault_placeholder.setItemMeta(meta);
        list.clear();

        editArchive.air_placeholder = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        meta = editArchive.air_placeholder.getItemMeta();
        meta.setDisplayName("§8Air_Populator");
        list.add("§7This item is used to represent air when viewing a page in debug or edit mode");
        meta.setLore(list);
        editArchive.air_placeholder.setItemMeta(meta);
        list.clear();

        editArchive.swap_view_1 = new ItemStack(Material.ITEM_FRAME);
        meta = editArchive.swap_view_1.getItemMeta();
        meta.setDisplayName("Page View");
        list.add("§7Click to swap to raw view");
        meta.setLore(list);
        editArchive.swap_view_1.setItemMeta(meta);
        list.clear();

        editArchive.swap_view_2 = new ItemStack(Material.GLOW_ITEM_FRAME);
        meta = editArchive.swap_view_2.getItemMeta();
        meta.setDisplayName("Raw View");
        list.add("§7Click to swap to page view");
        meta.setLore(list);
        editArchive.swap_view_2.setItemMeta(meta);
        list.clear();

        editArchive.null_slot = new ItemStack(Material.IRON_BARS);
        meta = editArchive.null_slot.getItemMeta();
        meta.setDisplayName("§8Null Slot");
        list.add("§8This slot does not exist");
        meta.setLore(list);
        editArchive.null_slot.setItemMeta(meta);
        list.clear();

        editArchive.reload_selection = new ItemStack(Material.CONDUIT);
        meta = editArchive.reload_selection.getItemMeta();
        meta.setDisplayName("§8Reload Placeholder Selection");
        list.add("§7In case you accidentally delete one");
        meta.setLore(list);
        editArchive.reload_selection.setItemMeta(meta);
        list.clear();

        editArchive.scroll_right = new ItemStack(Material.OAK_BUTTON);
        meta = editArchive.scroll_right.getItemMeta();
        meta.setDisplayName("-->");
        editArchive.scroll_right.setItemMeta(meta);
        list.clear();

        editArchive.scroll_left = new ItemStack(Material.OAK_BUTTON);
        meta = editArchive.scroll_left.getItemMeta();
        meta.setDisplayName("<--");
        editArchive.scroll_left.setItemMeta(meta);
        list.clear();

        editArchive.save = new ItemStack(Material.LIME_SHULKER_BOX);
        meta = editArchive.save.getItemMeta();
        meta.setDisplayName("§aSAVE");
        list.add("§7Exit and save changes");
        meta.setLore(list);
        editArchive.save.setItemMeta(meta);
        list.clear();

        editArchive.abort = new ItemStack(Material.RED_SHULKER_BOX);
        meta = editArchive.abort.getItemMeta();
        meta.setDisplayName("§4ABORT");
        list.add("§cExit WITHOUT saving");
        meta.setLore(list);
        editArchive.abort.setItemMeta(meta);
        list.clear();

        editArchive.save_session = new ItemStack(Material.LIGHT_BLUE_SHULKER_BOX);
        meta = editArchive.save_session.getItemMeta();
        meta.setDisplayName("§1Save to Cache");
        list.add("§9Saves all current changes to session cache");
        list.add("§9Make sure to save after making any changes");
        meta.setLore(list);
        editArchive.save_session.setItemMeta(meta);
        list.clear();
    }
}

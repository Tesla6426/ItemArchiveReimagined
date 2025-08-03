package net.txsla.itemarchivereimagined;

import net.txsla.itemarchivereimagined.DataTypes.Item;
import net.txsla.itemarchivereimagined.DataTypes.Placeholder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Path;

public final class ItemArchiveReimagined extends JavaPlugin {

    // for development  - remove later

    public static boolean debug = true;

    @Override
    public void onEnable() {
        Storage.server = this;

        // load archives
        load.archives();

        // load debug items
        Storage.air = new Item(new ItemStack(Material.AIR), "herobrine");
        ItemStack glass = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta meta = glass.getItemMeta(); meta.setDisplayName(ChatColor.RED + "Error: Item not found!"); glass.setItemMeta(meta);
        Storage.red_glass = new Item(glass, "herobrine");
        Storage.invalid_placeholder = new Placeholder("invalid", 0, "none", Storage.red_glass.getItemStack());

        // register commands
        getCommand("submit").setExecutor(new net.txsla.itemarchivereimagined.Commands.submit() );
        getCommand("create").setExecutor(new net.txsla.itemarchivereimagined.Commands.create() );
        getCommand("raw").setExecutor(new net.txsla.itemarchivereimagined.Commands.raw() );
        getCommand("search").setExecutor(new net.txsla.itemarchivereimagined.Commands.search() );
        getCommand("open").setExecutor(new net.txsla.itemarchivereimagined.Commands.open() );
        getCommand("loadoldarchive").setExecutor(new net.txsla.itemarchivereimagined.Commands.loadoldarchive() );

        getServer().getPluginManager().registerEvents(new Listener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

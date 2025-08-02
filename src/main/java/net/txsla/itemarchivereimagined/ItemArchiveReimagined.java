package net.txsla.itemarchivereimagined;

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

        // register commands
        getCommand("submit").setExecutor(new net.txsla.itemarchivereimagined.Commands.submit() );
        getCommand("create").setExecutor(new net.txsla.itemarchivereimagined.Commands.create() );
        getCommand("raw").setExecutor(new net.txsla.itemarchivereimagined.Commands.raw() );
        getCommand("search").setExecutor(new net.txsla.itemarchivereimagined.Commands.search() );
        getCommand("loadoldarchive").setExecutor(new net.txsla.itemarchivereimagined.Commands.loadoldarchive() );
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

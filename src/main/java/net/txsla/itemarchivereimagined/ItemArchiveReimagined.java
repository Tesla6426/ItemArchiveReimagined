package net.txsla.itemarchivereimagined;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Path;

public final class ItemArchiveReimagined extends JavaPlugin {

    // for development  - remove later
    public static File directory;
    public static boolean debug = true;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();


        // load file path for Archives
        directory = new File(this.getDataPath().getRoot() + File.separator + "Archives");
        load.directory = directory; // Yes, this is redundant. It will be optimised by compiler later, so it does not matter



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

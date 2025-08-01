package net.txsla.itemarchivereimagined;

import java.io.File;
import java.nio.file.Path;
import dev.dejvokep.boostedyaml.YamlDocument;
import net.txsla.itemarchivereimagined.DataTypes.Archive;
import net.txsla.itemarchivereimagined.DataTypes.Vault;

public class load {
    public static File directory;
    public static void archives() {
        // this should only be run once per server (re)start
        System.out.println("Loading Archives");

        // make sure archive directory exists
        if (!directory.exists()) {
            System.out.println(directory.mkdirs()
                    ? "Archive Directory created successfully: " + directory.getAbsolutePath()
                    : "ERROR: Failed to create archive directory!");
            System.out.println("No archives found!");
            return;
        }

        // get subdirs (archive folders)
        File[] archives = directory.listFiles(File::isDirectory);

        // handle no archives case
        if (archives == null) { System.out.println("No archives found!"); return; }

        // DEBUG REMOVE LATER
        for (File f : archives) System.out.println("Archive found: " + f.toString());

        // load all archives
        for (File archive : archives) archive(archive.getName());
        System.out.println("Finished Loading all Archives and associated Vaults!");
    }
    public static void archive(String name) {
        // (re)load a singular archive

        // unload
        if (Storage.archives.containsKey(name)) {
            System.out.println("Unloading archive " + name);
            Storage.archives.remove(name);
            Storage.vaults.remove(name + "-review");
            Storage.vaults.remove(name + "-main");
            Storage.vaults.remove(name + "-rejected");
        }
        //load
        new Thread(() -> {
            System.out.println("Loading " + name + ".");
            // load archive object
            Storage.archives.put(name, new Archive(name));
            // load vaults (review 1st, main 2nd, rejected 3rd)
            Storage.vaults.put(name + "-review", new Vault("review", name));
            Storage.vaults.put(name + "-main", new Vault("main", name));
            Storage.vaults.put(name + "-rejected", new Vault("rejected", name));
            System.out.println("Finished loading " + name + "!");
        }, name + "_loading").start();
    }
}

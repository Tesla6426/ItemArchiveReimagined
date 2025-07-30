package net.txsla.itemarchivereimagined;

import java.io.File;
import java.nio.file.Path;
import dev.dejvokep.boostedyaml.YamlDocument;

    public class load {
    public static File directory;

        // vaults and archives are saved, loaded and stored separately for maximum performance

        // server/Archives/archive_name/
        // archive_name.conf
        // archive_name.vault
        // archive_name-review.vault



    public static void archives() {
        // this should only be run once per server (re)start
        System.out.println("Loading Archives");


        // get list of directories in Archive/ folder

        // make sure archive directory exists
        if (!directory.exists()) {
            if (directory.mkdirs()) {
            System.out.println("Archive Directory created successfully: " + directory.getAbsolutePath());
            } else {
            System.out.println("ERROR: Failed to create archive directory!");
            }
            // if this is ever ran, then there are no archives
            System.out.println("No archives found!");
            return;
        }
        // get subdirs (archive folders)
        File[] archives = directory.listFiles(File::isDirectory);
        // handle no archives case
        if (archives == null) {
            System.out.println("No archives found!");
            return;
        }
        // DEBUG REMOVE LATER
        if (ItemArchiveReimagined.debug) {
            System.out.println("Looking for archives...");
            for (File f : archives) {
                System.out.println("Archive found: " + f.toString());
            }
        }

        // load archive configs



    }
    public static void vaults() {
        // load vaults
        // there should be three vaults per archive
        // 1: main vault, 2: review vault, 3: rejected vault
    }
}

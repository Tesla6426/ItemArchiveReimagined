package net.txsla.itemarchivereimagined.DataTypes;

import net.txsla.itemarchivereimagined.b64;
import net.txsla.itemarchivereimagined.deserialize;
import net.txsla.itemarchivereimagined.load;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static net.txsla.itemarchivereimagined.Storage.saveToFile;

public class Vault {
    // This data type is used to load/retrieve saved items in an archive
    private List<Item> items; // list of all items
    private Set<String> item_uuids = new HashSet<>(); // allows for much faster checking at the expense of initial loading speed
    private List<String> file = new ArrayList<>(); // yes, I am storing the entire vault file in RAM. It should only add the file size of the vault file to ram (~55mb*2 as of 8/1/2025)
    private String name;
    private Path file_location;
    private String archive_name;
    public List<Item> getItems() {return items;}
    public Item getItem(int index) {return items.get(index);}
    public int getSize() {return item_uuids.size();}




    public boolean checkDuplicate(String uuid) {
        return item_uuids.contains(uuid);
    }
    public Vault(String name, String archive_name) {
        // load from vault file in archive folder
        // archive_name is the name of the folder, name.vault is the name of the vault
        this.name = name; // name suffix
        this.archive_name = archive_name; // name prefix
        this.file_location = Paths.get(load.directory + File.separator + archive_name + File.separator + name + ".vault");


        // make sure file actually exists, create it if it does not
        if (!this.file_location.toFile().exists()) {
            try {
                Files.createDirectories(this.file_location.getParent());
                Files.createFile(this.file_location);
            } catch (Exception e) {}
        }

        // load into RAM and create item list
        loadFileIntoRAM();
        loadItemsFromRAM();
    }
    public void loadItemsFromRAM() {
        // decode items from file
        item_uuids.clear(); this.items = new ArrayList<>();// clear current lists
        Item current_item; // don't make a billion extra objects in mem
        for (String encoded_item : this.file) {
            current_item = deserialize.item(encoded_item);
            // add item and UUID to list
            this.item_uuids.add(current_item.getUUID());
            this.items.add(current_item);
        }
        System.out.println("Vault " + archive_name + "-" + name + " loaded from RAM" );
    }
    public void saveRamToFile() {
        try {
            saveToFile(this.file, this.file_location);
            System.out.println("Vault " + this.archive_name + "-" + this.name + " saved!");
        }catch (Exception e) {
            System.out.println("Unable to save vault " + this.archive_name + "-" + this.name + "!");
        }
    }
    public void saveItemsToRam() {
        // create a new file with updated file list
        // make sure to benchmark this later, as I am quite curious as to how it performs
        this.file = new ArrayList<>();
        for (Item item : this.items) file.add(item.serialize());
        System.out.println("Vault " + archive_name + "-" + name + " items saved to RAM" );
    }
    public void loadFileIntoRAM() {
        try {
            this.file = Files.readAllLines(this.file_location, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Failed to load Vault file " + this.archive_name + "-" + this.name + " from " + file_location);
            e.printStackTrace();
        }
    }


    // search features, these are slow, so make sure only supporters gain access
    public List<Item> searchFromItemName(String regex, int amount) {
        List<Item> items_found = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        for (Item item : this.items) {
            if (pattern.matcher(item.getName()).find()) items_found.add(item);
            if (items_found.size() >= amount) break;
        }
        return items_found;
    }
    public List<Item> searchFromItemUUID(String regex, int amount) {
        List<Item> items_found = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        for (Item item : this.items) {
            if (pattern.matcher(item.getUUID()).find()) items_found.add(item);
            if (items_found.size() >= amount) break;
        }
        return items_found;
    }
    public List<Item> searchFromSubmitterName(String regex, int amount) {
        List<Item> items_found = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        for (Item item : this.items) {
            if (pattern.matcher(item.getSubmitterName()).find()) items_found.add(item);
            if (items_found.size() >= amount) break;
        }
        return items_found;
    }
    public List<Item> searchFromSubmitterUUID(String regex, int amount) {
        List<Item> items_found = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        for (Item item : this.items) {
            if (pattern.matcher(item.getSubmitterUUID()).find()) items_found.add(item);
            if (items_found.size() >= amount) break;
        }
        return items_found;
    }
    public List<Item> searchFromItemVersion(String regex, int amount) {
        List<Item> items_found = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        for (Item item : this.items) {
            if (pattern.matcher(item.getItemVersion()).find()) items_found.add(item);
            if (items_found.size() >= amount) break;
        }
        return items_found;
    }
    public List<Item> searchFromItemLanguage(String regex, int amount) {
        List<Item> items_found = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        for (Item item : this.items) {
            if (pattern.matcher(item.getLanguage()).find()) items_found.add(item);
            if (items_found.size() >= amount) break;
        }
        return items_found;
    }
    public List<Item> searchFromItemSize(int size, boolean greater, int amount) {
        List<Item> items_found = new ArrayList<>();
        double current_size;
        for (Item item : this.items) {
            current_size = item.getItemSize();
            if (greater ? current_size > size : current_size < size) items_found.add(item);
            if (items_found.size() >= amount) break;
        }
        return items_found;
    }
    // This one can be extremely expensive - only high tier supporters will gain access
    public List<Item> searchFromItemNBT(String regex, int amount) {
        List<Item> items_found = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        for (Item item : this.items) {
            if (pattern.matcher(item.getItemStack().getItemMeta().getAsComponentString()).find()) items_found.add(item);
            if (items_found.size() >= amount) break;
        }
        return items_found;
    }
}

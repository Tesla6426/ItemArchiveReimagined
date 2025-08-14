package net.txsla.itemarchivereimagined.DataTypes;

import net.txsla.itemarchivereimagined.ItemConverter;
import net.txsla.itemarchivereimagined.Storage;
import net.txsla.itemarchivereimagined.b64;
import net.txsla.itemarchivereimagined.hash;
import org.bukkit.Bukkit;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Item {
    private String item_name;
    private String item_uuid;
    private String submitter_name;
    private String submitter_uuid;
    private double item_date;
    private double item_size;
    private String item_version;
    private ItemStack item_stack;
    private String item_language;
    public Item(ItemStack item, Player submitter) {
        // this constructor is for loading submitted items
        this.item_stack = item;
        this.submitter_name = submitter.getName();
        this.submitter_uuid = submitter.getUniqueId().toString();

        // make sure item name is not too long
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasDisplayName()) {
            String name = meta.getDisplayName();
            this.item_name = name.length() > 256 ? name.substring(0, 255) : name;
        } else {
            this.item_name = "unnamed_item";
        }

        this.item_size = item.toString().getBytes().length;
        this.item_date = System.currentTimeMillis();
        this.item_language = submitter.locale().getDisplayLanguage();
        this.item_version = Bukkit.getMinecraftVersion();
        this.item_uuid = hash.getUUID(item);
    }
    public Item(ItemStack item, String submitter_name) {
        // load from old file format
        // this method should not be needed as I plan on making a .ar -> .vault file converter built into this plugin
        this.item_stack = item;

        // make sure item name is not too long
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasDisplayName()) {
            String name = meta.getDisplayName();
            this.item_name = name.length() > 256 ? name.substring(0, 255) : name;
        } else {
            this.item_name = "unnamed_item";
        }

        this.item_size = item.toString().getBytes().length;
        this.item_date = System.currentTimeMillis();

        this.submitter_name = submitter_name;
        this.submitter_uuid = Storage.server.getServer().getOfflinePlayer(submitter_name).getUniqueId().toString();

        this.item_version = Bukkit.getMinecraftVersion();

        if (this.item_stack.getItemMeta() != null) {
            this.item_uuid = hash.getUUID(this.item_stack);
        } else {
            System.out.println("No Metadata on item " + item.toString() + "?");
            this.item_uuid = "???";
        }

        this.item_language = "null";
    }
    public Item(String item_name, String item_uuid, String submitter_name, String submitter_uuid, double item_date, double item_size, String item_version, ItemStack item_stack, String item_language) {
        // there are no protections for this method
        // make sure to input only valid data
        this.item_name = item_name;
        this.submitter_name = submitter_name;
        this.submitter_uuid = submitter_uuid;
        this.item_date = item_date;
        this.item_size = item_size;
        this.item_version = item_version;
        this.item_stack = item_stack;
        this.item_uuid = hash.getUUID(item_stack);
        this.item_language = item_language;
    }



    // GET VALUE

    public String getName() {
        if (this.item_name == null) return "unnamed_item";
        return item_name;
    }
    public String getUUID() {
        return this.item_uuid != null ? this.item_uuid : hash.getUUID(this.item_stack);
    }
    public String getSubmitterName() {
        return this.submitter_name;
    }
    public String getSubmitterUUID() {
        return this.submitter_uuid;
    }
    public double getItemSubmitDate() {
        return item_date;
    }
    public double getItemSize() {
        return item_size;
    }
    public String getItemVersion() {
        return this.item_version;
    }
    public ItemStack getItemStack() {
        return this.item_stack;
    }
    public String getLanguage() {
        return this.item_language;
    }








    // this serialization will be a bit different,
    // it will be formatted in order to be more human-readable when the data is viewed
    // while this will make the code a few microseconds longer to run, it will be very worth it considering ths specific method only needs to be run once or twice per item ever
    public String serialize() {
        // make sure display name stays under 64 chars
        String name = this.item_name;
        // safely cut the string
        if (name.length() > 64) {
            int cut = 64;
            if (Character.isHighSurrogate(name.charAt(cut - 1))) {
                // drop the unpaired high surrogate
                cut--;
            }
            name = name.substring(0, cut);
        }

        // human-readable section, this is discarded upon deserialization, (except for uuid)
        String out =
                name.replace("¦", "|") + "¦" +
                this.item_stack.getType() + "¦" +
                this.getSubmitterName() + "¦" +
                this.getUUID() + "¦"
                ;
        String data = b64.encode(
                    b64.encode(this.item_name)+ "¦" +
                    b64.encode(this.submitter_name) + "¦" +
                    b64.encode(this.submitter_uuid) + "¦" +
                    b64.encode(""+this.item_date) + "¦" +
                    b64.encode(""+this.item_size) + "¦" +
                    b64.encode(this.item_version) + "¦" +
                    b64.encode(ItemConverter.toString(this.item_stack)) + "¦" +
                    b64.encode(item_language)
        );


        return out + data;
    }
    public static Item deserialize(String serialized) {
        String[] out = serialized.split("¦"); // out+data
        String[] data = b64.decode(out[4]).split("¦"); // data
        return new Item(
                b64.decode(data[0]),
                b64.decode(out[3]),
                b64.decode(data[1]),
                b64.decode(data[2]),
                Double.parseDouble(b64.decode(data[3])),
                Double.parseDouble(b64.decode(data[4])),
                b64.decode(data[5]),
                ItemConverter.toItemStack(b64.decode(data[6])),
                b64.decode(data[7])
        );
    }
}

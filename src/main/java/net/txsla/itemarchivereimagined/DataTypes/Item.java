package net.txsla.itemarchivereimagined.DataTypes;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Item {
    public static Server server;
    private String item_name;
    private String item_uuid;
    private String submitter_name;
    private String submitter_uuid;
    private double item_date;
    private double item_size;
    private String item_version;
    private ItemStack item_stack;
    private String item_language;
    private String item_country;

    public Item(ItemStack item) {
        // this constructor is for loading items from file, populate the other fields with .setX()
        this.item_stack = item;
        this.item_name = item.getItemMeta().getDisplayName().substring(0, 255);
        this.item_size = item.toString().getBytes().length;
    }
    public Item(ItemStack item, Player submitter) {
        // this constructor is for loading submitted items
        this.item_stack = item;
        this.submitter_name = submitter.getName();
        this.submitter_uuid = submitter.getUniqueId().toString();
        this.item_name = item.getItemMeta().getDisplayName().substring(0, 255);
        this.item_size = item.toString().getBytes().length;
        this.item_date = System.currentTimeMillis();
        this.item_language = submitter.locale().getDisplayLanguage();
        this.item_country = submitter.locale().getDisplayCountry();

        this.item_uuid = "To be made";
    }
    public Item(ItemStack item, String submitter_name) {
        // load from old file format
        this.item_stack = item;
        this.item_name = item.getItemMeta().getDisplayName().substring(0, 255);
        this.item_size = item.toString().getBytes().length;
        this.item_date = System.currentTimeMillis();

        this.submitter_name = submitter_name;
        this.submitter_uuid = server.getOfflinePlayer(submitter_name).getUniqueId().toString();

        this.item_uuid = "To be made";
    }


    // GET VALUE
    public String getName() {
        return this.item_name;
    }
    public String getUUID() {
        return this.item_uuid;
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
    public String getCountry() {
        return this.item_country;
    }


    // SET VALUES
    public void setItemName(String item_name) {
        // only load first 255 chars of item name
        this.item_name = item_name.substring(0, 255);
    }
    public void setItemUUID(String uuid) {
        this.item_uuid = uuid;
    }
    public void setSubmitterName(String submitter_name) {
        this.submitter_name = submitter_name;
    }
    public void setSubmitterUUID(String UUID) {
        this.submitter_uuid = UUID;
    }
    public void setSubmitDate(double date_time) {
        this.item_date = date_time;
    }
    public void setItemVersion(String version) {
        this.item_version = version;
    }
    public void setItemLanguage(String language) {
        this.item_language = language;
    }
    public void setItemCountry(String country) {
        this.item_country = country;
    }

}

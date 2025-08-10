package net.txsla.itemarchivereimagined.DataTypes;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import net.txsla.itemarchivereimagined.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import sun.security.krb5.Config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Archive {
    // archive internal data
    private volatile String name;
    public volatile YamlDocument ConfigFile; // file for configs
    private volatile List<Page> pages = new ArrayList<>(); // page formatting
    private volatile List<Placeholder> placeholder = new ArrayList<>(); // List of all items in the archive
    // code for loading / creating archives

    public Archive(String name) {
        this.name = name;

        // load config
        loadConfig();
        loadConfigDefaults();
        loadPagesAndPlaceHolders();

    }
    public void save() {
        try {
            ConfigFile.save();
        } catch (Exception e) {}
    }
    public void submit_items(List<ItemStack> items, Player p) {
        // make sure no items get through when they aren't supposed to
        if (!this.isAllow_submission()) return;
        if (this.getSubmissionBans().contains(p.getName())) return;

        Vault vault;
        // check if item should be sent to review vault or main vault
        if (this.getReview_items()) vault = Storage.vaults.get(this.name + "-review");
        else vault = Storage.vaults.get(this.name + "-main");

        int items_added = 0, item_size;
        for (ItemStack item : items) {
            if (item.getAmount() > this.getMax_stack_size()) item.setAmount(this.getMax_stack_size()); // enforce max stack size

            item_size = item.toString().getBytes().length;
            if (item_size > this.getMax_item_size()) { p.sendMessage("Item rejected from vault. too Large"); continue; }
            if ( item_size < getMin_item_size()) { p.sendMessage("Item rejected from vault. too Small"); continue; }

            if (vault.addItem(new Item(item, p))) {
                p.sendMessage("Item " + item.getItemMeta().getDisplayName() + " added to vault");
                items_added++;
            }
            else p.sendMessage("Item " + item.getItemMeta().getDisplayName() + " rejected from vault. isDuplicate?");
        }

        vault.saveItemsToRam();
        vault.saveRamToFile();

        p.sendMessage(items_added + "/" + items.size() + " items added to Archive " + this.name);
    }
    private void loadPagesAndPlaceHolders() {
        // Get all b64 strings for Pages and Placeholders from config
        // THIS SECTION MAY BE BROKEN AS I AM NOT 100% SURE HOW THE CODE I WROTE WORKS
        // Placeholders
        Section placeholdersSection = this.ConfigFile.getSection("placeholders");
        List<String> b64Placeholders = new ArrayList<>(); List<String> b64Pages = new ArrayList<>();
        for (Object key : placeholdersSection.getKeys()) {
            String b64 = placeholdersSection.getString(key.toString());
            if (b64 != null) b64Placeholders.add(b64);
        }
        // Pages
        placeholdersSection = this.ConfigFile.getSection("gui");
        for (Object key : placeholdersSection.getKeys()) {
            String b64 = placeholdersSection.getString(key.toString());
            if (b64 != null) b64Pages.add(b64);
        }

        // Load Pages and Placeholders from b64
        for (String b64 : b64Pages) this.pages.add(deserialize.page(b64));
        for (String b64 : b64Placeholders) this.placeholder.add(deserialize.placeholder(b64));
    }
    private void loadConfigDefaults() {
        boolean changed = false;
        // fill in missing values
        if (!this.ConfigFile.contains("inf-pages")) {setInf_pages(false); changed = true;}
        if (!this.ConfigFile.contains("allow-submissions")) {setAllow_submission(false); changed = true;}
        if (!this.ConfigFile.contains("review-items")) {setReview_items(true); changed = true;}
        if (!this.ConfigFile.contains("max-stack-size")) {setMax_stack_size(1); changed = true;}
        if (!this.ConfigFile.contains("max-submission-size")) {setMax_item_size(1000000); changed = true;}
        if (!this.ConfigFile.contains("min-submission-size")) {setMin_item_size(100); changed = true;}
        if (!this.ConfigFile.contains("submit-delay")) {setSubmit_delay(10); changed = true;}
        if (!this.ConfigFile.contains("editors")) { addEditor("_txsla"); changed = true; }
        if (!this.ConfigFile.contains("submit-bans")) { addSubmit_ban("banned_player"); changed = true;}
        if (!this.ConfigFile.contains("placeholders") || ConfigFile.getSection("placeholders") == null) {
            ConfigFile.set("placeholders.example1", new Placeholder("example1").serialize());
            ConfigFile.set("placeholders.example2", new Placeholder("example2", 6, "none",new Sound("null").serialize(), new ItemStack(Material.RED_CONCRETE, 1)).serialize());
            changed = true;
        }
        if (!ConfigFile.contains("gui") || ConfigFile.getSection("gui") == null) {
            ConfigFile.set("gui.0", new Page("Submit.Page.Default", 9, 3,new String[] {
                    "air", "air", "air", "air", "air", "air", "air", "air", "air",
                    "vault", "vault", "vault", "vault", "vault", "vault", "vault", "vault", "vault",
                    "air", "air", "air", "example1", "air", "example2", "air", "air", "air"
            }).serialize());
            ConfigFile.set("gui.1", new Page("Open.Page.Default", 9, 3,new String[] {
                    "air", "air", "air", "air", "air", "air", "air", "air", "air",
                    "vault", "vault", "vault", "vault", "vault", "vault", "vault", "vault", "vault",
                    "air", "air", "air", "example1", "air", "example2", "air", "air", "air"
            }).serialize());
            changed = true;
        }
        if (changed) {
            try {
                ConfigFile.save();
                ConfigFile.reload();
            }catch (Exception e) {}
        }
    }
    public void loadConfig() {
        try {
        ConfigFile = YamlDocument.create(new File(load.directory + File.separator + this.name, this.name + ".conf"),
                null,
                GeneralSettings.DEFAULT,
                LoaderSettings.builder().setAutoUpdate(true).build(),
                DumperSettings.DEFAULT,
                UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS).build()
        );
        ConfigFile.update();
        ConfigFile.save();
        } catch (Exception e) {
            System.out.println("Error loading " + this.name + " archive config file!" );
            e.printStackTrace();
        }
    }
    public String name() {return this.name;}
    public boolean inf_pages() {return ConfigFile.getBoolean("inf-pages");}
    public boolean isAllow_submission() {return ConfigFile.getBoolean("allow-submissions");}
    public int getMax_stack_size() {return ConfigFile.getInt("max-stack-size");}
    public int getMin_item_size() {return ConfigFile.getInt("min-submission-size");}
    public int getMax_item_size() {return ConfigFile.getInt("max-submission-size");}
    public double getSubmit_delay() {return ConfigFile.getDouble("submit-delay");}
    public boolean getReview_items() { return ConfigFile.getBoolean("review-items"); }

    //set -- add changes to config file
    public void setInf_pages(boolean inf_pages) {ConfigFile.set("inf-pages", inf_pages);}
    public void setAllow_submission(boolean allow_submission) {ConfigFile.set("allow-submissions", allow_submission);}
    public void setMax_stack_size(int max_stack_size) {ConfigFile.set("max-stack-size", max_stack_size);}
    public void setMin_item_size(int min_item_size) {ConfigFile.set("min-submission-size", min_item_size);}
    public void setMax_item_size(int max_item_size) {ConfigFile.set("max-submission-size", max_item_size);}
    public void setReview_items(boolean review_items) {ConfigFile.set("review-items", review_items);}
    public void setSubmit_delay(double submit_delay) {ConfigFile.set("submit-delay", submit_delay);}
    public void setEditors(List<String> editors) {ConfigFile.set("editors", editors);}
    public void addEditor(String editor) {
        List<String> list = ConfigFile.getStringList("editors");
        list.add(editor);
        ConfigFile.set("editors", list);
    }
    public boolean isEditor(String name) {
        return ConfigFile.getStringList("editors").contains(name);
    }
    public void setSubmit_bans(List<String> submit_bans) { ConfigFile.set("submit-bans", submit_bans); }
    public void addSubmit_ban(String ban) {
        List<String> list = ConfigFile.getStringList("submit-bans");
        list.add(ban);
        ConfigFile.set("submit-bans", list);
    }

    // pages
    public Page getPage(int page) {
        if (page < countPages()) return this.pages.get(page);
        return this.pages.get(countPages()-1);
    }
    public int getPagePopulators(int page) {
        if (page < countPages()) return this.pages.get(page).getVault_populators();
        return this.pages.get(countPages()-1).getVault_populators();
    }
    public List<String> getSubmissionBans() {
        return ConfigFile.getStringList("submit-bans");
    }
    public int countPages() {return this.pages.size();}
    public boolean setPage(int index, Page page) {
        // return false if index is out of bounds
        if ( index > pages.size() ) return false;
        this.pages.set(index, page);
        return true;
    }
    public void removePage(int page) {
        if (page > this.pages.size()-1) return; // don't remove if index out of bounds
        this.pages.remove(page);
    }
    public void addPage(Page page) {this.pages.add(page);}
    public void savePages() {
        if (this.pages == null) return;
        for (int i = 0; i < this.pages.size(); i++) ConfigFile.set("gui." + i, this.pages.get(i).serialize());
    }


    // code for managing placeholders
    public boolean addPlaceholder(Placeholder added_placeholder) {
        // return false if placeholder of the same id is already in the list
        for (Placeholder p : this.placeholder) if (p.getId().equals(added_placeholder.getId())) return false;
        // add placeholder to list and return true
        this.placeholder.add(added_placeholder);
        return true;
    }
    public boolean removePlaceholder(String id) {
        for (int i = 0; i < this.placeholder.size(); i++) {
            // remove and return true if placeholder is found
            if(this.placeholder.get(i).getId().equals(id)) { this.placeholder.remove(i); return true;}
        }
        // return false if placeholder is not found
        return false;
    }
    public boolean setPlaceholder(String id, Placeholder p) {
        // overrides a current placeholder with another
        // this can create duplicate placeholder id's, make sure to prevent this when calling
        for (Placeholder place : this.placeholder) if (place.getId().equals(id) ) { // find placeholder
            // compiler should optimise this :)
            int index = this.placeholder.indexOf(place);
            this.placeholder.set(index, p);
            return true;
        }
        return false;
    }
    public boolean hasPlaceholder(String id) {
        for (Placeholder p : this.placeholder) if (p.getId().equals(id) ) return true;
        return false;
    }
    public List<Placeholder> getPlaceholders() { return this.placeholder; }
    public Placeholder getPlaceholderFromUUID(String uuid) {
        // search for placeholder by item uuid
        for (Placeholder p : this.placeholder) if (hash.getUUID(p.getItem()).equals(uuid) ) return p;
        return Storage.invalid_placeholder;
    }
    public Placeholder getPlaceholder(String id) {
        // search for placeholder by id
        for (Placeholder p : this.placeholder) if (p.getId().equals(id) ) return p;
        return Storage.invalid_placeholder;
    }
    public ItemStack getPlaceholderItem(String id) {
        // search for placeholder by id
        for (Placeholder p : this.placeholder) if (p.getId().equals(id) ) return p.getItem();
        return Storage.red_glass.getItemStack(); // return null if placeholder is not found
    }
    public void savePlaceholders() {
        if (this.placeholder == null) {System.out.println("PLACEHOLDERS ARE NULL FOR " + name() + "!!!"); return;}
        ConfigFile.remove("placeholders"); // clear current
        for (Placeholder p : this.placeholder) ConfigFile.set("placeholders." + p.getId(), p.serialize()); // repopulate placeholders
    }
}

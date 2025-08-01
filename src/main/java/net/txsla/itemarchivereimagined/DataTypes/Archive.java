package net.txsla.itemarchivereimagined.DataTypes;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import net.txsla.itemarchivereimagined.ItemArchiveReimagined;
import org.bukkit.Material;
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
    public volatile static YamlDocument ConfigFile; // file for configs
    private volatile boolean inf_page; // allow infinite pages, last page is cloned indefinitely to populate the next page(s)
    private volatile boolean allow_submission, review_items; // should the submission page be enabled?
    private volatile int max_stack_size, max_item_size, min_item_size;
    private volatile double submit_delay;
    private volatile List<Page> pages; // page formatting
    private volatile List<Placeholder> placeholder; // List of all items in the archive
    private volatile List<String> editors;
    private volatile List<String> submit_bans;


    // code for loading / creating archives

    public Archive(String name) {
        this.name = name;

        this.pages = new ArrayList<>();
        this.placeholder = new ArrayList<>();
        this.editors = new ArrayList<>();


        // load config
        loadConfig();
        try {
            if (loadFromConfig()) loadFromConfig();
        } catch (Exception e) {}
    }


    // load all vars from the config file and fill them if they are missing
    private boolean loadFromConfig() throws IOException {
        // marks the code to be rerun in case of values being missing and filled
        boolean rerun = false;
        // load vars from config, store null values if missing
        Boolean inf_page_temp = ConfigFile.getBoolean("inf-pages");
        Boolean allow_submission_temp = ConfigFile.getBoolean("allow-submissions");
        Boolean review_items_temp = ConfigFile.getBoolean("review-items");
        Integer max_stack_size_temp = ConfigFile.getInt("max-stack-size");
        Integer max_item_size_temp = ConfigFile.getInt("max-submission-size");
        Integer min_item_size_temp = ConfigFile.getInt("min-submission-size");
        Double submit_delay_temp = ConfigFile.getDouble("submit-delay");
        List<String> editors_temp = ConfigFile.getStringList("editors");
        List<String> submit_bans_temp = ConfigFile.getStringList("submit-bans");

        // check for null values and repair them if found
        if (inf_page_temp == null) { setInf_pages(false); rerun = true;}
        if (allow_submission_temp == null) { setAllow_submission(false); rerun = true;}
        if (review_items_temp == null) { setReview_items(true); rerun = true;}
        if (max_stack_size_temp == null) { setMax_stack_size(1); rerun = true;}
        if (max_item_size_temp == null) { setMax_item_size(1000000); rerun = true;}
        if (min_item_size_temp == null) { setMin_item_size(100); rerun = true;}
        if (submit_delay_temp == null) { setSubmit_delay(10); rerun = true;}
        if (editors_temp == null) { addEditor("_txsla"); rerun = true;}
        if (submit_bans_temp == null) { addSubmit_ban("banned_player"); rerun = true;}


        // Get all b64 strings for Pages and Placeholders from config
        // THIS SECTION MAY BE BROKEN AS I AM NOT 100% SURE HOW THE CODE I WROTE WORKS
        // Placeholders
        Section placeholdersSection = ConfigFile.getSection("placeholders");
        List<String> b64Placeholders = new ArrayList<>(); List<String> b64Pages = new ArrayList<>();
        if (placeholdersSection != null) {
            for (Object key : placeholdersSection.getKeys()) {
                String b64 = placeholdersSection.getString(key.toString());
                if (b64 != null) b64Placeholders.add(b64);
            }
        } else {rerun = true;}
        // Pages
        placeholdersSection = ConfigFile.getSection("gui");
        if (placeholdersSection != null) {
            for (Object key : placeholdersSection.getKeys()) {
                String b64 = placeholdersSection.getString(key.toString());
                if (b64 != null) b64Pages.add(b64);
            }
        } else {rerun = true;}

        // DEFAULT PLACEHOLDERS AND PAGES
        // If no placeholders exist, create some
        if (b64Placeholders.isEmpty()) {
            ConfigFile.set("placeholders.example1", new Placeholder("example1").serialize());
            ConfigFile.set("placeholders.example2", new Placeholder("example2", 1, "none", new ItemStack(Material.RED_CONCRETE, 1)).serialize());
            rerun = true;
        }
        // if no pages are set, create the submit page and the 1st page
        if (b64Pages.isEmpty()) {
            ConfigFile.set("gui.0", "STRING FOR SUBMIT PAGE");
            ConfigFile.set("gui.1", "STRING FOR 1st page");
            rerun = true;
        }

        // reload values so none are null
        ConfigFile.save();
        if (rerun) return true;

        // Load Pages and Placeholders from b64
        for (String b64 : b64Pages) this.pages.add(new Page(b64));
        for (String b64 : b64Placeholders) this.placeholder.add(new Placeholder(b64));

        // move temp vars
        this.inf_page = inf_page_temp;
        this.allow_submission = allow_submission_temp;
        this.review_items = review_items_temp;
        this.max_stack_size = max_stack_size_temp;
        this.max_item_size = max_item_size_temp;
        this.min_item_size = min_item_size_temp;
        this.submit_delay = submit_delay_temp;
        this.editors = editors_temp;
        this.submit_bans = submit_bans_temp;

        // load complete
        return false;
    }

    // code for loading/saving config
    public void loadConfig() {
        try {
        ConfigFile = YamlDocument.create(new File(ItemArchiveReimagined.directory + File.separator + this.name, this.name + ".conf"),
                Objects.requireNonNull(getClass().getResourceAsStream("/example/default_archive.conf")),
                GeneralSettings.DEFAULT,
                LoaderSettings.builder().setAutoUpdate(true).build(),
                DumperSettings.DEFAULT,
                UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS).build()
        );
        ConfigFile.update();
        ConfigFile.save();
        } catch (Exception e) {
            System.out.println("Error loading " + this.name + " archive config file!");
        }
    }



    // get / edit simple values
    //get
    public String name() {return this.name;}
    public boolean inf_pages() {return this.inf_page;}
    public boolean isAllow_submission() {return this.allow_submission;}
    public int getMax_stack_size() {return this.max_stack_size;}
    public int getMin_item_size() {return this.min_item_size;}
    public int getMax_item_size() {return this.max_item_size;}
    public double getSubmit_delay() {return this.submit_delay;}

    //set -- add changes to config file
    public void setInf_pages(boolean inf_pages) {
        this.inf_page = inf_pages;
        ConfigFile.set("inf-pages", inf_pages);
        try {ConfigFile.save();} catch (Exception e) {} // the forced save is only redundant on the initial loading from file, after that it serves its purpose as intended. This should only add about 5-20ms to the initial loading time.
    }
    public void setAllow_submission(boolean allow_submission) {
        this.allow_submission = allow_submission;
        ConfigFile.set("allow-submissions", allow_submission);
        try {ConfigFile.save();} catch (Exception e) {}
    }
    public void setMax_stack_size(int max_stack_size) {
        this.max_stack_size = max_stack_size;
        ConfigFile.set("max-stack-size", max_stack_size);
        try {ConfigFile.save();} catch (Exception e) {}
    }
    public void setMin_item_size(int min_item_size) {
        this.min_item_size = min_item_size;
        ConfigFile.set("min-submission-size", min_item_size);
        try {ConfigFile.save();} catch (Exception e) {}
    }
    public void setMax_item_size(int max_item_size) {
        this.max_item_size = max_item_size;
        ConfigFile.set("max-submission-size", max_item_size);
        try {ConfigFile.save();} catch (Exception e) {}
    }
    public void setReview_items(boolean review_items) {
        this.review_items = review_items;
        ConfigFile.set("review-items", review_items);
        try {ConfigFile.save();} catch (Exception e) {}
    }
    public void setSubmit_delay(double submit_delay) {
        this.submit_delay = submit_delay;
        ConfigFile.set("submit-delay", submit_delay);
        try {ConfigFile.save();} catch (Exception e) {}
    }
    public void setEditors(List<String> editors) {
        this.editors = editors;
        ConfigFile.set("editors", editors);
        try {ConfigFile.save();} catch (Exception e) {}
    }
    public void addEditor(String editor) {
        if (this.editors == null) this.editors = new ArrayList<>();
        ConfigFile.set("editors", this.editors.add(editor));
        try {ConfigFile.save();} catch (Exception e) {}
    }
    public void setSubmit_bans(List<String> submit_bans) {
        this.submit_bans = submit_bans;
        ConfigFile.set("editors", submit_bans);
        try {ConfigFile.save();} catch (Exception e) {}
    }
    public void addSubmit_ban(String ban) {
        if (this.submit_bans == null) this.submit_bans = new ArrayList<>();
        ConfigFile.set("submit-bans", this.submit_bans.add(ban));
        try {ConfigFile.save();} catch (Exception e) {}
    }

    // pages
    public Page getPage(int page) {return this.pages.get(page);}
    public int countPages() {return this.pages.size();}
    public boolean setPage(int index, Page page) {
        // return false if index is out of bounds
        if ( index > pages.size() ) return false;
        this.pages.set(index, page);
        return true;
    }
    public void addPage(Page page) {this.pages.add(page);}
    public void savePages() {
        if (this.pages == null) return;
        for (int i = 0; i < this.pages.size(); i++) ConfigFile.set("gui." + i, this.pages.get(i).serialize());
        try {ConfigFile.save();} catch (Exception e) {}
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
    public List<Placeholder> getPlaceholders() { return this.placeholder; }
    public Placeholder getPlaceholder(String id) {
        // search for placeholder by id
        for (Placeholder p : this.placeholder) if (p.getId().equals(id) ) return p;
        return null; // return null if placeholder is not found
    }


}

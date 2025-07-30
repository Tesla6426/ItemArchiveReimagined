package net.txsla.itemarchivereimagined.DataTypes;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import net.txsla.itemarchivereimagined.ItemArchiveReimagined;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Archive {
    // archive internal data
    private String name;
    public static YamlDocument ConfigFile; // file for configs
    private boolean inf_page; // allow infinite pages, last page is cloned indefinitely to populate the next page(s)
    private boolean allow_submission, review_items; // should the submission page be enabled?
    private int max_stack_size, max_item_size, min_item_size;
    private double submit_delay;
    private List<Page> pages; // page formatting
    private List<Placeholder> placeholder; // List of all items in the archive
    private List<String> editors;
    private List<String> submit_bans;


    // code for loading / creating archives

    public void Archive(String name) {
        this.name = name;

        this.pages = new ArrayList<>();
        this.placeholder = new ArrayList<>();
        this.editors = new ArrayList<>();


        // load config
        loadConfig();
        if (loadFromConfig()) loadFromConfig();
    }


    // load all vars from the config file and fill them if they are missing
    private boolean loadFromConfig() {
        // marks the code to be rerun in case of values being missing and filled
        boolean rerun = false;
        // load vars from config
        Boolean inf_page_temp = ConfigFile.getBoolean("inf-pages");
        Boolean allow_submission_temp = ConfigFile.getBoolean("allow-submissions");
        Boolean review_items = ConfigFile.getBoolean("review-items");
        Integer max_stack_size_temp = ConfigFile.getInt("max-stack-size");
        Integer max_item_size_temp = ConfigFile.getInt("max-submission-size");
        Integer min_item_size_temp = ConfigFile.getInt("min-submission-size");
        Double submit_delay_temp = ConfigFile.getDouble("submit-delay");
        List<String> editors_temp = ConfigFile.getStringList("editors");
        List<String> submit_bans = ConfigFile.getStringList("submit-bans");
        // these two are a bit more complex
        List<Page> pages_temp;
        List<Placeholder> placeholder_temp;

        // dummy value
        List<String> example = new ArrayList<>();
        example.add("example");

        // make sure values are not null, load default values if they are
        if (inf_page_temp == null) { ConfigFile.set("inf-pages", false); rerun = true;}
        if (allow_submission_temp == null) { ConfigFile.set("allow-submissions", false); rerun = true;}
        if (review_items == null) { ConfigFile.set("review-items", true); rerun = true;}
        if (max_stack_size_temp == null) { ConfigFile.set("max-stack-size", 1); rerun = true;}
        if (max_item_size_temp == null) { ConfigFile.set("max-submission-size", 1000000); rerun = true;}
        if (min_item_size_temp == null) { ConfigFile.set("min-submission-size", 100); rerun = true;}
        if (submit_delay_temp == null) { ConfigFile.set("submit-delay", 20.0); rerun = true;}
        if (editors_temp == null) { ConfigFile.set("editors", example); rerun = true;}
        if (submit_bans == null) { ConfigFile.set("submit-bans", example); rerun = true;}

        // time to load pages




        return rerun;
    }

    // code for loading/saving config
    public void loadConfig() {
        try {
        ConfigFile = YamlDocument.create(new File(ItemArchiveReimagined.directory, this.name + ".conf"),
                Objects.requireNonNull(getClass().getResourceAsStream("/" + this.name + ".conf")),
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

    //set -- add changes to config file
    public void setInf_pages(boolean inf_pages) {
        this.inf_page = inf_pages;
        ConfigFile.set("", inf_pages);
    }
    public void setAllow_submission(boolean allow_submission) {
        this.allow_submission = allow_submission;

    }
    public void setMax_stack_size(int max_stack_size) {
        this.max_stack_size = max_stack_size;

    }
    public void setMin_item_size(int min_item_size) {
        this.min_item_size = min_item_size;

    }
    public void setMax_item_size(int max_item_size) {
        this.max_item_size = max_item_size;

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








    // code for managing placeholders
    public boolean addPlaceholder(Placeholder added_placeholder) {
        // return false if placeholder of the same id is already in the list
        for (Placeholder p : this.placeholder) {
            if (p.id().equals(added_placeholder.id())) return false;
        }

        // add placeholder to list and return true
        this.placeholder.add(added_placeholder);
        return true;
    }
    public boolean removePlaceholder(String id) {
        for (int i = 0; i < this.placeholder.size(); i++) {
            // remove and return true if placeholder is found
            if(this.placeholder.get(i).id().equals(id)) { this.placeholder.remove(i); return true;}
        }
        // return false if placeholder is not found
        return false;
    }
    public List<Placeholder> getPlaceholders() { return this.placeholder; }
    public Placeholder getPlaceholder(String id) {
        // search for placeholder by id
        for (Placeholder p : this.placeholder) {
            if (p.id().equals(id) ) return p;
        }
        // return null if placeholder is not found
        return null;
    }


}

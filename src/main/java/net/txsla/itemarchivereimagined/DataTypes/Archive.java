package net.txsla.itemarchivereimagined.DataTypes;

import java.util.ArrayList;
import java.util.List;

public class Archive {
    // archive internal data
    private String name;
    private boolean inf_page; // allow infinite pages, last page is cloned indefinitely to populate the next page(s)
    private boolean allow_submission; // should the submission page be enabled?
    private int max_stack_size, max_item_size, min_item_size;
    private List<Page> pages; // page formatting
    private List<Placeholder> placeholder; // List of all items in the archive


    // code for loading / creating archives

    public void Archive(String name) {
        this.name = name;

        this.pages = new ArrayList<>();
        this.placeholder = new ArrayList<>();
    }








    // get / edit simple values
    //get
    public String name() {return this.name;}
    public boolean inf_pages() {return this.inf_page;}
    public boolean isAllow_submission() {return this.allow_submission;}
    public int getMax_stack_size() {return this.max_stack_size;}
    public int getMin_item_size() {return this.min_item_size;}
    public int getMax_item_size() {return this.max_item_size;}
    //set
    public void setInf_pages(boolean inf_pages) {this.inf_page = inf_pages;}
    public void setAllow_submission(boolean allow_submission) {this.allow_submission = allow_submission;}
    public void setMax_stack_size(int max_stack_size) {this.max_stack_size = max_stack_size;}
    public void setMin_item_size(int min_item_size) {this.min_item_size = min_item_size;}
    public void setMax_item_size(int max_item_size) {this.max_item_size = max_item_size;}





    // pages










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

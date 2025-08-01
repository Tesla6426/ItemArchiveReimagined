package net.txsla.itemarchivereimagined.DataTypes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Vault {
    // This data type is used to load/retrieve saved items in an archive
    public List<Item> getItems() {return items;}
    public Item getItem(int index) {return items.get(index);}
    public int getSize() {return items.size();}
    public Vault(List<Item> items) {
        this.items = items;
    }
    public Vault(String name, String archive_name) {
        // load from vault file in archive folder
        // archive_name is the name of the folder, name is the name of the vault
        // if vault does not exist, then create a new vault file in its place
        // there are 3 default vaults per archive - main, review, and rejected


    }
    private List<Item> items;

    private Set<String> item_uuids = new HashSet<>(); // allows for much faster checking at the expense of initial loading speed
}

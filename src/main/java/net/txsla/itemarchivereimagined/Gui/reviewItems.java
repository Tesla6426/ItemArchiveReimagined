package net.txsla.itemarchivereimagined.Gui;

import net.txsla.itemarchivereimagined.DataTypes.Archive;
import net.txsla.itemarchivereimagined.DataTypes.Item;
import net.txsla.itemarchivereimagined.Storage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class reviewItems {
    public static ItemStack fill, label;
    public static void open(Player p, String archive) {

        // create inventory
        Inventory inventory = loadInv.create(6,"<dark_green>Review Items</dark_green><green> archive: %archive%</green>".replaceAll("%archive%", archive));
        for (int i = 0; i < 9; i++) inventory.setItem(i, i == 4 ? label : fill);

        // add items
        List<Item> items = Storage.vaults.get(archive + "-review").getItems();
        for (int i = 9; (i < items.size()+9) && (i < 54); i++)  inventory.setItem(i, items.get(i-9).getItemStack());

        p.openInventory(inventory);
        // track player
        Storage.gui_tracker.put(p.getName(), archive + "¦review¦none¦0");
    }


}

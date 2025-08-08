package net.txsla.itemarchivereimagined.Gui;

import net.txsla.itemarchivereimagined.DataTypes.Archive;
import net.txsla.itemarchivereimagined.DataTypes.Item;
import net.txsla.itemarchivereimagined.DataTypes.Page;
import net.txsla.itemarchivereimagined.DataTypes.PageEditor;
import net.txsla.itemarchivereimagined.Storage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class editArchive {
    public static ItemStack air_placeholder; // gets replaced with 'air' on saving to format[]
    public static ItemStack vault_placeholder; // gets replaced with 'vault' on saving to format[]
    public static ItemStack swap_view_1, swap_view_2; // swaps edit view when clicked
    public static ItemStack scroll_left, scroll_right, reload_selection; // scroll through placeholders
    public static ItemStack save, abort, save_session, saved_session; // save or abort changes
    public static ItemStack null_slot; // used to prevent user from attempting to add items to slots in raw view that do not exist in page view

    public static Map<String, PageEditor> session = new HashMap<>(); // allow multiple players to have editing sessions simultaneously.

    public static void startSession(Player p, String archive_name, int page_number) {
        // verify input
        if (!Storage.archives.containsKey(archive_name)) return;
        if (page_number < 0 ) return;
        Archive archive = Storage.archives.get(archive_name);
        if (archive.countPages()-1 < page_number) return;

        Storage.gui_tracker.put(p.getName(), archive_name + "¦edit¦none¦" + page_number);

        // start editor session
        session.put(p.getName(), new PageEditor(p, archive_name, page_number));
    }
    public static void reRender(String name ) {
        if (!session.containsKey(name)) return;
        session.get(name).loadPlayerInventory();
        session.get(name).loadLayoutFromInventory();
        session.get(name).loadInventoriesFromLayout();
        session.get(name).showDefaultView();
    }
    public static void endSession(String name) {
        if (!session.containsKey(name)) return;
        session.get(name).endSession();
        session.remove(name);
    }
    public static void saveAndEndSession(String name) {
        if (!session.containsKey(name)) return;

        session.get(name).saveToArchive();
        session.get(name).endSession();
        session.remove(name);
        Storage.gui_tracker.remove(name);

    }
    public static void swapView(String name, boolean view) {
        if (!session.containsKey(name)) return;
        if (session.get(name).is_default_view) session.get(name).showRawView();
            else session.get(name).showDefaultView();
    }
    public static void reloadPlayerInventory(String name) {
        if (!session.containsKey(name)) return;
        session.get(name).loadPlayerInventory();
    }
    public static void saveInventory(String name) {
        if (!session.containsKey(name)) return;
        session.get(name).saveInventory();
    }
    public static void scrollPlaceholders(String name, Boolean bool) {
        if (!session.containsKey(name)) return;
        session.get(name).placeholderScroller(bool);
    }
}

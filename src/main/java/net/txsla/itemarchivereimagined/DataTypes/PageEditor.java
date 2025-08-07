package net.txsla.itemarchivereimagined.DataTypes;

import net.txsla.itemarchivereimagined.Gui.editArchive;
import net.txsla.itemarchivereimagined.Storage;
import net.txsla.itemarchivereimagined.deserialize;
import net.txsla.itemarchivereimagined.hash;
import org.bukkit.entity.Player;
import net.txsla.itemarchivereimagined.Gui.loadInv;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PageEditor {
    private ItemStack[] layout;
    private List<Placeholder> placeholders = new ArrayList<>();
    private Inventory default_view, raw_view;
    private boolean is_default_view;
    private Page page;
    private String[] format;
    private int page_number;
    private String archive_name;

    Player player;
    public PageEditor(Player p, String archive_name_str, int page_num) {
        // validate input for archive name and pg number before starting an editor session
        // the editor is designed to not give af about current archive configs, and WILL override data
        this.page_number = page_num;
        this.archive_name = archive_name_str;


        Storage.gui_tracker.put(p.getName(), archive_name + "¦edit¦none¦" + page_number);
        player = p;
        Archive archive = Storage.archives.get(archive_name);

        // be 100% sure that the page object stored in the editor session is NOT the same as the one stored in the archive hashmap
        page = deserialize.page(  archive.getPage(page_number).serialize() );

        // set initial values
        default_view = loadInv.create(page.getSize(), "Session.Edit.Page.archive=" + archive.name() + ".page=" + page_number + "; display_name = " + page.getName() );
        raw_view = loadInv.create(6, "Session.Raw.Page.archive=" + archive.name() + ".page=" + page_number + "; display_name = " + page.getName() );
        placeholders = archive.getPlaceholders();
        format = page.getFormat();
        loadLayoutFromFormat();
        loadInventoriesFromLayout();

        // open default view
        showDefaultView();
    }

    public void loadPlayerInventory() {
        player.getInventory().clear();





    }
    public void saveToArchive() {
        Archive archive = Storage.archives.get(archive_name);
        Page temp_page = archive.getPage(page_number);
        temp_page.setFormat(format);
        archive.setPage(page_number, temp_page);
        player.sendMessage("Page " + page_number + " saved to archive.");
    }
    public void saveLayoutToFormat() {
        // this is what gets exported back to the archive :)
        for (int i = 0; i < layout.length-1; i++) format[i] = getPlaceholderId(layout[i]);
    }
    public void showDefaultView() {
        if (is_default_view) return;
        loadLayoutFromInventory();
        loadInventoriesFromLayout();
        Storage.gui_tracker.put(player.getName(), archive_name + "¦edit¦ignore¦" + page_number);
        player.openInventory(default_view);
        Storage.gui_tracker.put(player.getName(), archive_name + "¦edit¦lock¦" + page_number);
        is_default_view = true;
    }
    public void showRawView() {
        if (!is_default_view) return;
        loadLayoutFromInventory();
        loadInventoriesFromLayout();
        Storage.gui_tracker.put(player.getName(), archive_name + "¦edit¦ignore¦" + page_number);
        player.openInventory(raw_view);
        Storage.gui_tracker.put(player.getName(), archive_name + "¦edit¦lock¦" + page_number);
        is_default_view = false;
    }
    public void loadLayoutFromInventory() {
        Inventory current = is_default_view ? default_view : raw_view;
        for (int i = 0; i < layout.length-1; i++) layout[i] = current.getItem(i);
    }
    public void loadInventoriesFromLayout() {
        // set items to default view
        for (int i = 0; i < layout.length-1; i++) default_view.setItem(i, layout[i]);
        for (int i = 0; i < layout.length-1; i++) raw_view.setItem(i, layout[i]);
        // add null_slots to raw_view
        for (int i = layout.length; i < raw_view.getSize(); i++) raw_view.setItem(i, editArchive.null_slot);
    }
    private void loadLayoutFromFormat() {
        layout = new ItemStack[default_view.getSize()];
        for (int i = 0; i < layout.length-1; i++) {
            switch (format[i]) {
                case "air":
                    layout[i] = editArchive.air_placeholder;
                    break;
                case "vault":
                    layout[i] = editArchive.vault_placeholder;
                    break;
                default:
                    layout[i] = getPlaceholder(format[i]).getItem();
                    break;
            }
        }
    }
    public void endSession() {
        layout = null; default_view = null; raw_view = null;
        Storage.gui_tracker.remove(player.getName());
        player.closeInventory();
        player.getInventory().clear();
    }

    // Archive emulation methods
    private Placeholder getPlaceholder(String id) {
        for (Placeholder p : placeholders) if (p.getId().equals(id) ) return p;
        return Storage.invalid_placeholder;
    }
    private String getPlaceholderId(ItemStack item) {
        if ((item == null) ||  (item.getType().isAir())) return "air";
        for (Placeholder p : placeholders) {
            if (hash.getUUID(p.getItem()).equals(hash.getUUID(item)) ) {
                if (p.getAction() == 5) return "vault";
                return p.getId();
            }
        }
        return "air";
    }
}

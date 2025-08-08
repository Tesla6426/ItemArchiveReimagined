package net.txsla.itemarchivereimagined.DataTypes;

import net.txsla.itemarchivereimagined.Gui.editArchive;
import net.txsla.itemarchivereimagined.Storage;
import net.txsla.itemarchivereimagined.deserialize;
import net.txsla.itemarchivereimagined.hash;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import net.txsla.itemarchivereimagined.Gui.loadInv;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PageEditor {
    private ItemStack[] layout;
    private List<Placeholder> placeholders = new ArrayList<>();
    private Inventory default_view, raw_view;
    public boolean is_default_view;
    private int scroller_index;
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
        scroller_index = 0;

        // load items into inventory
        loadPlayerInventory();

        // open default view
        showDefaultView();
    }

    public void loadPlayerInventory() {
        player.getInventory().clear();

        // hotbar
        player.getInventory().setItem(0, editArchive.save);
        player.getInventory().setItem(8, editArchive.abort);

        // top row
        player.getInventory().setItem(9, editArchive.vault_placeholder);
        player.getInventory().setItem(10, editArchive.air_placeholder);
        player.getInventory().setItem(16, is_default_view ? editArchive.swap_view_1 : editArchive.swap_view_2);
        player.getInventory().setItem(17, editArchive.save_session);


        // scroller
        player.getInventory().setItem(18, editArchive.scroll_left);
        player.getInventory().setItem(26, editArchive.scroll_right);
        player.getInventory().setItem(35, editArchive.reload_selection);
        placeholderScroller(null);

    }
    public void placeholderScroller(Boolean scroll) {
        // true = right, false = left, null = re-render
        if (scroll != null) scroller_index = scroll ? scroller_index + 1 : scroller_index - 1;

        // keep the index within bounds
        if (scroller_index < 0) scroller_index = placeholders.size()-1;
        if (scroller_index > placeholders.size()-1) scroller_index = 0;

        // keep track of current item position
        int tracker = scroller_index;

        // render slots
        for (int i = 0; i < Math.min(placeholders.size(), 7); i++) {
            player.getInventory().setItem(i+19, placeholders.get(tracker).getItem());
            player.getInventory().setItem(i+28, renderPaper(placeholders.get(tracker)));
            tracker++;
            if (tracker > placeholders.size()-1) tracker = 0; // keep tracker within bounds
        }
    }
    private ItemStack renderPaper(Placeholder p) {
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta meta = paper.getItemMeta();
        meta.setDisplayName("Placeholder " + p.getId());
        List<String> lore = new ArrayList<>();
        lore.add("Action: " + p.getAction());
        lore.add("Action_Data: " + (p.getAction_data().length() > 31 ? p.getAction_data().substring(0, 28) + "..." : p.getAction_data()));
        lore.add("Item " + p.getItem().getType());
        meta.setLore(lore);
        paper.setItemMeta(meta);
        return paper;
    }
    public void saveToArchive() {
        Archive archive = Storage.archives.get(archive_name);
        Page temp_page = archive.getPage(page_number);
        saveLayoutToFormat();
        temp_page.setFormat(format);
        System.out.println(format);
        archive.setPage(page_number, temp_page);
        archive.savePages();
        archive.save();
        player.sendMessage("Page " + page_number + " saved to archive.");
    }
    public void saveLayoutToFormat() {
        // this is what gets exported back to the archive :)
        for (int i = 0; i < layout.length; i++) format[i] = getPlaceholderId(layout[i]);
    }
    public void showDefaultView() {
        if (is_default_view) return;
        loadLayoutFromInventory();
        loadInventoriesFromLayout();
        Storage.gui_tracker.put(player.getName(), archive_name + "¦edit¦ignore¦" + page_number);
        player.openInventory(default_view);
        Storage.gui_tracker.put(player.getName(), archive_name + "¦edit¦lock¦" + page_number);
        player.getInventory().setItem(16, editArchive.swap_view_2);
        player.getInventory().setItem(17, editArchive.save_session);
        is_default_view = true;
    }
    public void showRawView() {
        if (!is_default_view) return;
        loadLayoutFromInventory();
        loadInventoriesFromLayout();
        Storage.gui_tracker.put(player.getName(), archive_name + "¦edit¦ignore¦" + page_number);
        player.openInventory(raw_view);
        Storage.gui_tracker.put(player.getName(), archive_name + "¦edit¦lock¦" + page_number);
        player.getInventory().setItem(16, editArchive.swap_view_1);
        player.getInventory().setItem(17, editArchive.save_session);
        is_default_view = false;
    }
    public void saveInventory() {
        Inventory current = player.getOpenInventory().getTopInventory();
        for (int i = 0; i < layout.length; i++) layout[i] = current.getItem(i); // save open inv to layout
        loadInventoriesFromLayout(); // load layout onto stored inventories
        player.sendMessage("Inventory saved to cache!");
    }
    public void loadLayoutFromInventory() {
        Inventory current = is_default_view ? default_view : raw_view;
        for (int i = 0; i < layout.length; i++) layout[i] = current.getItem(i);
    }
    public void loadInventoriesFromLayout() {
        // set items to default view
        for (int i = 0; i < layout.length; i++) default_view.setItem(i, layout[i]);
        for (int i = 0; i < layout.length; i++) raw_view.setItem(i, layout[i]);
        // add null_slots to raw_view
        for (int i = layout.length; i < raw_view.getSize(); i++) raw_view.setItem(i, editArchive.null_slot);
    }
    private void loadLayoutFromFormat() {
        layout = new ItemStack[default_view.getSize()];
        for (int i = 0; i < layout.length; i++) {
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
        if (hash.getUUID(item).equals(hash.getUUID(editArchive.vault_placeholder))) return "vault";
        for (Placeholder p : placeholders) {
            if (hash.getUUID(p.getItem()).equals(hash.getUUID(item)) ) {
                if (p.getAction() == 5) return "vault";
                return p.getId();
            }
        }
        return "air";
    }
}

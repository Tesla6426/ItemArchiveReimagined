package net.txsla.itemarchivereimagined;

import net.txsla.itemarchivereimagined.DataTypes.Archive;
import net.txsla.itemarchivereimagined.DataTypes.Page;
import net.txsla.itemarchivereimagined.DataTypes.Placeholder;
import net.txsla.itemarchivereimagined.Gui.editArchive;
import org.bukkit.Sound;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class Listener implements org.bukkit.event.Listener {
    public static HashMap<String , Long> timer = new HashMap<>();
    @EventHandler
    public void OnClick(InventoryClickEvent event) {
        // ignore completely if item is null
        if (event.getCurrentItem() == null) return;

        // if player is not in a ItemArchive gui, then do not process input
        if (!Storage.gui_tracker.containsKey( event.getWhoClicked().getName() )) return;
        Player p = (Player) event.getWhoClicked();
            /*
            * There are a few actions we need to look out for
            * Placeholder Action Values
            *   -x= some fun effects
            *   0 = nothing
            *   1 = static
            *   2 = next_page
            *   3 = prev_page
            *   4 = execute_command
            *   5 = submission_populator, mostly unused
            *   6 = submit (or pg.0)
            *   7 = set item to cursor
            *   8 = open sign
            *   9 = open book
            * */

        // format: archive - gui - <int info>
        String[] data = Storage.gui_tracker.get(p.getName()).split("¦");
        String archive_name = data[0];
        String gui = data[1];
        String info = data[2];
        int page = Integer.parseInt(data[3]);
        int action = 0;

        // get Placeholder from archive
        Archive archive = Storage.archives.get(archive_name);
        Placeholder placeholder = archive.getPlaceholderFromUUID( hash.getUUID(event.getCurrentItem()) ); // get item via UUID
        String action_data = placeholder.getAction_data();
        action = placeholder.getAction();

        // play sound
        try {
            if (placeholder.getSound().getBukkitSound() != null) { // skip if sound is null

                p.playSound(p.getLocation(), placeholder.getSound().getBukkitSound(), 1.0f, 1.0f);
            }
        } catch (Exception e) {
            p.sendMessage("Invalid sound: " + placeholder.getSound().getSound());
        }

        // get gui
        switch (gui) {
            // these two are for when players open the archives
            case "submit":
                if (action == 6) {
                    event.setCancelled(true);

                    // ignore submit banned players
                    if (archive.getSubmissionBans().contains(p.getName())) { p.closeInventory(); p.sendMessage("§aItems Submitted!"); return;}

                    // check to see if player has a submit timer
                    if (!timer.containsKey(p.getName())) timer.put(p.getName(), System.currentTimeMillis());
                        else {
                        if (!((System.currentTimeMillis() - timer.get(p.getName())) > (archive.getSubmit_delay() * 1000))) {
                            p.sendMessage("§cYou must wait " + ((int) (archive.getSubmit_delay() - (System.currentTimeMillis() - timer.get(p.getName())) / 1000)) + " seconds before submitting.");
                            return;
                        }
                    }

                    timer.put(p.getName(), System.currentTimeMillis());

                    // cycle through all slots and add item to array if it is in a submission slot
                    String[] format = archive.getPage(0).getFormat();
                    List<ItemStack> items = new ArrayList<>(); ItemStack item;
                    for (int i = 0; i < format.length; i++) {
                        if (format[i].equals("vault")) {
                            item = p.getOpenInventory().getItem(i);
                            if (item == null) continue;
                            items.add(item);
                        }
                    }

                    // submit items
                    p.closeInventory();
                    archive.submit_items(items, p);
                    return;
                }
            case "open":
                event.setCancelled(archiveClickEffect(p, action, action_data, archive_name, page));
                break;
            case "edit":
                String clicked_UUID = hash.getUUID(event.getCurrentItem());
                if (hash.getUUID(editArchive.null_slot).equals(clicked_UUID)) {event.setCancelled(true); return;}
                if (hash.getUUID(editArchive.save_session).equals(clicked_UUID)) {event.setCancelled(true); editArchive.saveInventory(p.getName()); return;}
                if (hash.getUUID(editArchive.reload_selection).equals(clicked_UUID)) {event.setCancelled(true); editArchive.reloadPlayerInventory(p.getName()); return;}
                if (hash.getUUID(editArchive.scroll_left).equals(clicked_UUID)) {event.setCancelled(true); editArchive.scrollPlaceholders(p.getName(), false); return;}
                if (hash.getUUID(editArchive.scroll_right).equals(clicked_UUID)) {event.setCancelled(true); editArchive.scrollPlaceholders(p.getName(), true); return;}
                if (hash.getUUID(editArchive.abort).equals(clicked_UUID)) {event.setCancelled(true); editArchive.endSession(p.getName()); return;}
                if (hash.getUUID(editArchive.save).equals(clicked_UUID)) {event.setCancelled(true); editArchive.saveAndEndSession(p.getName()); return;}
                if (hash.getUUID(editArchive.swap_view_1).equals(clicked_UUID)) {event.setCancelled(true); editArchive.swapView(p.getName(), true); return;}
                if (hash.getUUID(editArchive.swap_view_2).equals(clicked_UUID)) {event.setCancelled(true); editArchive.swapView(p.getName(), false); return;}
                break;
            case "review":
                // this gui is for accepting or rejecting items

                break;
        }
        if (action == 0) return;
        if (action == 5) return; // this value should never be able to be clicked
        if (action == 1) {event.setCancelled(true); return;}


    }
    public static boolean archiveClickEffect(Player p, int action, String action_data, String archive_name, int page) {
        switch (action) {
            case -4:
                p.showWinScreen();
                return true;
            case -3:
                p.showDemoScreen();
                return true;
            case -2:
                p.showElderGuardian();
                return true;
            case -1:
                p.closeInventory();
                return true;
            case 2:
                p.performCommand("open " + archive_name + " " + (page+1));
                return true;
            case 3:
                p.performCommand("open " + archive_name + " " + (page-1));
                return true;
            case 4:
                p.performCommand(action_data);
                return true;
            case 6:
                // go to submit page
                p.performCommand("submit " + archive_name);
                return true;
            case 7:
                p.setItemOnCursor(ItemConverter.toItemStack(action_data));
                return true;
            case 8:
                //p.openSign(); //string<\n>string<\n>string<\n>string
                return true;
            case 9:
                p.openBook(ItemConverter.toItemStack(action_data));
                return true;
        }
        return false;
    }
    @EventHandler
    public void OnClose(InventoryCloseEvent event) {
        // remove player from tracker when they close an inventory

        // used for editor sessions
        if (Storage.gui_tracker.containsKey(event.getPlayer().getName())) {
            if (Storage.gui_tracker.get(event.getPlayer().getName()).split("¦")[2].equals("ignore")) return;
            if (Storage.gui_tracker.get(event.getPlayer().getName()).split("¦")[2].equals("lock")) {
                editArchive.endSession(event.getPlayer().getName());
                event.getPlayer().sendMessage("§cEditor session aborted!");
                return;
            }
        }

        Storage.gui_tracker.remove(event.getPlayer().getName());
    }
}

package net.txsla.itemarchivereimagined;

import net.txsla.itemarchivereimagined.DataTypes.Archive;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class Listener implements org.bukkit.event.Listener {
    @EventHandler
    public void OnClick(InventoryClickEvent event) {
        // if player is not in a ItemArchive gui, then do not process input
        if (!Storage.gui_tracker.containsKey( event.getWhoClicked().getName() )) return;

            /*
            * There are a few actions we need to look out for
            * Placeholder Action Values
            *   0 = nothing
            *   1 = static
            *   2 = next_page
            *   3 = prev_page
            *   4 = execute_command
            *   5 = submission_populator
            *   6 = submit (or pg.0)
            * */

        // format: archive - gui - <int info>
        String[] data = Storage.gui_tracker.get(event.getWhoClicked().getName()).split("-");
        String archive_name = data[0];
        String gui = data[1];
        int info = Integer.parseInt(data[2]);
        int action = 0;
        // get Placeholder from archive
        Archive archive = Storage.archives.get(archive_name);

        // get action
        switch (gui) {
            case "open":
            case "submit":
                action = archive.getPlaceholder( archive.getPage(info).getPlaceholderIdFromSlot(event.getSlot()) ).getAction(); // action will be 0 if not found

                // next page actions for open and submit only
                if (action == 2) {
                    event.setCancelled(true);
                    ((Player) event.getWhoClicked()).performCommand("open " + archive_name + " " + (info+1));
                    return;
                }
                if (action == 3) {
                    event.setCancelled(true);
                    ((Player) event.getWhoClicked()).performCommand("open " + archive_name + " " + (info-1));
                    return;
                }

                break;
            case "edit":
                /* this gui is for editing Page objects
                *  there are a couple different custom items for this page
                *
                */

                break;
            case "review":
                // this gui is for accepting or rejecting items

                break;
        }
        if (action == 0) return;
        if (action == 5) return; // this value should never be able to be clicked
        if (action == 1) {event.setCancelled(true); return;}


    }
    @EventHandler
    public void OnClose(InventoryCloseEvent event) {
        // remove player from tracker when they close an inventory
        Storage.gui_tracker.remove(event.getPlayer().getName());
    }
}

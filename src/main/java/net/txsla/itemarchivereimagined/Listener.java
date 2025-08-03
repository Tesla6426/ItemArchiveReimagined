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
            *   5 = submission_populator
            *   6 = submit (or pg.0)
            *   7 = set item to cursor
            *   8 = open sign
            *   9 = open book
            * */

        // format: archive - gui - <int info>
        String[] data = Storage.gui_tracker.get(p.getName()).split("-");
        String archive_name = data[0];
        String gui = data[1];
        int info = Integer.parseInt(data[2]);
        int action = 0;

        // get Placeholder from archive
        Archive archive = Storage.archives.get(archive_name);
        String action_data = archive.getPlaceholder( archive.getPage(info).getPlaceholderIdFromSlot(event.getSlot()) ).getAction_data();
        action = archive.getPlaceholder( archive.getPage(info).getPlaceholderIdFromSlot(event.getSlot()) ).getAction();

        // get action
        switch (gui) {
            // these two are for when players open the archives
            case "open":
            case "submit":
                event.setCancelled(archiveClickEffect(p, action, action_data, archive_name, info));
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
    public static boolean archiveClickEffect(Player p, int action, String action_data, String archive_name, int info) {
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
                p.performCommand("open " + archive_name + " " + (info+1));
                return true;
            case 3:
                p.performCommand("open " + archive_name + " " + (info-1));
                return true;
            case 4:
                p.performCommand(action_data);
                return true;
            case 7:
                p.setItemOnCursor(ItemConverter.toItemStack(action_data));
                return true;
            case 8:
                //p.openSign(); write serializer
                return true;
            case 9:
                //p.openBook(); write serializer
                return true;
        }
        return false;
    }
    @EventHandler
    public void OnClose(InventoryCloseEvent event) {
        // remove player from tracker when they close an inventory
        Storage.gui_tracker.remove(event.getPlayer().getName());
    }
}

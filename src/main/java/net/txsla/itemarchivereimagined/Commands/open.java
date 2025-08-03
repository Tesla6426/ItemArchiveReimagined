package net.txsla.itemarchivereimagined.Commands;

import net.txsla.itemarchivereimagined.DataTypes.Archive;
import net.txsla.itemarchivereimagined.Storage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class open implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) { sender.sendMessage("This command can only be run by a player!"); return true; }
        if (args.length < 1) return false;
        Player p = (Player) sender;

        // validate user input
        if (!Storage.archives.containsKey(args[0])) {sender.sendMessage("§aArchive " + args[0] + " does not exist!"); return true;}
        int number = 1;

        if (args.length > 1) {
            try {
                number = Integer.parseInt(args[1]);
                if (number < 1) { sender.sendMessage("§aPage is reserved."); return true; }
            } catch (Exception e) {
                sender.sendMessage("§a" + args[1] + " is not a valid number!"); return true;
            }
        }

        // get archive
        Archive archive = Storage.archives.get(args[0]);

        // make sure page exists
        if ( (number > archive.countPages()-1) && !archive.inf_pages()) {sender.sendMessage("§aPage " + args[1] + " does not exist!"); return true;}

        // hand over control to gui
        net.txsla.itemarchivereimagined.Gui.openArchive.open(p, args[0], number);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        switch(args.length) {
            case 1:
               return new ArrayList<>(Storage.archives.keySet());
            case 2:
                if (Storage.archives.containsKey(args[0])) {
                    int pages = Storage.archives.get(args[0]).countPages();
                    for (int i = 1; i < pages; i++) list.add("" + i);
                }
        }
        return list;
    }
}

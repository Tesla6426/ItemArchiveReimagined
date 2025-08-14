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

public class reviewItems implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) { sender.sendMessage("This command can only be run by a player!"); return true; }
        if (args.length < 1) return false;
        Player p = (Player) sender;

        // validate user input
        if (!Storage.archives.containsKey(args[0])) {sender.sendMessage("§aArchive " + args[0] + " does not exist!"); return true;}

        // hand over control to gui
        net.txsla.itemarchivereimagined.Gui.reviewItems.open(p, args[0]);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return new ArrayList<>(Storage.archives.keySet());
    }
}

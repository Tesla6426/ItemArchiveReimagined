package net.txsla.itemarchivereimagined.Commands;

import net.txsla.itemarchivereimagined.DataTypes.Archive;
import net.txsla.itemarchivereimagined.Storage;
import net.txsla.itemarchivereimagined.load;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class submit implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length < 1) return false;
        if (!(sender instanceof Player)) { sender.sendMessage("This command can only be run by a player!"); return true; }

        Player p = (Player) sender;

        if (!Storage.archives.containsKey(args[0])) {sender.sendMessage("§cArchive " + args[0] + " does not exist!"); return true;}
        Archive archive = Storage.archives.get(args[0]);

        if (!archive.isAllow_submission()) {p.sendMessage("§cSubmissions are not enabled for this archive!"); return true;}


        // hand over control to gui
        net.txsla.itemarchivereimagined.Gui.submitArchive.openMenu(p, args[0]);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return new ArrayList<>(Storage.archives.keySet());
    }
}

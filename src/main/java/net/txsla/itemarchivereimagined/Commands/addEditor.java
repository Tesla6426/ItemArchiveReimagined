package net.txsla.itemarchivereimagined.Commands;

import net.txsla.itemarchivereimagined.DataTypes.Archive;
import net.txsla.itemarchivereimagined.Storage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class addEditor implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
    {
        if (args.length < 2) return false;

        if (!Storage.archives.containsKey(args[0])) {
            sender.sendMessage("§cArchive " + args[0] + " does not exist"); return true;
        }

        Archive archive = Storage.archives.get(args[0]);

        // make sure player is either op or editor
        if (!(sender.isOp() || archive.isEditor(sender.getName()))) {
            sender.sendMessage("You do not have permission to edit this archive!");
            return true;
        }

        Pattern regex = Pattern.compile("^\\w+$");
        if (!regex.matcher(args[1]).matches()) {sender.sendMessage("Invalid player name. " + args[1] + " does not match pattern '^\\w+$'"); return true;}

        archive.addEditor(args[1]);
        sender.sendMessage("Editor " + args[1] + " added to archive");

        archive.save();

        return true;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String str, @NotNull String[] args) {
        switch (args.length) {
            case 1:
                return new ArrayList<>(Storage.archives.keySet());
            case 2:
                return null;
        }
        return new ArrayList<>();
    }
}

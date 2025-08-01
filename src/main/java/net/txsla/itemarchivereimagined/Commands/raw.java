package net.txsla.itemarchivereimagined.Commands;

import net.txsla.itemarchivereimagined.DataTypes.Vault;
import net.txsla.itemarchivereimagined.Storage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class raw implements CommandExecutor, TabExecutor {
    // open a vault directly bypassing the archive interface
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
    {
        // use the damn command correctly
        if (args.length < 1) return false;

        // make sure the archive exists
        if (Storage.archives.containsKey(args[0])) {
            sender.sendMessage("§cArchive" + args[0] + " does not exist"); return true;
        }

        sender.sendMessage("§aThere are " + Storage.vaults.get(args[0]).getSize() + " items in vault " + args[0]);
        return true;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String str, @NotNull String[] args) {
        if (Storage.vaults == null) return new ArrayList<>();
        return new ArrayList<>(Storage.vaults.keySet());
    }
}

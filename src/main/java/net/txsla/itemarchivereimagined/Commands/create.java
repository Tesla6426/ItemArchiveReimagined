package net.txsla.itemarchivereimagined.Commands;

import net.txsla.itemarchivereimagined.DataTypes.Item;
import net.txsla.itemarchivereimagined.DataTypes.Vault;
import net.txsla.itemarchivereimagined.Storage;
import net.txsla.itemarchivereimagined.load;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class create implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
    {
        if (args.length < 1) return false;

        Pattern regex = Pattern.compile("^\\w+$");
        if (!regex.matcher(args[3]).matches()) {sender.sendMessage("Invalid archive name. " + args[3] + " does not match pattern '^\\w+$'"); return true;}

        if (Storage.archives.containsKey(args[0])) sender.sendMessage("§aArchive " + args[0] + " already exists, reloading instead.");
            else sender.sendMessage("§aArchive " + args[0] + " created!");

        load.archive(args[0]);
        return true;
   }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return new ArrayList<>();
    }
}


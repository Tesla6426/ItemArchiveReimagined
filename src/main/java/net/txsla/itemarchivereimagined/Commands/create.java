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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class create implements CommandExecutor, TabExecutor {
    public static List<String> timeIndex;
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
    {
        if (args.length < 1) return false;


        // add some input validation later

        Player p = (Player) sender;

        // load a new archive;
        load.archive(args[0]);
        sender.sendMessage("Archive " + args[0] + " created");

        return true;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return new ArrayList<>();
    }
}


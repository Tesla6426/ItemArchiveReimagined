package net.txsla.itemarchivereimagined.Commands;

import net.txsla.itemarchivereimagined.DataTypes.Item;
import net.txsla.itemarchivereimagined.DataTypes.Vault;
import net.txsla.itemarchivereimagined.Storage;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class submitRaw implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
    {
        // THIS IS A DEV/TESTING COMMAND AND IS NOT INTENDED TO BE USED BY REGULAR PLAYERS
        if (!(sender instanceof Player)) { sender.sendMessage("This command can only be run by a player!"); return true; }
        if (args.length < 2) return false;

        List<ItemStack> items = new ArrayList<>();
        Player p = (Player) sender;

        // get items from player
        switch (args[1]) {
            case "hand":
                items.add(p.getInventory().getItemInMainHand()); // bypasses all checks for now
                break;
            case "hotbar":
                items =
                        IntStream.range(0, 9).mapToObj(i -> p.getInventory().getItem(i))
                                .filter(item -> (item != null) && (item.getType() != Material.AIR))
                                .collect(Collectors.toList());
                break;
            case "inventory":
                for (ItemStack item : p.getInventory()) if (item != null && item.getType() != Material.AIR) items.add(item);
                break;
            default:
            case "gui":
                // Hand control over to gui
                return true;
        }
        int items_added = 0;
        // get vault
        Vault vault = Storage.vaults.get(args[0]);
        for (ItemStack item : items) {
            if (vault.addItem(new Item(item, p))) {
                sender.sendMessage("§aItem " + item.getItemMeta().getDisplayName() + "§a added to vault");
                items_added++;
            }
            else sender.sendMessage("§cItem " + item.getItemMeta().getDisplayName() + "§c rejected from vault. isDuplicate?");
        }

        // save vault
        vault.saveItemsToRam();
        vault.saveRamToFile();

        sender.sendMessage(items_added + " items added to vault " + args[0]);

        return true;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        switch (args.length) {
            case 2:
                list.add("hand");
                list.add("hotbar");
                list.add("gui");
                list.add("inventory");
                break;
            default:
                list = new ArrayList<>(Storage.vaults.keySet());
                break;
        }
        return list;
    }
}

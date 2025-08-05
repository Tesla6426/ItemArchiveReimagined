package net.txsla.itemarchivereimagined.Commands;


import net.txsla.itemarchivereimagined.DataTypes.Item;
import net.txsla.itemarchivereimagined.DataTypes.Vault;
import net.txsla.itemarchivereimagined.Gui.loadInv;
import net.txsla.itemarchivereimagined.Storage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class openDemoInventory implements CommandExecutor, TabExecutor {
    // open a vault directly bypassing the archive interface
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
    {
        if (!(sender instanceof Player)) { sender.sendMessage("This command can only be run by a player!"); return true; }
        Player p = (Player) sender;
        // use the damn command correctly
        if (args.length < 2) return false;

        // make sure the vault exists
        if (!Storage.vaults.containsKey(args[0])) {
            sender.sendMessage("§cVault " + args[0] + " does not exist"); return true;
        }


        int size = Integer.parseInt(args[1]);
        List<Item> items = Storage.vaults.get(args[0]).getItems();
        Inventory inventory = loadInv.create(size, "demo_inventory.vault=" + args[0] + ".size="+size);

        // Populate inventory slots with items (up to inventory size)
        int load;
        if (args.length == 3) load=55; // overload storage if 3rd arg exists
        else load =inventory.getSize();
        for (int i = 0; (i < items.size()) && (i < load); i++)  inventory.setItem(i, items.get(i).getItemStack());


        p.openInventory(inventory);
        return true;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String str, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        switch (args.length) {
            case 1:
                return new ArrayList<>(Storage.vaults.keySet());
            case 2:
                for (int i = 0; i < 37; i++) list.add(""+i);
                return list;
            case 3:
                list.add("true");
                list.add("false");
                return list;
        }
        return null;
    }
}

package net.txsla.itemarchivereimagined.Commands;


import net.txsla.itemarchivereimagined.DataTypes.Item;
import net.txsla.itemarchivereimagined.DataTypes.Vault;
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

        Inventory inventory;
        int size = Integer.parseInt(args[1]);
        List<Item> items = Storage.vaults.get(args[0]).getItems();

        if (size < 7) {
            inventory = Bukkit.createInventory(null, size * 9, args[0]);
        }else {
            // custom gui sizes, allows designer to choose from any inventory.
            switch (size) {
                case 7:
                    inventory = Bukkit.createInventory(null, InventoryType.ANVIL, args[0]);
                    break;
                case 8:
                    inventory = Bukkit.createInventory(null, InventoryType.BARREL, args[0]);
                    break;
                case 9:
                    inventory = Bukkit.createInventory(null, InventoryType.BEACON, args[0]);
                    break;
                case 10:
                    inventory = Bukkit.createInventory(null, InventoryType.BLAST_FURNACE, args[0]);
                    break;
                case 11:
                    inventory = Bukkit.createInventory(null, InventoryType.BREWING, args[0]);
                    break;
                case 12:
                    inventory = Bukkit.createInventory(null, InventoryType.CARTOGRAPHY, args[0]);
                    break;
                case 13:
                    inventory = Bukkit.createInventory(null, InventoryType.CHEST, args[0]);
                    break;
                case 14:
                    inventory = Bukkit.createInventory(null, InventoryType.CHISELED_BOOKSHELF, args[0]);
                    break;
                case 15:
                    inventory = Bukkit.createInventory(null, InventoryType.COMPOSTER, args[0]);
                    break;
                case 16:
                    inventory = Bukkit.createInventory(null, InventoryType.CRAFTER, args[0]);
                    break;
                case 17:
                    inventory = Bukkit.createInventory(null, InventoryType.CRAFTING, args[0]);
                    break;
                case 18:
                    inventory = Bukkit.createInventory(null, InventoryType.CREATIVE, args[0]);
                    break;
                case 19:
                    inventory = Bukkit.createInventory(null, InventoryType.DECORATED_POT, args[0]);
                    break;
                case 20:
                    inventory = Bukkit.createInventory(null, InventoryType.DISPENSER, args[0]);
                    break;
                case 21:
                    inventory = Bukkit.createInventory(null, InventoryType.DROPPER, args[0]);
                    break;
                case 22:
                    inventory = Bukkit.createInventory(null, InventoryType.ENCHANTING, args[0]);
                    break;
                case 23:
                    inventory = Bukkit.createInventory(null, InventoryType.ENDER_CHEST, args[0]);
                    break;
                case 24:
                    inventory = Bukkit.createInventory(null, InventoryType.FURNACE, args[0]);
                    break;
                case 25:
                    inventory = Bukkit.createInventory(null, InventoryType.GRINDSTONE, args[0]);
                    break;
                case 26:
                    inventory = Bukkit.createInventory(null, InventoryType.HOPPER, args[0]);
                    break;
                case 27:
                    inventory = Bukkit.createInventory(null, InventoryType.JUKEBOX, args[0]);
                    break;
                case 28:
                    inventory = Bukkit.createInventory(null, InventoryType.LECTERN, args[0]);
                    break;
                case 29:
                    inventory = Bukkit.createInventory(null, InventoryType.LOOM, args[0]);
                    break;
                case 30:
                    inventory = Bukkit.createInventory(null, InventoryType.MERCHANT, args[0]);
                    break;
                case 31:
                    inventory = Bukkit.createInventory(null, InventoryType.PLAYER, args[0]);
                    break;
                case 32:
                    inventory = Bukkit.createInventory(null, InventoryType.SHULKER_BOX, args[0]);
                    break;
                case 33:
                    inventory = Bukkit.createInventory(null, InventoryType.SMITHING, args[0]);
                    break;
                case 34:
                    inventory = Bukkit.createInventory(null, InventoryType.SMOKER, args[0]);
                    break;
                case 35:
                    inventory = Bukkit.createInventory(null, InventoryType.STONECUTTER, args[0]);
                    break;
                case 36:
                    inventory = Bukkit.createInventory(null, InventoryType.WORKBENCH, args[0]);
                    break;
                default: // default to a max sized inventory
                    inventory = Bukkit.createInventory(null, 54, args[0]);
            }
        }

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

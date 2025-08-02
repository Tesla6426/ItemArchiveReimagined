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
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class search implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
    {

        Player p = (Player) sender;
        // use the damn command correctly
        if (args.length < 2) return false;


        // make sure the vault exists
        if (Storage.vaults.containsKey(args[0])) {
            sender.sendMessage("§cArchive " + args[0] + " does not exist"); //return true;
        }

        // some variables
        Vault vault = Storage.vaults.get(args[0]);
        String parameter = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
        Inventory chestInventory = Bukkit.createInventory(null, 54, "Searching " + args[0] + ". " + args[1] + " : " + parameter);

        new Thread(() -> {
            List<Item> items = new ArrayList<>();
            switch (args[1]) {
                case "item_name":
                    items = vault.searchFromItemName(parameter, 54);
                    break;
                case "item_uuid":
                    items = vault.searchFromItemUUID(parameter, 54);
                    break;
                case "submitter_name":
                    items = vault.searchFromSubmitterName(parameter, 54);
                    break;
                case "submitter_uuid":
                    items = vault.searchFromSubmitterUUID(parameter, 54);
                    break;
                case "item_date":
                    items = vault.searchFromItemSubmitDate(1, true, 54);
                    break;
                case "item_size":
                    items = vault.searchFromItemSize(1, true, 54);
                    break;
                case "item_version":
                    items = vault.searchFromItemVersion(parameter, 54);
                    break;
                case "item_nbt":
                    items = vault.searchFromItemNBT(parameter, 54);
                    break;
                case "item_data":
                    items = vault.searchFromItemRawData(parameter, 54);
                    break;
                case "submitter_language":
                    items = vault.searchFromItemLanguage(parameter, 54);
                    break;
                default:
                    sender.sendMessage("Unknown search parameter.");
                    break;
            }
            sender.sendMessage(items.size() + " items found!");

            // Populate inventory slots with items (up to inventory size)
            for (int i = 0; (i < items.size()) && (i < 54); i++) chestInventory.setItem(i, items.get(i).getItemStack());

            // open inv sync
            Bukkit.getScheduler().runTask(Storage.server, () -> {
                if (p.isOnline()) {
                    p.openInventory(chestInventory);
                }
            });
        }).start();

        return true;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String str, @NotNull String[] args) {
        List<String> list = new ArrayList<>();

        switch (args.length) {
            case 1:
                list = new ArrayList<>(Storage.vaults.keySet());
                break;
            case 2:
                list.add("item_name");
                list.add("item_uuid");
                list.add("submitter_name");
                list.add("submitter_uuid");
                list.add("item_date");
                list.add("item_size");
                list.add("item_version");
                list.add("item_nbt");
                list.add("item_data");
                list.add("submitter_language");
                break;
            case 3:
                if (args[1].equals("item_size") || args[1].equals("item_date")) {
                    list.add("greater_than");
                    list.add("less_than");
                }else {
                    list.add("<regex>");
                }
                break;
            case 4:
                if (args[1].equals("item_size") || args[1].equals("item_date")) {
                    list.add("<int>");
                }
                break;
        }
        return list;
    }
}

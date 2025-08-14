package net.txsla.itemarchivereimagined.Commands;

import net.txsla.itemarchivereimagined.DataTypes.Archive;
import net.txsla.itemarchivereimagined.DataTypes.Vault;
import net.txsla.itemarchivereimagined.Storage;
import net.txsla.itemarchivereimagined.hash;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class removeItem implements CommandExecutor, TabExecutor {
    // open a vault directly bypassing the archive interface
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
    {
        if (!(sender instanceof Player)) { sender.sendMessage("This command can only be run by a player!"); return true; }
        Player p = (Player) sender;
        // use the damn command correctly
        if (args.length < 2) return false;

        // make sure the vault exists
        if (!Storage.archives.containsKey(args[0])) { sender.sendMessage("§cArchive " + args[0] + " does not exist"); return true; }
        String uuid = null;

        Archive archive = Storage.archives.get(args[0]);
        if (!archive.isEditor(p.getName())) { p.sendMessage("§cYou do not have permission to edit this archive!"); return true;}

        Vault vault = Storage.vaults.get(args[0] + "-main");

        switch (args[1]) {
            case "item_uuid":
                 if (args.length > 2) uuid = args[2];
            case "item_in_hand":
                if (uuid == null) uuid = hash.getUUID(p.getInventory().getItemInMainHand());

                if (vault.removeItemByUUID(uuid, archive.name())) {
                    p.sendMessage("Item " + uuid + " removed from archive.");
                    vault.saveItemsToRam();
                    vault.saveRamToFile();
                    return true;
                }

                p.sendMessage("Item " + uuid + " not found.");
                break;
            case "submitter_name":
                if (args.length < 3) return true; // user needs to actually give a name

                Pattern regex = Pattern.compile("^\\w+$");
                if (!regex.matcher(args[2]).matches()) {sender.sendMessage("Invalid player name. " + args[2] + " does not match pattern '^\\w+$'"); return true;}

                p.sendMessage("Removing " + args[2] + "'s items from the archive.");
                int removed = vault.removeItemsBySubmitter(args[2], archive.name());
                p.sendMessage(removed + " items found and removed from archive.");

                break;
            default:
                sender.sendMessage("§cUnknown argument?");
                return true;
        }
        return true;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String str, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        switch (args.length) {
            case 1:
                return new ArrayList<>(Storage.archives.keySet());
            case 2:
                list.add("item_uuid");
                list.add("submitter_name");
                list.add("item_in_hand");
            case 3:
                if (!args[1].equals("item_in_hand")) list.add("<string>");
        }
        return list;
    }
}

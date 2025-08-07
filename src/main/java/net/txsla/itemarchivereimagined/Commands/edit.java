package net.txsla.itemarchivereimagined.Commands;

import net.txsla.itemarchivereimagined.DataTypes.Archive;
import net.txsla.itemarchivereimagined.DataTypes.Page;
import net.txsla.itemarchivereimagined.DataTypes.Placeholder;
import net.txsla.itemarchivereimagined.Gui.editArchive;
import net.txsla.itemarchivereimagined.Storage;
import net.txsla.itemarchivereimagined.load;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class edit implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
    {
        if (args.length < 4) return false;
        if (!(sender instanceof Player)) { sender.sendMessage("This command can only be run by a player!"); return true; }

        Player p = (Player) sender;

        if (!Storage.archives.containsKey(args[0])) {sender.sendMessage("§aArchive " + args[0] + " does not exist!"); return true;}
        Archive archive = Storage.archives.get(args[0]);

        int val, val2; double d_val; Page page; Placeholder placeholder; ItemStack item; Pattern regex = Pattern.compile("^\\w+$");// temp vars, compiler should optimise this for me
        switch (args[1]) {
            case "placeholder":
                switch (args[2]) {
                    case "create":
                        if (!regex.matcher(args[3]).matches()) {p.sendMessage("Invalid placeholder name. " + args[3] + " does not match pattern '^\\w+$'"); return true;}
                        if (archive.hasPlaceholder(args[3])) {p.sendMessage("Placeholder " + args[3] + " already exists"); return true;}

                        // get item and create placeholder
                        item = p.getInventory().getItemInMainHand();
                        if ((item == null) || (item.getType().isAir())) item = Storage.grass;
                        placeholder = new Placeholder(args[3]);
                        placeholder.setItem(item);

                        archive.addPlaceholder(placeholder);
                        p.sendMessage("Placeholder " + args[3] + " created for archive " + archive.name() + " with item " + placeholder.getItem().getItemMeta().getDisplayName());
                        archive.savePlaceholders();

                        break;
                    case "modify":
                        switch (args[4]) {
                            case "item":
                                if (!archive.hasPlaceholder(args[3])) {p.sendMessage("Placeholder " + args[3] + " not found."); return true;}
                                // set placeholder to item in hand
                                placeholder = archive.getPlaceholder(args[3]);
                                item = p.getInventory().getItemInMainHand();
                                if ((item == null) || (item.getType().isAir())) {p.sendMessage("You must be holding an item!"); return true;}
                                placeholder.setItem(item); // set item to new item
                                if (archive.setPlaceholder(args[3], placeholder)) { // replace placeholder
                                    p.sendMessage("Placeholder " + args[3] + " item set to " + item.getItemMeta().getDisplayName());
                                    archive.savePlaceholders();
                                }else {
                                    p.sendMessage("Placeholder " + args[3] + " not found."); return true;
                                }
                                break;
                            case "action":
                                if (!archive.hasPlaceholder(args[3])) {p.sendMessage("Placeholder " + args[3] + " not found."); return true;}
                                // set action value to integer
                                placeholder = archive.getPlaceholder(args[3]);
                                try {
                                    val = Integer.parseInt(args[5]);
                                } catch (Exception e) {
                                    p.sendMessage(args[5] + " is not a valid integer."); return true;
                                }
                                placeholder.setAction(val); // set action
                                if (archive.setPlaceholder(args[3], placeholder)) { // replace placeholder
                                    p.sendMessage("Placeholder " + args[3] + " action set to " + args[5]);
                                    archive.savePlaceholders();
                                }else {
                                    p.sendMessage("Placeholder " + args[3] + " not found."); return true;
                                }
                                break;
                            case "action_data":
                                // set action data to specified type

                                break;
                        }
                        break;
                    case "remove":
                        if (archive.removePlaceholder(args[3])) {
                            p.sendMessage("Placeholder " + args[3] + " removed from archive " + archive.name());
                            archive.savePlaceholders();
                        }else {
                            p.sendMessage("Placeholder " + args[3] + " not found."); return true;
                        }
                        break;
                }
                break;
            case "page":
                switch (args[2]) {
                    case "remove":
                        try {
                            val = Integer.parseInt(args[3]); if (val > archive.countPages()-1) {p.sendMessage("Page " + args[3] + " does not exist."); return true;}
                        }catch (Exception e) { p.sendMessage(args[3] + " is not a valid Integer."); return true;}
                        archive.removePage(val);
                        archive.savePages();
                        p.sendMessage("Page " + args[3] + " has been removed from " + args[0] + ".");
                        break;
                    case "create":
                        if (!regex.matcher(args[3]).matches()) {p.sendMessage("Invalid page name. " + args[3] + " does not match pattern '^\\w+$'"); return true;}
                        archive.addPage(new Page("New.Page", 9, 3,new String[] {
                                "air", "air", "air", "air", "air", "air", "air", "air", "air",
                                "vault", "vault", "vault", "vault", "vault", "vault", "vault", "vault", "vault",
                                "air", "air", "air", "example1", "air", "example2", "air", "air", "air"
                        }));
                        archive.savePages();
                        p.sendMessage("A new page has been added to " + args[0]);
                        break;
                    case "swap":
                        try {
                            val = Integer.parseInt(args[3]); val2 = Integer.parseInt(args[4]);
                            if (val > archive.countPages()-1) {p.sendMessage("Page " + args[3] + " does not exist."); return true;}
                            if (val2 > archive.countPages()-1) {p.sendMessage("Page " + args[4] + " does not exist."); return true;}
                        }catch (Exception e) { p.sendMessage("Either " + args[3] + " or " + args[4] + " is not a valid Integer."); return true;}
                        Page temp = archive.getPage(val); // save page 1
                        archive.setPage(val, archive.getPage(val2)); // save page 1 to page 2
                        archive.setPage(val2, temp); // set page 2 to page 1
                        archive.savePages();
                        p.sendMessage("Pages " + args[3] + " and " + args[4] + " have been swapped.");
                        break;
                    case "modify":
                        switch (args[4]) {
                            case "name":
                                try {
                                    val = Integer.parseInt(args[3]); if (val > archive.countPages()-1) {p.sendMessage("Page " + args[3] + " does not exist."); return true;}
                                }catch (Exception e) { p.sendMessage(args[3] + " is not a valid Integer."); return true;}
                                page = archive.getPage(val);
                                page.setName(String.join(" ", Arrays.copyOfRange(args, 5, args.length))); // spaces are valid here
                                archive.setPage(val, page);
                                archive.savePages();
                                break;
                            case "size":
                                try {
                                    val = Integer.parseInt(args[3]);
                                    val2 = Integer.parseInt(args[5]);
                                    if (val > archive.countPages()-1) {p.sendMessage("Page " + args[3] + " does not exist."); return true;}
                                }catch (Exception e) { p.sendMessage(args[3] + " is not a valid Integer."); return true;}
                                page = archive.getPage(val);
                                page.setSize(val2);
                                archive.setPage(val, page);
                                archive.savePages();
                                p.sendMessage("Page " + args[3] + "'s size ha been set to " + args[5] + ".");
                                break;
                            case "format":
                                try {
                                    val = Integer.parseInt(args[3]); if (val > archive.countPages()-1) {p.sendMessage("Page " + args[3] + " does not exist."); return true;}
                                }catch (Exception e) { p.sendMessage(args[3] + " is not a valid Integer."); return true;}
                                // hand over control to editor session
                                editArchive.startSession(p, archive.name(), val);
                                return true;
                        }
                        break;
                }
                break;
            case "config":
                switch (args[2]) {
                    case "inf-pages":
                        archive.setInf_pages(args[3].equals("true"));
                        p.sendMessage("Archive " + args[0] + " config inf-pages set to " + args[3].equals("true"));
                        break;
                    case "allow-submissions":
                        archive.setAllow_submission(args[3].equals("true"));
                        p.sendMessage("Archive " + args[0] + " config allow-submissions set to " + args[3].equals("true"));
                        break;
                    case "review-items":
                        archive.setReview_items(args[3].equals("true"));
                        p.sendMessage("Archive " + args[0] + " config review-items set to " + args[3].equals("true"));
                        break;
                    case "max-submission-size":
                        try {
                            val = Integer.parseInt(args[3]);
                        } catch (Exception e) { p.sendMessage(args[3] + " is not a valid integer"); return true; }
                        archive.setMax_item_size(val);
                        p.sendMessage("Archive " + args[0] + " config max-submission-size set to " + args[3]);
                        break;
                    case "min-submission-size":
                        try {
                            val = Integer.parseInt(args[3]);
                        } catch (Exception e) { p.sendMessage(args[3] + " is not a valid integer"); return true; }
                        archive.setMin_item_size(val);
                        p.sendMessage("Archive " + args[0] + " config min-submission-size set to " + args[3]);
                        break;
                    case "max-stack-size":
                        try {
                            val = Integer.parseInt(args[3]);
                        } catch (Exception e) { p.sendMessage(args[3] + " is not a valid integer"); return true; }
                        archive.setMax_stack_size(val);
                        p.sendMessage("Archive " + args[0] + " config max-stack-size set to " + args[3]);
                        break;
                    case "submit-delay":
                        try {
                            d_val = Double.parseDouble(args[3]);
                        } catch (Exception e) { p.sendMessage(args[3] + " is not a valid double"); return true; }
                        archive.setSubmit_delay(d_val);
                        p.sendMessage("Archive " + args[0] + " config max-stack-size set to " + args[3]);
                        break;
                    default:
                        p.sendMessage("Unknown config option " + args[2] + "?");
                        return true;
                }
                break;
        }
        archive.save(); // save config to SSD
        return true;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        // complex ahh tab menu
        switch (args.length) {
            case 1:
                return new ArrayList<>(Storage.archives.keySet());
            case 2:
                if (!Storage.archives.containsKey(args[0])) {list.add("?"); break;}
                list.add("placeholder");
                list.add("page");
                list.add("config");
                break;
            case 3:
                switch (args[1]) {
                    case "config":
                        list.add("inf-pages");
                        list.add("allow-submissions");
                        list.add("review-items");
                        list.add("max-stack-size");
                        list.add("max-submission-size");
                        list.add("min-submission-size");
                        list.add("submit-delay");
                        break;
                    case "page":
                        list.add("swap");
                    case "placeholder":
                        list.add("create");
                        list.add("modify");
                        list.add("remove");
                        break;
                    default:
                        list.add("?");
                        break;
                }
                break;
            case 4:
                switch (args[2]) {
                    case "create":
                        list.add("<string>");
                        break;
                    case "modify":
                    case "remove":
                    case "swap":
                        if (!Storage.archives.containsKey(args[0])) {list.add("?"); break;}
                        switch (args[1]) {
                            case "placeholder":
                                for (Placeholder p : Storage.archives.get(args[0]).getPlaceholders()) list.add(p.getId());
                                break;
                            case "page":
                                for (int i = 0; i < Storage.archives.get(args[0]).countPages(); i++) list.add(""+i);
                                break;
                            default:
                                list.add("?");
                                break;
                        }
                        break;
                    case "inf-pages":
                    case "allow-submissions":
                    case "review-items":
                        list.add("true");
                        list.add("false");
                        break;
                    case "max-submission-size":
                    case "min-submission-size":
                    case "max-stack-size":
                        list.add("<int>");
                        break;
                    case "submit-delay":
                        list.add("<double>");
                        break;
                    default:
                        list.add("?");
                        break;
                }
                break;
            case 5:
                if (!Storage.archives.containsKey(args[0])) {list.add("?"); break;}
                switch (args[2]) {
                    case "swap":
                        for (int i = 0; i < Storage.archives.get(args[0]).countPages(); i++) list.add(""+i);
                        break;
                    case "modify":
                        switch (args[1]) {
                            case "placeholder":
                                list.add("action"); // <int>
                                list.add("action_data"); // string, item, book, sign
                                list.add("item"); // ItemStack from mainhand
                                break;
                            case "page":
                                list.add("size"); // <int>
                                list.add("name"); // <string>, minimessage
                                list.add("format"); // go to gui editor
                                break;
                        }
                        break;
                    default:
                        list.add("?");
                        break;
                }
                break;
            case 6:
                switch (args[4]) {
                    case "name":
                        list.add("<string>");
                        break;
                    case "action":
                    case "size":
                        list.add("<int>");
                        break;
                    case "action_data":
                        list.add("string");
                        list.add("sign");
                        list.add("book");
                        list.add("item");
                }
                break;
            case 7:
                switch (args[5]) {
                    case "string":
                        list.add("<string>");
                        break;
                }
        }
        return list;
    }
}
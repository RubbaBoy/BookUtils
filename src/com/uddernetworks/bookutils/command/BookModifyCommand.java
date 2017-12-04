package com.uddernetworks.bookutils.command;

import com.uddernetworks.bookutils.main.Main;
import com.uddernetworks.bookutils.utils.ArgumentUtil;
import com.uddernetworks.bookutils.utils.BookAction;
import com.uddernetworks.bookutils.utils.BookUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class BookModifyCommand implements CommandExecutor {

    Main plugin;
    private BookUtils bookUtils;

    public BookModifyCommand(Main passedPlugin) {
        this.plugin = passedPlugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        args = ArgumentUtil.parseQuotes(args);
        if (cmd.getName().equalsIgnoreCase("bookmodify")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("bu.bookmodify")) {
                    if (args.length > 0) {
                        ItemStack book1 = (player.getInventory().getItemInMainHand().getType() == Material.BOOK_AND_QUILL ||
                                player.getInventory().getItemInMainHand().getType() == Material.WRITTEN_BOOK) ? player.getInventory().getItemInMainHand() : null;
                        ItemStack book2 = (player.getInventory().getItemInOffHand().getType() == Material.BOOK_AND_QUILL ||
                                player.getInventory().getItemInOffHand().getType() == Material.WRITTEN_BOOK) ? player.getInventory().getItemInOffHand() : null;
                        switch (args[0].toUpperCase()) {
                            case "ADDPAGE":
                                if (args.length == 2) {
                                    bookUtils = new BookUtils(BookAction.ADDPAGE, player, book1, book2, args[1]);
                                    player.getInventory().setItemInMainHand(bookUtils.getNewBook());
                                } else {
                                    player.sendMessage(String.format(MessageEnum.NEEDS_OPTION.getMessage(), "an integer"));
                                }
                                break;
                            case "REMOVEPAGE":
                                if (args.length == 2) {
                                    bookUtils = new BookUtils(BookAction.REMOVEPAGE, player, book1, book2, args[1]);
                                    player.getInventory().setItemInMainHand(bookUtils.getNewBook());
                                } else {
                                    player.sendMessage(String.format(MessageEnum.NEEDS_OPTION.getMessage(), "an integer"));
                                }
                                break;
                            case "TRIM":
                                bookUtils = new BookUtils(BookAction.TRIM, player, book1, book2, null);
                                player.getInventory().setItemInMainHand(bookUtils.getNewBook());
                                break;
                            case "COMBINE":
                                bookUtils = new BookUtils(BookAction.COMBINE, player, book1, book2, null);
                                player.getInventory().setItemInMainHand(bookUtils.getNewBook());
                                break;
                            case "SETAUTHOR":
                                if (args.length == 2) {
                                    bookUtils = new BookUtils(BookAction.SETAUTHOR, player, book1, book2, args[1]);
                                    player.getInventory().setItemInMainHand(bookUtils.getNewBook());
                                } else {
                                    player.sendMessage(String.format(MessageEnum.NEEDS_OPTION.getMessage(), "a string"));
                                }
                                break;
                            case "SETTITLE":
                                if (args.length == 2) {
                                    bookUtils = new BookUtils(BookAction.SETTITLE, player, book1, book2, args[1]);
                                    player.getInventory().setItemInMainHand(bookUtils.getNewBook());
                                } else {
                                    player.sendMessage(String.format(MessageEnum.NEEDS_OPTION.getMessage(), "a string"));
                                }
                                break;
                            case "UNSIGN":
                                BookMeta bookMeta = (BookMeta) book1.getItemMeta();
                                if (bookMeta.getPages().size() <= 50) {
                                    bookUtils = new BookUtils(BookAction.UNSIGN, player, book1, book2, null);
                                    player.getInventory().setItemInMainHand(bookUtils.getNewBook());
                                } else {
                                    player.sendMessage(MessageEnum.TOO_MANY_PAGES.getMessage());
                                }
                                break;
                            case "SETORIGINAL":
                                bookUtils = new BookUtils(BookAction.SETORIGINAL, player, book1, book2, null);
                                player.getInventory().setItemInMainHand(bookUtils.getNewBook());
                                break;
                            default:
                                sender.sendMessage(MessageEnum.HELP_BOOKMODIFY.getMessage());
                                break;
                        }
                    } else {
                        sender.sendMessage(MessageEnum.HELP_BOOKMODIFY.getMessage());
                    }
                } else {
                    sender.sendMessage(MessageEnum.NO_PERMISSION.getMessage());
                }
            } else {
                sender.sendMessage(MessageEnum.PLAYERS_ONLY.getMessage());
            }
            return true;
        }
        return false;
    }

}

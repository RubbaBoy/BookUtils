package com.uddernetworks.bookutils.command;

import com.uddernetworks.bookutils.main.Main;
import com.uddernetworks.bookutils.filetype_managers.PDFManager;
import com.uddernetworks.bookutils.filetype_managers.TXTManager;
import com.uddernetworks.bookutils.utils.ArgumentUtil;
import com.uddernetworks.bookutils.utils.IntegerUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BookMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class BookExportCommand implements CommandExecutor {

    Main plugin;

    public BookExportCommand(Main passedPlugin) {
        this.plugin = passedPlugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        args = ArgumentUtil.parseQuotes(args);
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("bookexport")) {
                if (player.hasPermission("bu.bookexport")) {
                    if (player.getInventory().getItemInMainHand().getType() == Material.WRITTEN_BOOK) {
                        if (args.length >= 2) {
                            BookMeta bookMeta;
                            String name;
                            switch (args[1]) {
                                case "txt":
                                    bookMeta = (BookMeta) player.getInventory().getItemInMainHand().getItemMeta();
                                    name = args[0].equalsIgnoreCase("%auto%") ? bookMeta.getTitle().replaceAll("<", "").replaceAll(">", "").replaceAll("\\.", "").replaceAll("\\\\", "").replaceAll("/", "").replaceAll("|", "").replaceAll("\\*", "") + " - " + bookMeta.getAuthor() : args[0];
                                    TXTManager txtManager = new TXTManager(name, new ArrayList<>(bookMeta.getPages()));
                                    player.sendMessage((txtManager.writeTXT() ? MessageEnum.TXT_SUCCESS : MessageEnum.TXT_FAIL).getMessage());
                                    break;
                                case "pdf":
                                    if (args.length == 4) {
                                        if ((args[2].equalsIgnoreCase("yes") || args[2].equalsIgnoreCase("no"))) {
                                            if (IntegerUtil.isInteger(args[3], 10) && Integer.parseInt(args[3]) > 0) {
                                                bookMeta = (BookMeta) player.getInventory().getItemInMainHand().getItemMeta();
                                                name = args[0].equalsIgnoreCase("%auto%") ? bookMeta.getTitle().replaceAll("<", "").replaceAll(">", "").replaceAll("\\.", "").replaceAll("\\\\", "").replaceAll("/", "").replaceAll("|", "").replaceAll("\\*", "") + " - " + bookMeta.getAuthor() : args[0];
                                                boolean printColor = args[2].equalsIgnoreCase("yes");
                                                int fontsize = Integer.parseInt(args[3]);

                                                PDFManager pdfManager = new PDFManager("plugins" + File.separator + "SavedBooks" + File.separator + name + ".pdf", bookMeta.getPages().stream().collect(Collectors.joining(" ")), printColor, fontsize);
                                                player.sendMessage((pdfManager.writePDF() ? MessageEnum.PDF_SUCCESS : MessageEnum.PDF_FAIL).getMessage());
                                            } else {
                                                player.sendMessage(MessageEnum.HELP_FONTSIZE.getMessage());
                                            }
                                        } else {
                                            player.sendMessage(MessageEnum.HELP_YES_NO.getMessage());
                                        }
                                    } else {
                                        player.sendMessage(MessageEnum.HELP_PDF_OPTIONS.getMessage());
                                    }
                                    break;
                                default:
                                    player.sendMessage(MessageEnum.UNSUPPORTED_FORMAT.getMessage());
                                    break;
                            }
                        } else {
                            player.sendMessage(MessageEnum.HELP_BOOKEXPORT.getMessage());
                        }
                    } else {
                        player.sendMessage(MessageEnum.MUST_HOLD_BOOK.getMessage());
                    }
                } else {
                    player.sendMessage(MessageEnum.NO_PERMISSION.getMessage());
                }
                return true;
            }
        } else {
            sender.sendMessage(MessageEnum.PLAYERS_ONLY.getMessage());
        }
        return false;
    }

}

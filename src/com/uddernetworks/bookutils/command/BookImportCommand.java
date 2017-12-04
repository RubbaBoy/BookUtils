package com.uddernetworks.bookutils.command;

import com.uddernetworks.bookutils.filetype_managers.PDFManager;
import com.uddernetworks.bookutils.input.TXTImport;
import com.uddernetworks.bookutils.main.Main;
import com.uddernetworks.bookutils.utils.ArgumentUtil;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftMetaBook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookImportCommand implements CommandExecutor {

    Main plugin;

    public BookImportCommand(Main passedPlugin) {
        this.plugin = passedPlugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        args = ArgumentUtil.parseQuotes(args);
        if (cmd.getName().equalsIgnoreCase("bookimport")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("bu.bookimport")) {
                    if (args.length == 3) {
                        String filename = args[0];
                        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                        BookMeta bookMeta = (BookMeta) book.getItemMeta();
                        List<IChatBaseComponent> pages;
                        try {
                            pages = (List<IChatBaseComponent>) CraftMetaBook.class.getDeclaredField("pages").get(bookMeta);
                        } catch (ReflectiveOperationException ex) {
                            ex.printStackTrace();
                            pages = new ArrayList<>();
                        }
                        if (filename.endsWith(".txt")) {
                            TXTImport txtImport = new TXTImport(filename, pages);
                            if (txtImport.importFile()) {
                                bookMeta.setTitle(args[1].equals("%auto%") ? filename.substring(0, filename.length() - 4) : args[1]);
                                bookMeta.setAuthor(args[2]);
                                book.setItemMeta(bookMeta);
                                player.getInventory().setItemInMainHand(book);

                                player.sendMessage(String.format(MessageEnum.SUCCESS_IMPORT.getMessage(), filename));
                            } else {
                                player.sendMessage(String.format(MessageEnum.FAILED_IMPORT.getMessage(), filename));
                            }
                        } else if (filename.endsWith(".pdf")) {
                            try {
                                PDFManager pdfManager = new PDFManager(filename);
                                ArrayList<String> list = TXTImport.addLinebreaks(pdfManager.readPDF());
                                pages.clear();
                                for (String string : list) {
                                    IChatBaseComponent page = IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(new TextComponent(string)));
                                    pages.add(page);
                                }
                                bookMeta.setTitle(args[1].equals("%auto%") ? filename.substring(0, filename.length() - 4) : args[1]);
                                bookMeta.setAuthor(args[2]);
                                book.setItemMeta(bookMeta);
                                player.getInventory().setItemInMainHand(book);
                            } catch (IOException e) {
                                e.printStackTrace();
                                player.sendMessage(String.format(MessageEnum.FAILED_IMPORT.getMessage(), filename));
                                return true;
                            }
                            player.sendMessage(String.format(MessageEnum.SUCCESS_IMPORT.getMessage(), filename));
                        }
                    } else {
                        player.sendMessage(MessageEnum.HELP_IMPORT.getMessage());
                    }
                } else {
                    player.sendMessage(MessageEnum.NO_PERMISSION.getMessage());
                }
            } else {
                sender.sendMessage(MessageEnum.PLAYERS_ONLY.getMessage());
            }
            return true;
        }
        return false;
    }

}

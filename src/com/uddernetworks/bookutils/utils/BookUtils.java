package com.uddernetworks.bookutils.utils;

import com.google.common.collect.Lists;
import com.uddernetworks.bookutils.command.MessageEnum;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftMetaBook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;

public class BookUtils {

    private final BookAction action;
    private Player player;
    private ItemStack book1;
    private ItemStack book2;
    private Object amount_obj;

    private BookMeta bookMeta;
    private List<IChatBaseComponent> pages;

    public BookUtils(BookAction action, Player player, ItemStack book1, ItemStack book2, Object amount_obj) {
        this.action = action;
        this.player = player;
        this.book1 = book1.clone();
        this.book2 = book2;
        this.amount_obj = amount_obj;
    }

    public ItemStack getNewBook() {
        switch (action) {
            case ADDPAGE:
                if (IntegerUtil.isInteger(amount_obj.toString(), 10)) {
                    int amount = Integer.valueOf(amount_obj.toString());
                    if (amount > 0) {
                        bookMeta = (BookMeta) book1.getItemMeta();
                        try {
                            pages = (List<IChatBaseComponent>) CraftMetaBook.class.getDeclaredField("pages").get(bookMeta);
                        } catch (ReflectiveOperationException ex) {
                            ex.printStackTrace();
                            pages = new ArrayList<>();
                        }
                        for (int i = 0; i < amount; i++) {
                            IChatBaseComponent page = IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(new TextComponent("")));
                            pages.add(page);
                        }
                        book1.setItemMeta(bookMeta);
                    } else {
                        player.sendMessage(MessageEnum.AMOUNT_MUST_BE_OVER_ZERO.getMessage());
                    }
                } else {
                    player.sendMessage(MessageEnum.AMOUNT_MUST_BE_OVER_ZERO.getMessage());
                }
                break;
            case REMOVEPAGE:
                if (IntegerUtil.isInteger(amount_obj.toString(), 10)) {
                    int index = Integer.valueOf(amount_obj.toString());
                    if (index > 0) {
                        bookMeta = (BookMeta) book1.getItemMeta();
                        try {
                            pages = (List<IChatBaseComponent>) CraftMetaBook.class.getDeclaredField("pages").get(bookMeta);
                        } catch (ReflectiveOperationException ex) {
                            ex.printStackTrace();
                            pages = new ArrayList<>();
                        }
                        if (pages.size() > index - 1) {
                            pages.remove(index - 1);
                            book1.setItemMeta(bookMeta);
                        } else {
                            player.sendMessage(MessageEnum.AMOUNT_OVER_PAGE.getMessage());
                        }
                    } else {
                        player.sendMessage(MessageEnum.AMOUNT_MUST_BE_OVER_ZERO.getMessage());
                    }
                } else {
                     player.sendMessage(MessageEnum.AMOUNT_MUST_BE_OVER_ZERO.getMessage());
                }
                break;
            case TRIM:
                BookMeta bookMeta = (BookMeta) book1.getItemMeta();
                try {
                    pages = (List<IChatBaseComponent>) CraftMetaBook.class.getDeclaredField("pages").get(bookMeta);
                } catch (ReflectiveOperationException ex) {
                    ex.printStackTrace();
                    pages = new ArrayList<>();
                }
                List<IChatBaseComponent> buffPage = new ArrayList<>(pages);
                pages.clear();
                List<IChatBaseComponent> reversePages = new ArrayList<>();
                for (IChatBaseComponent page : new ArrayList<>(buffPage)) {
                    if (page.getText().equals("")) {
                        buffPage.remove(page);
                    } else {
                        reversePages = Lists.reverse(buffPage);
                        break;
                    }
                }
                for (IChatBaseComponent page : new ArrayList<>(reversePages)) {
                    if (page.getText().equals("")) {
                        buffPage.remove(page);
                    } else {
                        break;
                    }
                }
                pages.addAll(buffPage);
                book1.setItemMeta(bookMeta);
                break;
            case COMBINE:
                if (book2 != null) {
                    BookMeta bookMeta1 = (BookMeta) book1.getItemMeta();
                    BookMeta bookMeta2 = (BookMeta) book2.getItemMeta();
                    List<IChatBaseComponent> pages1;
                    List<IChatBaseComponent> pages2;

                    try {
                        pages1 = (List<IChatBaseComponent>) CraftMetaBook.class.getDeclaredField("pages").get(bookMeta1);
                        pages2 = (List<IChatBaseComponent>) CraftMetaBook.class.getDeclaredField("pages").get(bookMeta2);
                    } catch (ReflectiveOperationException ex) {
                        ex.printStackTrace();
                        pages1 = new ArrayList<>();
                        pages2 = new ArrayList<>();
                    }
                    for (IChatBaseComponent page : pages2) {
                        pages1.add(page);
                    }
                    book1.setItemMeta(bookMeta1);
                } else {
                    player.sendMessage(MessageEnum.BOOK_OFFHAND.getMessage());
                }
                break;
            case SETAUTHOR:
                if (amount_obj instanceof String) {
                    String author = (String) amount_obj;
                    bookMeta = (BookMeta) book1.getItemMeta();
                    bookMeta.setAuthor(author);
                    book1.setItemMeta(bookMeta);
                } else {
                    player.sendMessage(MessageEnum.OPTION_MUST_BE_STRING.getMessage());
                }
                break;
            case SETTITLE:
                if (amount_obj instanceof String) {
                    String author = (String) amount_obj;
                    bookMeta = (BookMeta) book1.getItemMeta();
                    bookMeta.setTitle(author);
                    book1.setItemMeta(bookMeta);
                } else {
                    player.sendMessage(MessageEnum.OPTION_MUST_BE_STRING.getMessage());
                }
                break;
            case UNSIGN:
                ItemStack book_new = new ItemStack(Material.BOOK_AND_QUILL);
                bookMeta = (BookMeta) book1.getItemMeta();
                book_new.setItemMeta(bookMeta);
                return book_new;
            case SETORIGINAL:
                bookMeta = (BookMeta) book1.getItemMeta();
                bookMeta.setGeneration(BookMeta.Generation.ORIGINAL);
                book1.setItemMeta(bookMeta);
                break;
            default:
                break;
        }
        return book1;
    }

}

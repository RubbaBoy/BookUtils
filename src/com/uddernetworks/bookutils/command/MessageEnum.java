package com.uddernetworks.bookutils.command;

import com.uddernetworks.bookutils.main.Main;
import org.bukkit.ChatColor;

public enum MessageEnum {

    HELP_BOOKEXPORT(ChatColor.translateAlternateColorCodes('&', Main.messages[0])),
    HELP_IMPORT(ChatColor.translateAlternateColorCodes('&', Main.messages[1])),
    HELP_YES_NO(ChatColor.translateAlternateColorCodes('&', Main.messages[2])),
    HELP_BOOKMODIFY(ChatColor.translateAlternateColorCodes('&', Main.messages[3])),
    HELP_PDF_OPTIONS(ChatColor.translateAlternateColorCodes('&', Main.messages[4])),
    PDF_SUCCESS(ChatColor.translateAlternateColorCodes('&', Main.messages[5])),
    PDF_FAIL(ChatColor.translateAlternateColorCodes('&', Main.messages[6])),
    TXT_SUCCESS(ChatColor.translateAlternateColorCodes('&', Main.messages[7])),
    TXT_FAIL(ChatColor.translateAlternateColorCodes('&', Main.messages[8])),
    FAILED_IMPORT(ChatColor.translateAlternateColorCodes('&', Main.messages[9])),
    SUCCESS_IMPORT(ChatColor.translateAlternateColorCodes('&', Main.messages[10])),
    AMOUNT_MUST_BE_OVER_ZERO(ChatColor.translateAlternateColorCodes('&', Main.messages[11])),
    OPTION_MUST_BE_STRING(ChatColor.translateAlternateColorCodes('&', Main.messages[12])),
    BOOK_OFFHAND(ChatColor.translateAlternateColorCodes('&', Main.messages[13])),
    AMOUNT_OVER_PAGE(ChatColor.translateAlternateColorCodes('&', Main.messages[14])),
    NEEDS_OPTION(ChatColor.translateAlternateColorCodes('&', Main.messages[15])),
    HELP_FONTSIZE(ChatColor.translateAlternateColorCodes('&', Main.messages[16])),
    PLAYERS_ONLY(ChatColor.translateAlternateColorCodes('&', Main.messages[17])),
    MUST_HOLD_BOOK(ChatColor.translateAlternateColorCodes('&', Main.messages[18])),
    UNSUPPORTED_FORMAT(ChatColor.translateAlternateColorCodes('&', Main.messages[19])),
    NO_PERMISSION(ChatColor.translateAlternateColorCodes('&', Main.messages[20])),
    TOO_MANY_PAGES(ChatColor.translateAlternateColorCodes('&', Main.messages[21]));

    String message;
    MessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
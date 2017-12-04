package com.uddernetworks.bookutils.main;

import com.uddernetworks.bookutils.command.BookExportCommand;
import com.uddernetworks.bookutils.command.BookImportCommand;
import com.uddernetworks.bookutils.command.BookModifyCommand;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    public static Plugin plugin;
    public static String[] messages = new String[22];

    @Override
    public void onEnable() {
        plugin = this;

        saveDefaultConfig();
        reloadConfig();

        messages[0] = getConfig().isSet("messages.help.bookexport") ? getConfig().getString("messages.help.bookexport") : "&cUsage: /bookexport <file_name> <format> [pdf options]";
        messages[1] = getConfig().isSet("messages.help.bookimport") ? getConfig().getString("messages.help.bookimport") : "&cUsage: /bookimport <file name with extension> <title> <author>";
        messages[2] = getConfig().isSet("messages.help.yes_or_no") ? getConfig().getString("messages.help.yes_or_no") : "&cThird and fourth argument must be &6yes &cor &6no";
        messages[3] = getConfig().isSet("messages.help.bookmodify") ? getConfig().getString("messages.help.bookmodify") : "&cUsage: /bookmodify <addpage|removepage|trim|combine|setauthor|settitle|unsign|setoriginal> [amount]";
        messages[4] = getConfig().isSet("messages.help.pdf_options") ? getConfig().getString("messages.help.pdf_options") : "&cPDF Option usage: &c/bookutils <file_name> pdf <print in color: yes|no> <font size>";
        messages[5] = getConfig().isSet("messages.info.pdf_success") ? getConfig().getString("messages.info.pdf_success") : "&aSuccessfully written to PDF!";
        messages[6] = getConfig().isSet("messages.info.pdf_fail") ? getConfig().getString("messages.info.pdf_fail") : "&cError writing to PDF!";
        messages[7] = getConfig().isSet("messages.info.txt_success") ? getConfig().getString("messages.info.txt_success") : "&aSuccessfully written to TXT file!";
        messages[8] = getConfig().isSet("messages.info.txt_fail") ? getConfig().getString("messages.info.txt_fail") : "&cError writing to TXT file";
        messages[9] = getConfig().isSet("messages.info.failed_import") ? getConfig().getString("messages.info.failed_import") : "&cFailed the import of %s";
        messages[10] = getConfig().isSet("messages.info.success_import") ? getConfig().getString("messages.info.success_import") : "&aSuccessfully imported %s";
        messages[11] = getConfig().isSet("messages.error.amount") ? getConfig().getString("messages.error.amount") : "&cError: Option value must be an integer over zero in this command";
        messages[12] = getConfig().isSet("messages.error.option_string") ? getConfig().getString("messages.error.option_string") : "&cError: Option must be a string";
        messages[13] = getConfig().isSet("messages.error.book_offhand") ? getConfig().getString("messages.error.book_offhand") : "&cError: You must have the book in your offhand to combine with the one in your main hand";
        messages[14] = getConfig().isSet("messages.error.amount_over_page") ? getConfig().getString("messages.error.amount_over_page") : "&cError: The page you want to remove must be less than or equal to the pages in the book";
        messages[15] = getConfig().isSet("messages.error.needs_option") ? getConfig().getString("messages.error.needs_option") : "&cError: This command requires an option of %s";
        messages[16] = getConfig().isSet("messages.error.fontsize") ? getConfig().getString("messages.error.fontsize") : "&cError: Fifth argument must be a whole integer above 0";
        messages[17] = getConfig().isSet("messages.error.players_only") ? getConfig().getString("messages.error.players_only") : "&cFor players only!";
        messages[18] = getConfig().isSet("messages.error.hold_book") ? getConfig().getString("messages.error.hold_book") : "&cYou must be holding a book for this command to work!";
        messages[19] = getConfig().isSet("messages.error.unsupported_format") ? getConfig().getString("messages.error.unsupported_format") : "&cError: Unsupported format!";
        messages[20] = getConfig().isSet("messages.error.no_permission") ? getConfig().getString("messages.error.no_permission") : "&cError: You are not permitted to do this command!";
        messages[21] = getConfig().isSet("messages.error.max_page_limit") ? getConfig().getString("messages.error.max_page_limit") : "&cError: You can not unsign books over 50 pages, because modifying them/resigning them causes clients to crash.";

        getCommand("bookexport").setExecutor(new BookExportCommand(this));
        getCommand("bookmodify").setExecutor(new BookModifyCommand(this));
        getCommand("bookimport").setExecutor(new BookImportCommand(this));
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        plugin = null;
    }

    public static Plugin getPlugin() {
        return plugin;
    }

}

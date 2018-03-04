package com.uddernetworks.bookutils.input;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import org.apache.commons.io.FileUtils;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftChatMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class TXTImport {


    private final String fileName;
    private final List<IChatBaseComponent> pages;

    public TXTImport(String fileName, List<IChatBaseComponent> pages) {
        this.fileName = fileName;
        this.pages = pages;
    }

    public boolean importFile() {
        try {
            File file = new File("plugins" + File.separator + "SavedBooks" + File.separator + fileName);
            if (!file.exists()) return false;
            String text = FileUtils.readFileToString(file, "UTF-8");
            ArrayList<String> list = addLinebreaks(text);
            pages.clear();
            for (String string : list) {
                IChatBaseComponent page = IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(new TextComponent(string)));
                pages.add(page);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public List<IChatBaseComponent> getPages() {
        return pages;
    }

    public static ArrayList<String> addLinebreaks(String input) {
        StringTokenizer tok = new StringTokenizer(input);
        ArrayList<String> output = new ArrayList<>();
        StringBuilder out = new StringBuilder();

        while (tok.hasMoreTokens()) {
            String word = tok.nextToken();
            if (word.length() >= 255) {
                out = new StringBuilder();
                output.add(word);
            } else {
                if (out.length() + word.length() <= 255) {
                    out.append(" ").append(word);
                } else {
                    output.add(out.toString());
                    out = new StringBuilder(word);
                }
            }
        }

        output.add(out.toString());

        return output;
    }

}

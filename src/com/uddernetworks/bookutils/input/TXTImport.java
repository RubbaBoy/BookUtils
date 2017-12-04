package com.uddernetworks.bookutils.input;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
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
        String out = "";
        while (tok.hasMoreTokens()) {
            String word = tok.nextToken();
            if (word.length() >= 255) {
                out = "";
                output.add(word);
            } else {
                if (out.length() + word.length() <= 255) {
                    out += " " + word;
                } else {
                    output.add(out);
                    out = word;
                }
            }
        }
        return output;
    }

}

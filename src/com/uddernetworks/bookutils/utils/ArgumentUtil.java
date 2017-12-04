package com.uddernetworks.bookutils.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArgumentUtil {

    public static String[] parseQuotes(String[] input) {
        StringBuilder builder = new StringBuilder();
        for (String value : input) {
            builder.append(value).append(" ");
        }
        return parseQuotes(builder.toString().trim());
    }

    public static String[] parseQuotes(String input) {
        ArrayList<String> quotes = new ArrayList<>();
        ArrayList<String> args = new ArrayList<>();
        Matcher m = Pattern.compile("\"([^\"]*)\"").matcher(input);
        while (m.find()) quotes.add(m.group(1));
        getArgs(input, quotes, args, 0, 0);
        return args.toArray(new String[args.size()]);
    }

    private static void getArgs(String full, ArrayList<String> quotes, ArrayList<String> args, int quotenum, int recursive_counter) {
        if (recursive_counter < 1000) {
            recursive_counter++;
            String temp = "";
            for (String string : full.split("\\s+")) {
                string = string.trim();
                if (!string.trim().startsWith("\"")) {
                    if (!string.trim().equals("")) {
                        args.add(string);
                        temp += " " + string;
                    }
                } else {
                    full = full.replace(temp.trim(), "").replace("\"" + quotes.get(quotenum) + "\"", "").trim();
                    args.add(quotes.get(quotenum));
                    quotenum++;
                    getArgs(full, quotes, args, quotenum, recursive_counter);
                    break;
                }
            }
        } else {
            System.err.println("Stopping method, it is probably recursive (Ran 1000+ times)");
        }
    }
}
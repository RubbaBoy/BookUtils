package com.uddernetworks.bookutils.filetype_managers;

import com.uddernetworks.bookutils.utils.IntegerUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PDFManager {

    private String name;
    private String text;
    private boolean printColor;
    private int fontSize;
    private static final String seperator = "§0#n§0";
    private static final String inLineChars = "§0#n";

    public PDFManager(String name, String text, boolean printColor, int fontSize) {
        this.name = name;
        this.text = text;
        this.printColor = printColor;
        this.fontSize = fontSize;
    }

    public PDFManager(String name) {
        this.name = name;
    }

    public boolean writePDF() {
        PDDocument doc = null;
        try {
            try {
                File parent = new File(name).getParentFile();
                if (!parent.exists()) parent.mkdir();

                doc = new PDDocument();
                PDPage page = new PDPage(PDRectangle.LETTER);
                doc.addPage(page);
                PDPageContentStream contentStream = new PDPageContentStream(doc, page);

                PDFont pdfFont = PDType1Font.TIMES_ROMAN;
                float leading = 1.5f * this.fontSize;

                PDRectangle mediabox = page.getMediaBox();
                float margin = 72;
                float width = mediabox.getWidth() - 2 * margin;
                float startX = mediabox.getLowerLeftX() + margin;
                float startY = mediabox.getUpperRightY() - margin;
                ArrayList<String> lines = new ArrayList<>();
                int lastSpace = -1;
                String text = this.text;
                text = text.replaceAll("§0\n", inLineChars); // §0newline§0
                text = text.replaceAll("\n", "§0#n§0");
                while (text.length() > 0) {
                    int spaceIndex = text.indexOf(' ', lastSpace + 1);
                    if (spaceIndex < 0) spaceIndex = text.length();
                    String subString = text.substring(0, spaceIndex);
                    if (!isValid(pdfFont, subString)) subString = makeValid(pdfFont, subString);
                    float size = this.fontSize * pdfFont.getStringWidth(subString) / 1000;
                    if (size > width) {
                        if (lastSpace < 0) lastSpace = spaceIndex;
                        subString = text.substring(0, lastSpace);

                        lines.add(subString);
                        text = text.substring(lastSpace).trim();
                        lastSpace = -1;
                    } else if (spaceIndex == text.length()) {
                        lines.add(text);
                        text = "";
                    } else {
                        lastSpace = spaceIndex;
                    }
                }

                ArrayList<String> line_clone = new ArrayList<>(lines);
                lines.clear();
                for (String line : line_clone) {
                    if (line.contains(seperator)) {
                        lines = doStuff(line.replaceAll("\n", "#n"), new ArrayList<>(lines));
                    } else {
                        lines.add(line);
                    }
                }

                int counter = 0;
                contentStream.beginText();
                contentStream.setFont(pdfFont, this.fontSize);
                contentStream.newLineAtOffset(startX, startY);
                for (String line : lines) {
                    if (counter < startY) {
                        counter += leading;
                        if (printColor) {
                            printColor(contentStream, pdfFont, line);
                        } else {
                            System.out.println(line);
                            contentStream.showText((isValid(pdfFont, strip(line)) ? strip(line) : makeValid(pdfFont, strip(line))));
                        }
                        contentStream.newLineAtOffset(0, -leading);
                    } else {
                        counter = 0;
                        contentStream.endText();
                        contentStream.close();

                        page = new PDPage(PDRectangle.LETTER);
                        doc.addPage(page);
                        contentStream = new PDPageContentStream(doc, page);
                        contentStream.beginText();
                        contentStream.setFont(pdfFont, this.fontSize);
                        contentStream.newLineAtOffset(startX, startY);

                        counter += leading;
                        if (printColor) {
                            printColor(contentStream, pdfFont, line);
                        } else {
                            contentStream.showText((isValid(pdfFont, strip(line)) ? strip(line) : makeValid(pdfFont, strip(line))));
                        }
                        contentStream.newLineAtOffset(0, -leading);
                    }
                }
                contentStream.endText();
                contentStream.close();

                doc.save(new File(this.name));
            } finally {
                if (doc != null) doc.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static String strip(String string) {
        return string.replaceAll(inLineChars, "").replaceAll(seperator, "");
    }

    private static ArrayList<String> doStuff(String line, ArrayList<String> original) {
        if (line.contains(seperator)) {
            String temp = line.substring(line.indexOf(seperator));
            String before = temp.substring(temp.indexOf(inLineChars)).replaceAll("\n", "#n");
            if (line.contains(seperator)) original.add(line.substring(0, line.indexOf(seperator)));
            for (int i = 0; i < howManyInline(temp); i++) {
                original.add("");
            }
            String going_add = before.substring(howManyInline(before) * 4 + 2, before.length());
            if (going_add.contains("#n")) {
                original = doStuff(going_add, new ArrayList<>(original));
            } else {
                original.add(going_add);
            }
            return original;
        }
        return original;
    }

    private static int howManyInline(String haystack) {
        String[] strs = haystack.replaceAll("\n", "#n").split("(?<=\\G.{4})");
        int ret = 0;
        for (int i = 0; i < strs.length; i++) {
            if (strs[i].equalsIgnoreCase(inLineChars.replaceAll("\n", "#n"))) {
                ret++;
            } else {
                i = strs.length;
            }
        }
        return ret;
    }

    private static void printColor(PDPageContentStream contentStream, PDFont pdfFont, String line) throws IOException {
        String[] split_by_chars = line.split("(?<=\\G.)");
        String previous = "";
        for (String character : split_by_chars) {
            boolean printprev = true;
            String prev_buff = "%%%%";
            if (previous.equalsIgnoreCase("§")) {
                Color color;
                switch ((IntegerUtil.isInteger(character, 10)) ? character : character.toLowerCase()) {
                    case "0":
                        color = new Color(0, 0, 0);
                        break;
                    case "1":
                        color = new Color(0, 0, 170);
                        break;
                    case "2":
                        color = new Color(0, 170, 0);
                        break;
                    case "3":
                        color = new Color(0, 170, 170);
                        break;
                    case "4":
                        color = new Color(170, 0, 0);
                        break;
                    case "5":
                        color = new Color(170, 0, 170);
                        break;
                    case "6":
                        color = new Color(255, 170, 0);
                        break;
                    case "7":
                        color = new Color(170, 170, 170);
                        break;
                    case "8":
                        color = new Color(85, 85, 85);
                        break;
                    case "9":
                        color = new Color(85, 85, 255);
                        break;
                    case "a":
                        color = new Color(85, 255, 85);
                        break;
                    case "b":
                        color = new Color(85, 255, 255);
                        break;
                    case "c":
                        color = new Color(255, 85, 85);
                        break;
                    case "d":
                        color = new Color(255, 85, 255);
                        break;
                    case "e":
                        color = new Color(255, 255, 85);
                        break;
                    case "f":
                        color = new Color(255, 255, 255);
                        break;
                    default:
                        color = null;
                        break;
                }
                if (color != null) {
                    contentStream.setNonStrokingColor(color);
                    prev_buff = "";
                    printprev = false;
                }
            }
            if (printprev) contentStream.showText(previous);
            previous = (prev_buff.equalsIgnoreCase("%%%%") ? character : prev_buff);
        }
        contentStream.showText(previous);
    }

    public String readPDF() throws IOException {
        File file = new File("plugins" + File.separator + "SavedBooks" + File.separator + this.name);
        if (!file.exists()) return null;

        new File("plugins", "SavedBooks").mkdirs();
        try (PDDocument document = PDDocument.load(file)) {

            PDFTextStripper pdfStripper = new PDFTextStripper();

            return pdfStripper.getText(document);
        }
    }

    private static boolean isValid(PDFont font, String character) {
        try {
            font.getStringWidth(character);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static String makeValid(PDFont font, String string) {
        StringBuilder ret = new StringBuilder();
        for (char character : string.toCharArray()) {
            if (isValid(font, String.valueOf(character))) {
                ret.append(character);
            } else {
                ret.append(" ");
            }
        }
        return ret.toString();
    }
}

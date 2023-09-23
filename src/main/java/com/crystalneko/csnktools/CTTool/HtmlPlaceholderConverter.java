package com.crystalneko.csnktools.CTTool;

import com.crystalneko.csnktools.CSNKTools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HtmlPlaceholderConverter {
    public String convertedHtml;
    private CSNKTools plugin;
    public HtmlPlaceholderConverter(CSNKTools plugin) {
        this.plugin = plugin;
    }

    public void startConverter(String[] placeholder,String[] replacement,String htmlpath) {
        String htmlFilePath = htmlpath;
        String[] placeholders = placeholder;
        String[] replacements = replacement;

        try {
            convertedHtml = replacePlaceholders(htmlFilePath, placeholders, replacements);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static String replacePlaceholders(String htmlFilePath, String[] placeholders, String[] replacements) throws IOException {
        String htmlContent = readHtmlFile(htmlFilePath);

        for (int i = 0; i < placeholders.length; i++) {
            String placeholder = placeholders[i];
            String replacement = replacements[i];
            htmlContent = htmlContent.replace(placeholder, replacement);
        }

        return htmlContent;
    }

    private static String readHtmlFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append(System.lineSeparator());
        }

        reader.close();
        return content.toString();
    }
}
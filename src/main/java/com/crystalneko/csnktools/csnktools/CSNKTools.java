package com.crystalneko.csnktools.csnktools;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CSNKTools extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getConsoleSender().sendMessage(
                "//--------------| |---------=---------|\n" +
                "||--------------| |---------=---------|\n" +
                "||\t\t\t   | |\n" +
                "||\t\t\t   | |\n" +
                "||\t\t\t   | |\n" +
                "||\t\t\t   | |\n" +
                "||\t\t\t   | |\n" +
                "||\t\t\t   | |\n" +
                "||--------------|\t   | |\n" +
                "\\\\--------------|\t   ---" + "\nCSNKTools V0.0.1 作者:CrystalNeko");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getConsoleSender().sendMessage(
                "//--------------| |---------=---------|\n" +
                        "||--------------| |---------=---------|\n" +
                        "||\t\t\t   | |\n" +
                        "||\t\t\t   | |\n" +
                        "||\t\t\t   | |\n" +
                        "||\t\t\t   | |\n" +
                        "||\t\t\t   | |\n" +
                        "||\t\t\t   | |\n" +
                        "||--------------|\t   | |\n" +
                        "\\\\--------------|\t   ---" + "\nCSNKTools V0.0.1 作者:CrystalNeko");
    }
}

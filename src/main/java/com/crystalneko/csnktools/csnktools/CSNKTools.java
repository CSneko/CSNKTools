package com.crystalneko.csnktools.csnktools;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CSNKTools extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getConsoleSender().sendMessage(
                "____________________________________\n" +
                        "|\n" +
                        "|  //----------| |-------------|\n" +
                        "|  ||----------| |-------------|\n" +
                        "|  ||\t\t       ||\n" +
                        "|  ||\t\t       ||\n" +
                        "|  ||\t\t       ||\n" +
                        "|  ||\t\t       ||\n" +
                        "|  ||----------|      ||\n" +
                        "|  \\\\----------|      --\n" +
                        "|\n" +
                        "|CSNKTools V0.0.1 作者:CrystalNeko\n" +
                        "____________________________________");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getConsoleSender().sendMessage(
                "____________________________________\n" +
                        "|\n" +
                        "|  //----------| |-------------|\n" +
                        "|  ||----------| |-------------|\n" +
                        "|  ||\t\t       ||\n" +
                        "|  ||\t\t       ||\n" +
                        "|  ||\t\t       ||\n" +
                        "|  ||\t\t       ||\n" +
                        "|  ||----------|      ||\n" +
                        "|  \\\\----------|      --\n" +
                        "|\n" +
                        "|CSNKTools V0.0.1 作者:CrystalNeko\n" +
                        "____________________________________");
    }
}

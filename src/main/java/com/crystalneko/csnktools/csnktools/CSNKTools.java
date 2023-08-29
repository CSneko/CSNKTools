package com.crystalneko.csnktools.csnktools;

import com.crystalneko.csnktools.csnktools.CTTool.CTScoreboard;
import com.crystalneko.csnktools.csnktools.CTTool.loginmsg;
import com.crystalneko.csnktools.csnktools.CTcommand.csnktools;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;

public final class CSNKTools extends JavaPlugin {
    private File configFile;
    private FileConfiguration config;
    private loginmsg loginMsgListener;
    @Override
    public void onEnable() {
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
        // 创建一个名为config.yml的配置文件
        configFile = new File(getDataFolder(), "Config.yml");
        // 获取配置文件实例
        config = getConfig();
        // 如果配置文件不存在，则从插件资源中复制默认的配置文件
        if (!configFile.exists()) {
            saveResource("Config.yml", false);
        }

        //判断插件配置是否启用
        if (config.getBoolean("Enable")) {
            //加载类readconfig
            readconfig();
        } else{
            Bukkit.getConsoleSender().sendMessage("[CT]插件为禁用状态");
        }

    }
    public void readconfig() {
        Bukkit.getConsoleSender().sendMessage("[CT]插件为启用状态，开始加载配置");
        //注册命令
        getCommand("csnktools").setExecutor(new csnktools());
        //加载配置登陆提示语
        if (getConfig().getBoolean("player.join.Enable")){
            // 注册监听器
            loginMsgListener = new loginmsg();
            getServer().getPluginManager().registerEvents(loginMsgListener, this);
            loginMsgListener.loadConfig();
        }
        // 加载配置计分板
        if (getConfig().getBoolean("Scoreboard.Enable")) {
            CTScoreboard ctscoreboard = new CTScoreboard(this);
            getServer().getPluginManager().registerEvents(ctscoreboard, this);

            // 玩家加入服务器时创建计分板
            getServer().getPluginManager().registerEvents(new PlayerJoinListener(ctscoreboard), this);
        }


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

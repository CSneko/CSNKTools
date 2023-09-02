package com.crystalneko.csnktools.csnktools;

import com.crystalneko.csnktools.csnktools.CTTool.CTScoreboard;
import com.crystalneko.csnktools.csnktools.CTTool.loginmsg;
import com.crystalneko.csnktools.csnktools.CTcommand.csnktools;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scheduler.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class CSNKTools extends JavaPlugin implements Listener {
    private File configFile;
    private BukkitScheduler scheduler;
    private FileConfiguration config;
    private loginmsg loginMsgListener;
    private CTScoreboard CTScoreboardListener;
    private final Map<Player, Scoreboard> playerScoreboards = new HashMap<>();
    private FileConfiguration languageConfig;
    private String language;


    @Override
    public void onEnable() {
        int pluginId = 19702; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);
        // 创建一个名为config.yml的配置文件
        configFile = new File(getDataFolder(), "config.yml");
        // 获取配置文件实例
        config = getConfig();
        // 如果配置文件不存在，则从插件资源中复制默认的配置文件
        if (!configFile.exists()) {
            saveResource("Config.yml", false);
        }
        // 检查并修复配置文件名的大小写
        String configFileName = "config.yml";
        File configFile = new File(getDataFolder(), configFileName);
        File oldConfigFile = new File(getDataFolder(), "Config.yml");

        if (oldConfigFile.exists() && !configFile.exists()) {
            oldConfigFile.renameTo(configFile);
        }
        // 创建语言文件
        createLanguageFiles();

        // 加载语言文件
        loadLanguageFile();
        //加载消息
        String enableplugin = getMessage("Console.enable");
        Bukkit.getConsoleSender().sendMessage(enableplugin);
        // 判断插件配置是否启用
        if (config.getBoolean("Enable")) {
            // 加载类readconfig
            readconfig();
        } else {
            String disableplugin = getMessage("Console.disable");
            Bukkit.getConsoleSender().sendMessage(disableplugin);
        }
    }

    public void readconfig() {
        String loadingplugin = getMessage("Console.loading");
        Bukkit.getConsoleSender().sendMessage(loadingplugin);
        // 注册命令
        getCommand("csnktools").setExecutor(new csnktools(this));
        // 加载配置登陆提示语
        if (getConfig().getBoolean("player.join.Enable")) {
            // 注册监听器
            loginMsgListener = new loginmsg();
            getServer().getPluginManager().registerEvents(loginMsgListener, this);
            loginMsgListener.loadConfig();
        }
        //加载计分板
        if (getConfig().getBoolean("Scoreboard.Enable")) {

            // 注册监听器
            CTScoreboardListener = new CTScoreboard(getConfig(), this);
            getServer().getPluginManager().registerEvents(CTScoreboardListener,this);
            CTScoreboardListener.loadConfig();
        }


    }

    private void createLanguageFiles() {
        String[] languageFiles = {"zh-cn.yml" ,"en-eu.yml", "more.yml"};
        File languageFolder = new File(getDataFolder(), "language");

        if (!languageFolder.exists()) {
            languageFolder.mkdirs();
        }

        for (String fileName : languageFiles) {
            File languageFile = new File(languageFolder, fileName);
            if (!languageFile.exists()) {
                saveResource("language/" + fileName, false);
            }
        }
    }


    private void loadLanguageFile() {
        // 获取配置文件中的语言选项
        language = getConfig().getString("language");

        // 根据语言选项加载对应的语言文件
        File languageFile = new File(getDataFolder(),  "language/"+ language + ".yml");
        if (!languageFile.exists()) {
            saveResource("language/" +language + ".yml", false);
        }

        languageConfig = YamlConfiguration.loadConfiguration(languageFile);
    }

    // 获取翻译内容的方法
    public String getMessage(String key) {
        return languageConfig.getString(key);
    }




    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("____________________________________\n" +
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
                        "|CSNKTools V0.0.4 作者:CrystalNeko\n" +
                        "____________________________________");
    }

}

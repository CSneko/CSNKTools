package com.crystalneko.csnktools.csnktools;

import com.crystalneko.csnktools.csnktools.CTTool.CTScoreboard;
import com.crystalneko.csnktools.csnktools.CTTool.Music;
import com.crystalneko.csnktools.csnktools.CTTool.loginmsg;
import com.crystalneko.csnktools.csnktools.CTcommand.csnktools;
import com.crystalneko.csnktools.csnktools.CTcommand.csnktoolsTabCompleter;

import org.bukkit.Bukkit;
import org.bukkit.Note;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scheduler.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
/*import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;*/


public final class CSNKTools extends JavaPlugin implements Listener {

    private String pluginVersion;
    private File configFile;
    private FileConfiguration config;
    private loginmsg loginMsgListener;
    private CTScoreboard CTScoreboardListener;
    private Music musicListener;
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

        //加载配置
        createConfigFile();

        //检查配置
        //checkConfigFile();

        // 创建语言文件
        createLanguageFiles();

        // 获取插件版本
        pluginVersion = getDescription().getVersion();

        // 保存插件版本到配置文件
        savePluginVersionToConfig();


        // 加载语言文件
        loadLanguageFile();



        //加载消息
        String enableplugin = getMessage("Console.enable");
        Bukkit.getConsoleSender().sendMessage(enableplugin);
        //检查更新
        checkUpdates();
        // 加载类readconfig
        readconfig();
    }

    public void readconfig() {
        //检查PAPI是否启用
        Boolean papienable = (isPluginLoaded("PlaceholderAPI"));
        if(papienable) {}else{
            String NOPAPI = getMessage("Error.NOPAPI");
            Bukkit.getConsoleSender().sendMessage(NOPAPI);
        }
        //检查NBAPI是否启用
        Boolean nbapienable = (isPluginLoaded("NoteBlockAPI"));
        if(nbapienable) {}else{
            String NONBAPI = getMessage("Error.NONBAPI");
            Bukkit.getConsoleSender().sendMessage(NONBAPI);
        }

        // 注册命令执行器
        getCommand("csnktools").setExecutor(new csnktools(this,nbapienable));
        // 注册 Tab 补全
        getCommand("csnktools").setTabCompleter(new csnktoolsTabCompleter());


        String loadingplugin = getMessage("Console.loading");
        Bukkit.getConsoleSender().sendMessage(loadingplugin);
        // 注册命令
        getCommand("csnktools").setExecutor(new csnktools(this,nbapienable));
        // 加载配置登陆提示语
        if (getConfig().getBoolean("player.join.Enable")) {

            // 注册监听器
            loginMsgListener = new loginmsg();
            getServer().getPluginManager().registerEvents(loginMsgListener, this);
            loginMsgListener.loadConfig(papienable);
        }
        //加载计分板
        if (getConfig().getBoolean("Scoreboard.Enable")) {

            // 注册监听器
            CTScoreboardListener = new CTScoreboard(this,papienable);
            getServer().getPluginManager().registerEvents(CTScoreboardListener,this);
            CTScoreboardListener.lloadConfig();
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
        File languageFile = new File(getDataFolder(), "language/" + language + ".yml");
        if (!languageFile.exists()) {
            saveResource("language/" + language + ".yml", false);
        }

        languageConfig = YamlConfiguration.loadConfiguration(languageFile);

        // 替换插件版本占位符
        String versionPlaceholder = "%version%";
        String translatedVersion = languageConfig.getString("plugin-version");
        if (translatedVersion != null) {
            translatedVersion = translatedVersion.replace(versionPlaceholder, pluginVersion);
            languageConfig.set("plugin-version", translatedVersion);
        }
    }

    // 获取翻译内容的方法
    public String getMessage(String key) {
        return languageConfig.getString(key);
    }


    public void checkUpdates() {
        String remoteUrl = "https://w.csk.asia/res/version.txt"; // 远程网站中存储版本号的文件地址

        try {
            URL url = new URL(remoteUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String remoteVersion = reader.readLine();

                reader.close();

                // 将远程版本号与插件版本进行比较
                String pluginVersion = getDescription().getVersion(); // 获取插件的版本号
                if (!pluginVersion.equals(remoteVersion)) {
                    // 版本不同，发出更新提醒
                    String checkupdate_update = getMessage("System.check-update.update");
                    getLogger().info(checkupdate_update);
                } else {
                    // 版本相同，无需更新
                    String checkupdate_isupdate = getMessage("System.check-update.is-update");
                    getLogger().info(checkupdate_isupdate);
                }
            } else {
                // 请求失败
                String checkupdate_warning = getMessage("System.check-update.warning");
                getLogger().warning(checkupdate_warning + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            // 发生异常
            String checkupdate_warning2 = getMessage("System.check-update.warning2");
            getLogger().warning(checkupdate_warning2 + e.getMessage());
        }
    }

    public void checkUpdates2(Player player) {
        String remoteUrl = "https://w.csk.asia/res/version.txt"; // 远程网站中存储版本号的文件地址

        try {
            URL url = new URL(remoteUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String remoteVersion = reader.readLine();
                reader.close();

                // 将远程版本号与插件版本进行比较
                String pluginVersion = getDescription().getVersion(); // 获取插件的版本号
                if (!pluginVersion.equals(remoteVersion)) {
                    // 版本不同，发出更新提醒
                    String checkupdate_update = getMessage("System.check-update.update");
                    if (checkupdate_update != null) {
                        player.sendMessage(checkupdate_update);
                    }
                } else {
                    // 版本相同，无需更新
                    String checkupdate_isupdate = getMessage("System.check-update.is-update");
                    if (checkupdate_isupdate != null) {
                        player.sendMessage(checkupdate_isupdate);
                    }
                }
            } else {
                // 请求失败
                String checkupdate_warning = getMessage("System.check-update.warning");
                player.sendMessage(checkupdate_warning + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            // 发生异常
            String checkupdate_warning2 = getMessage("System.check-update.warning2");
            player.sendMessage(checkupdate_warning2 + e.getMessage());
        }
    }



    public void createConfigFile(){
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
    }

    private void savePluginVersionToConfig() {
        FileConfiguration config = getConfig();
        config.set("plugin-version", pluginVersion);
        saveConfig();
    }

    //检查特定插件是否启用
    private boolean isPluginLoaded(String pluginName) {
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        Plugin targetPlugin = pluginManager.getPlugin(pluginName);
        if (targetPlugin != null && targetPlugin.isEnabled()) {
            return true;
        }
        return false;
    }

    //这一段无法正常运行，因此被注释了
  /*  private void checkConfigFile() {
        //---------------------------------------------------------------------------------------
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
        //---------------------------------------------------------------------------------------
        String sourceFileName = "config.yml";
        String targetFileName = "newconfig.yml";

        // 获取插件的数据文件夹路径
        File dataFolder = getDataFolder();

        // 创建源文件和目标文件对象
        File sourceFile = new File(dataFolder, sourceFileName);
        File targetFile = new File(dataFolder, targetFileName);

        try {
            // 复制文件
            Files.copy(sourceFile.toPath(), targetFile.toPath());

            System.out.println("文件复制成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //----------------------------------------------------------------------------------------

        // 创建配置文件对象
        File dconfigFile = new File(getDataFolder(), "config.yml");

        // 检查配置文件是否存在
        if (dconfigFile.exists()) {
            // 删除配置文件
            boolean deleted = dconfigFile.delete();

            // 检查删除操作是否成功
            if (deleted) {
                getLogger().info("配置文件已成功删除。");
            } else {
                getLogger().warning("删除配置文件失败。");
            }
        } else {
            getLogger().warning("配置文件不存在。");
        }

        //----------------------------------------------------------------------------------------------

        // 创建一个名为config.yml的配置文件
        configFile = new File(getDataFolder(), "config.yml");
        // 获取配置文件实例
        config = getConfig();
        // 如果配置文件不存在，则从插件资源中复制默认的配置文件
        if (!configFile.exists()) {
            saveResource("Config.yml", false);
        }
        // 检查并修复配置文件名的大小写
        String configFileName11 = "config.yml";
        File configFile11 = new File(getDataFolder(), configFileName11);
        File oldConfigFile11 = new File(getDataFolder(), "Config.yml");
        if (oldConfigFile11.exists() && !configFile11.exists()) {
            oldConfigFile11.renameTo(configFile11);
        }

        //---------------------------------------------------------------------------------------------------------

        File configFile1 = new File(getDataFolder(), "config.yml");
        File configFile2 = new File(getDataFolder(), "newconfig.yml");
        // 加载配置文件1
        FileConfiguration config1 = YamlConfiguration.loadConfiguration(configFile1);

        // 加载配置文件2
        FileConfiguration config2 = YamlConfiguration.loadConfiguration(configFile2);

        // 遍历配置文件2的所有键值对，将它们合并到配置文件1中
        for (String key : config2.getKeys(true)) {
            if (!config1.contains(key)) {
                // 如果配置文件1中没有该键，则将键值对添加到配置文件1中
                config1.set(key, config2.get(key));
            } else {
                // 如果配置文件1中已经存在该键，则使用配置文件2中的值覆盖配置文件1中的值
                config1.set(key, config2.get(key));
            }
        }

        try {
            // 保存合并后的配置文件1
            config1.save(configFile1);
            getLogger().info("成功合并并保存配置文件1.");
        } catch (IOException e) {
            getLogger().warning("无法保存合并后的配置文件1.");
            e.printStackTrace();
        }




    }*/







    @Override
    public void onDisable() {
        //清除缓存
        File tempfolder = new File("plugins/CSNKTools/temp");
        deleteFolder(tempfolder);

        getMessage("Console.enable");
        Bukkit.getConsoleSender().sendMessage();
    }

    //清除缓存
    public static void deleteFolder(File folder) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteFolder(file);
                }
            }
        }
        folder.delete();
    }
}

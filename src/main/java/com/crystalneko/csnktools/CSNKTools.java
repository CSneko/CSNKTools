package com.crystalneko.csnktools;


import com.crystalneko.csnktools.CTTool.*;
import com.crystalneko.csnktools.CTcommand.csnktools;
import com.crystalneko.csnktools.CTcommand.csnktoolsTabCompleter;
import com.crystalneko.csnktools.CTcommand.csnktoolsadmin;
import com.crystalneko.csnktools.CTcommand.download;
import com.crystalneko.csnktools.sql.PlayerJoinListener;
import com.crystalneko.csnktools.website.LoginServlet;
import com.crystalneko.csnktools.website.UserServlet;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public final class CSNKTools extends JavaPlugin implements Listener {


    private File configFile;
    private FileConfiguration config;
    private loginmsg loginMsgListener;
    private CTScoreboard CTScoreboardListener;
    private FileConfiguration languageConfig;
    private String language;
    private mysqlandemail mysqlAndemail;
    private mysqlandemail2 mysqlAndemail2;
    private serverendisable serverendisaBle;
    private HtmlPlaceholderConverter htmlPlaceholderConverter;
    private sqlite sqLite;
    private PlayerJoinListener playerJoinListener;



    @Override
    public void onEnable() {
        int pluginId = 19702; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);
        // 判断ctLib插件是否启用
        download.checkAndDownloadPlugin("ctLib","https://w.csk.asia/plugins/ctLib.jar");
        //复制资源文件
        copyresourceFiles();
        //加载配置
        createConfigFile();
        // 加载语言文件
        loadLanguageFile();
        //加载消息
        String enableplugin = getMessage("Console.enable");
        Bukkit.getConsoleSender().sendMessage(enableplugin);
        //检查更新
        checkUpdates();
        //创建必要的东西
        createNecessary();
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
        if(nbapienable) {}else {
            String NONBAPI = getMessage("Error.NONBAPI");
            Bukkit.getConsoleSender().sendMessage(NONBAPI);
        }
//------------------------------------------------------------------------初始化---------------------------------------------------------------------

        //初始化mysql
        mysqlAndemail2 = new mysqlandemail2(this);
        //初始化占位符转换器
        htmlPlaceholderConverter = new HtmlPlaceholderConverter(this);
        //初始化email
        mysqlAndemail = new mysqlandemail(this,htmlPlaceholderConverter);
        //初始化sqlite
        sqLite = new sqlite(this);
        //初始化监听器PlayerJoinListen(com.crystalenko.csnktools.sql.PlayerJoinListener)
        playerJoinListener = new PlayerJoinListener(sqLite,this);


//------------------------------------------------------------------------监听器---------------------------------------------------------------------

        // 注册监听器PlayerJoinListen(com.crystalenko.csnktools.sql.PlayerJoinListener)
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(sqLite,this), this);

//------------------------------------------------------------------------加载配置---------------------------------------------------------------------

        // 注册命令执行器
        getCommand("csnktools").setExecutor(new csnktools(this,nbapienable,mysqlAndemail,htmlPlaceholderConverter));
        getCommand("csnktoolsadmin").setExecutor(new csnktoolsadmin(this));
        // 注册 Tab 补全
        getCommand("csnktools").setTabCompleter(new csnktoolsTabCompleter());
        getCommand("csnktoolsadmin").setTabCompleter(new csnktoolsTabCompleter());

        //插件加载提示语
        String loadingplugin = getMessage("Console.loading");
        Bukkit.getConsoleSender().sendMessage(loadingplugin);



        //加载mysql
        if(getConfig().getBoolean("mysql.Enable")){
            mysqlAndemail2.loadConfig();
        }

        //加载email
        if (getConfig().getBoolean("smtp.Enable")){
            mysqlAndemail.loadConfig();
            //加载启动邮件提示
            if(getConfig().getBoolean("serverendisable.enable")){
                serverendisaBle = new serverendisable(mysqlAndemail,this,mysqlAndemail2,sqLite);
                serverendisaBle.serverenable();
            }
        }


        //加载网页
        if (getConfig().getBoolean("website.Enable")){
            String table_name = getConfig().getString("website.user_table");
            //创建必要的表和列
            mysqlAndemail2.createColumn(table_name,"username","VARCHAR(255)");
            mysqlAndemail2.createColumn(table_name,"password","VARCHAR(255)");
            mysqlAndemail2.createColumn(table_name,"ip","VARCHAR(255)");
            mysqlAndemail2.createColumn(table_name,"regip","VARCHAR(255)");
            mysqlAndemail2.createColumn(table_name,"email","VARCHAR(255)");
            mysqlAndemail2.createColumn(table_name,"permission","VARCHAR(255)");
            mysqlAndemail2.createColumn(table_name,"logged","VARCHAR(255)");
            mysqlAndemail2.create_table(table_name,"username VARCHAR(255),password VARCHAR(255),ip VARCHAR(255),regip VARCHAR(255),email VARCHAR(255),permission VARCHAR(255),logged VARCHAR(255)");
            LoginServlet loginServlet = new LoginServlet(this, mysqlAndemail, mysqlAndemail2);
            UserServlet userServlet = new UserServlet(htmlPlaceholderConverter,mysqlAndemail2,this,sqLite);
            Thread jettyThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 创建嵌入式Web服务器
                        try {
                           // 获取服务器的端口号
                            int port = getConfig().getInt("website.port");

                            // 实例化Jetty服务器
                            Server server = new Server(port);

// 创建ServletContextHandler templateservlet，并设置上下文路径
                            ServletContextHandler userservlet = new ServletContextHandler(ServletContextHandler.SESSIONS);
                            userservlet.setContextPath("/user");
                            userservlet.addServlet(new ServletHolder(userServlet), "/*");

// 创建ServletContextHandler loginservlet，并设置上下文路径
                            ServletContextHandler loginservlet = new ServletContextHandler(ServletContextHandler.SESSIONS);
                            loginservlet.setContextPath("/login");
                            loginservlet.addServlet(new ServletHolder(loginServlet), "/*");



                            // 配置静态资源处理器
                            ResourceHandler resourceHandler = new ResourceHandler();
                            String resourceBase = System.getProperty("user.dir") + "/plugins/CSNKTools/website/";
                            resourceHandler.setResourceBase(resourceBase);
                            // 可显示目录结构
                            resourceHandler.setDirectoriesListed(getConfig().getBoolean("website.dirlist"));

                            // 创建处理器集合
                            HandlerCollection handlers = new HandlerCollection();
                            handlers.setHandlers(new Handler[]{resourceHandler, loginservlet,userservlet});

                            server.setHandler(handlers);

                            // 启动服务器
                            server.start();
                            server.join();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            jettyThread.start();
        }



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



    private void loadLanguageFile() {
        // 获取配置文件中的语言选项
        language = getConfig().getString("language");

        // 根据语言选项加载对应的语言文件
        File languageFile = new File(getDataFolder(), "language/" + language + ".yml");
        if (!languageFile.exists()) {
            saveResource("language/" + language + ".yml", false);
        }

        languageConfig = YamlConfiguration.loadConfiguration(languageFile);
    }

    // 获取翻译内容的方法
    public String getMessage(String key) {
        return languageConfig.getString(key);
    }


    public void checkUpdates() {
        String remoteUrl = "https://w.csk.asia/res/version/CSNKTools.txt"; // 远程网站中存储版本号的文件地址

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
        String remoteUrl = "https://w.csk.asia/res/version/CSNKTools.txt"; // 远程网站中存储版本号的文件地址

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


    //检查特定插件是否启用
    private boolean isPluginLoaded(String pluginName) {
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        Plugin targetPlugin = pluginManager.getPlugin(pluginName);
        if (targetPlugin != null && targetPlugin.isEnabled()) {
            return true;
        } else {
            return false;
        }
    }
    //复制资源文件
    private void copyresourceFiles() {
        // 创建目标文件夹
        File targetFolder = new File(getDataFolder().getParentFile(), "CSNKTools");
        if (!targetFolder.exists()) {
            targetFolder.mkdirs();
        }

        // 保存资源文件到目标文件夹
        checkAndSaveResource("email/serverdisable.html");
        checkAndSaveResource("email/serverenable.html");
        checkAndSaveResource("email/feedback.html");
        checkAndSaveResource("website/index.html");
        checkAndSaveResource("website/login.html");
        checkAndSaveResource("website/register.html");
        checkAndSaveResource("website/user/user.html");
        checkAndSaveResource("data/data.db");
        saveResource("language/zh-cn.yml", true);
        saveResource("language/en-eu.yml", true);
    }
    private void checkAndSaveResource(String filePath) {
        if (!isFileExists(filePath)) {
            saveResource(filePath, false);
        } else {
        }
    }

    private boolean isFileExists(String filePath) {
        File file = new File(getDataFolder(), filePath);
        return file.exists() && file.isFile();
    }

    @Override
    public void onDisable() {
        //清除缓存
        File tempfolder = new File("plugins/CSNKTools/temp");
        deleteFolder(tempfolder);
        if (getConfig().getBoolean("smtp.Enable")){
            //加载关闭邮件提示
            if(getConfig().getBoolean("serverendisable.enable")){
                serverendisaBle.serverdisable();
            }
        }

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
    public static void createNecessary(){
        createPath("plugins/CSNKTools/music/");
    }
    public static void createPath(String path){
        String directoryPath = path;
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            boolean isCreated = directory.mkdirs();
        } else {
            System.out.println("Directory already exists");
        }
    }
}

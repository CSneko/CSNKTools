package com.crystalneko.csnktools.csnktools;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import com.crystalneko.csnktools.csnktools.CTTool.loginmsg;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandExecutor;
public final class CSNKTools extends JavaPlugin {
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
        //创建配置变量
        FileConfiguration config = this.getConfig();
        //创建默认配置
            //插件总开关
        config.addDefault("Enable", true);
        config.options().copyDefaults(true);
        saveConfig();
            //登陆提示语开关
        config.addDefault("player.join.Enable", true);
        config.options().copyDefaults(true);
        saveConfig();
            //登陆提示语
        config.addDefault("player.join.Message", "欢迎%player_name%加入服务器");
        config.options().copyDefaults(true);
        saveConfig();
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
        getCommand("csnktools").setExecutor(new com.crystalneko.csnktools.csnktools.CTcommand.csnktools());
        //加载配置登陆提示语
        if (getConfig().getBoolean("player.join.Enable")){
            // 注册监听器
            loginMsgListener = new loginmsg();
            getServer().getPluginManager().registerEvents(loginMsgListener, this);
            loginMsgListener.loadConfig();
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

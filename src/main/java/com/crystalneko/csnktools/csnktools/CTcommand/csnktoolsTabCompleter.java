package com.crystalneko.csnktools.csnktools.CTcommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class csnktoolsTabCompleter implements TabCompleter {
    //获取配置中某个键的子键(尽管目前还没用到)
    public static String[] getSubKeys(FileConfiguration config, String key) {
        ConfigurationSection section = config.getConfigurationSection(key);

        if (section == null) {
            return new String[0]; // 若不存在该键，则返回空数组
        }

        return section.getKeys(false).toArray(new String[0]);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("csnktools")) {
            if (args.length == 1) { // 补全第一个参数
                completions.add("help");
                completions.add("about");
                completions.add("check");
                completions.add("playsound");
                completions.add("soundurl");
                completions.add("feedback");
            }

        }
        if (command.getName().equalsIgnoreCase("csnktoolsadmin")) {
            if (args.length == 1) { // 补全第一个参数
                completions.add("help");
                completions.add("download");
            }

        }


        return completions;
    }
}

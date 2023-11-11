package com.crystalneko.csnktools.CTcommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            }if(args.length == 2 && args[1].equalsIgnoreCase("playsound")){//补全曲目
                return getMusic();
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
    public List<String> getMusic(){
        String directoryPath = "plugins/CSNKTools/music";
        List<String> fileNames = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {
            fileNames = paths
                    .filter(Files::isRegularFile) // 是文件，不是文件夹
                    .map(Path::toFile)
                    .filter(file -> file.getName().endsWith(".nbs")) // 文件扩展名为.nbs
                    .map(file -> {
                        String fileNameWithExtension = file.getName();
                        return fileNameWithExtension.substring(0, fileNameWithExtension.lastIndexOf(".")); // 去掉文件扩展名
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileNames;
    }
}

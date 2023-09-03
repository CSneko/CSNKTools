package com.crystalneko.csnktools.csnktools.CTcommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class csnktoolsTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("csnktools")) {
            if (args.length == 1) { // 补全第一个参数
                completions.add("help");
                completions.add("about");
                completions.add("check");
            }
        }

        return completions;
    }
}

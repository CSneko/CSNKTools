package com.crystalneko.csnktools.CTcommand;

import com.crystalneko.csnktools.CTTool.mysqlandemail;
import com.crystalneko.csnktools.CSNKTools;
import com.crystalneko.csnktools.CTTool.HtmlPlaceholderConverter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class feedback {
    private CSNKTools plugin;
    private mysqlandemail email;
    private HtmlPlaceholderConverter htmlPlaceholderConverter;

    public feedback(CSNKTools plugin,mysqlandemail email,HtmlPlaceholderConverter htmlPlaceholderConverter){
        this.plugin = plugin;
        this.email = email;
        this.htmlPlaceholderConverter = htmlPlaceholderConverter;

    }
    public void feedbackcommand(Player player,String[] args){
        String title = args[1];
        String content = args[2];
        // 获取当前时间
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //获取当前坐标
        Location location = player.getLocation();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        // 获取反馈数据节点
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "data/feedback.yml"));
        ConfigurationSection feedbackSection = config.getConfigurationSection("feedback");
        if (feedbackSection == null) {
            feedbackSection = config.createSection("feedback");
        }

        // 创建新的反馈节点
        ConfigurationSection newFeedbackSection = feedbackSection.createSection(title);
        newFeedbackSection.set("name", player.getName());
        newFeedbackSection.set("location",x+","+y+","+z);
        newFeedbackSection.set("time", time);
        newFeedbackSection.set("feedback", content);

        // 保存反馈文件
        try {
            config.save(new File(plugin.getDataFolder(), "data/feedback.yml"));
        } catch (IOException e) {
            e.printStackTrace();
            String unfeedback = plugin.getMessage("Command.ct_unfeedback");
            player.sendMessage(unfeedback);
        }
        String feedback = plugin.getMessage("Command.ct_feedback");
        player.sendMessage(feedback);
        //邮件提醒
        if(plugin.getConfig().getBoolean("feedback.email.Enable")){
            if(plugin.getConfig().getBoolean("smtp.Enable")){
                //转换标题
                String subject = plugin.getConfig().getString("feedback.email.subject");
                String replaced_subject = subject.replace("<player_name>", player.getName());
                replaced_subject = replaced_subject.replace("<subject>", title);
                replaced_subject = replaced_subject.replace("<time", time);

                String[] placeholders = {"<player_name />","<time />","<subject />","<feedback />","<location />"};
                String[] replacement = {player.getName(),time,title,content,x +","+ y +"," + z};
                email.setsendmessage(plugin.getConfig().getString("feedback.email.email"),replaced_subject,placeholders,replacement,"plugins/CSNKTools/email/feedback.html");
            }
        }
    }
}

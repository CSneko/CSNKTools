package com.crystalneko.csnktools.website;

import com.crystalneko.csnktools.CSNKTools;
import com.crystalneko.csnktools.CTTool.HtmlPlaceholderConverter;
import com.crystalneko.csnktools.CTTool.mysqlandemail2;
import com.crystalneko.csnktools.CTTool.sqlite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserServlet extends HttpServlet {
    private String[] placeholders = {"<online />", "<max_online />","<motd />","<list_ban />","<list_op />","<player_name />","<player_uuid />"};
    private String[] replacements;
    private HtmlPlaceholderConverter htmlPlaceholderConverter;
    private mysqlandemail2 mysqlAndemail2;
    private CSNKTools plugin;
    private sqlite sqLite;

    public UserServlet(HtmlPlaceholderConverter htmlPlaceholderConverter,mysqlandemail2 mysqlAndemail2,CSNKTools plugin,sqlite sqLite) {
        this.htmlPlaceholderConverter =htmlPlaceholderConverter;
        this.mysqlAndemail2 = mysqlAndemail2;
        this.plugin = plugin;
        this.sqLite = sqLite;
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        // 获取用户名参数
        String username = request.getParameter("username");
        //判断登陆时间
        Long logintime = Long.parseLong(mysqlAndemail2.read_table_where_data("logged",plugin.getConfig().getString("website.user_table"),"username",username));
        //获取时间戳
        long currentTimeStamp = System.currentTimeMillis();
        long istime = currentTimeStamp - logintime;
        //判断是否为30分内
        if(istime <= 1800000) {
            //获取文件是否存在
            File htmlfile = new File("plugins/CSNKTools/website/user" + pathInfo + ".html");
            String htmlpath = "plugins/CSNKTools/website/user" + pathInfo + ".html";
            if (htmlfile.exists()) {
                // 设置变量
                setPlaceholders(username);
                htmlPlaceholderConverter.startConverter(placeholders, replacements, String.valueOf(htmlfile));
                String convertedhtml = htmlPlaceholderConverter.convertedHtml;
                // 获取HTML内容，并作为响应返回给客户端
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().write(convertedhtml);
            } else {
                // 文件不存在
                response.sendRedirect("../404.html");
            }
        } else {response.sendRedirect("../login.html");}

    }
    public void setPlaceholders(String username){
        List<String> banList;
        List<String> opList;
        // 获取服务器对象
        Server server = Bukkit.getServer();
        // 获取在线人数
        int online = server.getOnlinePlayers().size();
        // 获取最大在线人数
        int maxonline = server.getMaxPlayers();
        // 获取MOTD
        String motd = ChatColor.stripColor(server.getMotd()); // 移除颜色代码
        // 创建一个列表来保存封禁玩家的名字
        banList = new ArrayList<>();
        // 封禁列表
        for (OfflinePlayer player : server.getBannedPlayers()) {
            banList.add(player.getName());
        }
        // 创建一个列表来保存管理员的名字
        opList = new ArrayList<>();
        // 管理员列表
        for (OfflinePlayer player : server.getOperators()) {
            opList.add(player.getName());
        }
        //获取玩家uuid
        String uuid = sqLite.getColumnValue(plugin.getConfig().getString("sqlite.player_data_table"),"uuid","name",username);

        //设置变量
        replacements = new String[]{String.valueOf(online), String.valueOf(maxonline), motd, banList.toString(), opList.toString(),username,uuid};
        //防止null
        int length = replacements.length;
        int i =0;
        while (length-1 >i){
            if(replacements[i] != null){}else{
                replacements[i] = "N/A";
            }
            i++;
        }
    }




}



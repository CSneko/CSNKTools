package com.crystalneko.csnktools.csnktools;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServerServlet extends HttpServlet {
    private String adminKey; // 管理员登录密钥
    private CSNKTools plugin;

    @Override
    public void init() throws ServletException {
        super.init();

        // 从配置文件中读取管理员登录密钥
        adminKey = plugin.getConfig().getString("website.key", "");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取网页文件路径
        File file = new File("plugins/CSNKTools/website/index.html");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // 构建响应内容
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String content = sb.toString();

            // 设置响应头和内容
            resp.setContentType("text/html");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println(content);
        } catch (IOException e) {
            e.printStackTrace();
            // 处理错误情况
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("Error reading file.");
        }
    }



}

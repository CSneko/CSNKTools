package com.crystalneko.csnktools.CTcommand;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class download {

    //下载插件
    public static void downloadFile(String fileUrl, String saveFilePath) throws IOException {
        URL url = new URL(fileUrl);
        URLConnection connection = url.openConnection();
        BufferedInputStream in = new BufferedInputStream(connection.getInputStream());

        File outputFile = new File(saveFilePath);
        File parentDir = outputFile.getParentFile();
        if (parentDir != null) {
            parentDir.mkdirs();
        }

        FileOutputStream out = new FileOutputStream(outputFile);

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }

        out.close();
        in.close();
    }

    //复制文件夹
    public static void copyfile(String sourcefolder,String destinationfolder) {
        // 源文件夹路径
        File sourceFolder = new File(sourcefolder);

        // 目标文件夹路径
        File destinationFolder = new File(destinationfolder);

        try {
            // 复制文件夹
            copyFolder(sourceFolder, destinationFolder);
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("文件夹复制失败：" + e.getMessage());
        }
    }

    public static void copyFolder(File sourceFolder, File destinationFolder) throws IOException {
        // 检查源文件夹是否存在
        if (!sourceFolder.exists()) {
            throw new IllegalArgumentException("源文件夹不存在！");
        }

        // 创建目标文件夹
        if (!destinationFolder.isDirectory()) {
            destinationFolder.mkdirs();
        }

        // 获取源文件夹中的所有文件和文件夹
        File[] files = sourceFolder.listFiles();

        // 复制每个文件到目标文件夹
        if (files != null) {
            for (File file : files) {
                File destination = new File(destinationFolder, file.getName());

                if (file.isDirectory()) {
                    // 递归复制子文件夹
                    copyFolder(file, destination);
                } else {
                    // 复制文件
                    Path sourcePath = file.toPath();
                    Path destinationPath = destination.toPath();
                    Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }
    //检查和下载插件
    public static void checkAndDownloadPlugin(String pluginName, String pluginUrl) {
        PluginManager pluginManager = Bukkit.getPluginManager();
        Plugin plugin = pluginManager.getPlugin(pluginName);

        if (plugin == null) {
            Bukkit.getLogger().info(pluginName+"插件未启动，开始下载并启动插件...");

            try {
                downloadPlugin(pluginUrl, pluginName);
                pluginManager.loadPlugin(new File("plugins/" + pluginName + ".jar"));
                pluginManager.enablePlugin(pluginManager.getPlugin(pluginName));
                Bukkit.getLogger().info("插件下载并启动成功！");
            } catch (Exception e) {
                Bukkit.getLogger().severe("插件下载或启动失败：" + e.getMessage());
            }
        } else {
            Bukkit.getLogger().info(pluginName+"插件已启动！");
        }
    }

    private static void downloadPlugin(String pluginUrl, String pluginName) throws IOException {
        URL url = new URL(pluginUrl);
        BufferedInputStream in = new BufferedInputStream(url.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream("plugins/" + pluginName + ".jar");

        byte[] dataBuffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
            fileOutputStream.write(dataBuffer, 0, bytesRead);
        }

        in.close();
        fileOutputStream.close();
    }
}

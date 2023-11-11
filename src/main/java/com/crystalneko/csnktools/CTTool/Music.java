package com.crystalneko.csnktools.CTTool;

import com.crystalneko.csnktools.CSNKTools;
import com.crystalneko.csnktools.CTcommand.download;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.EntitySongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Music {
    private CSNKTools plugin;
    public Music(CSNKTools plugin) {
        this.plugin = plugin;
    }
    public void getCommandValue(String songName, Player player){


        // 检查曲名是否在配置中
        if (isMusic(songName)) {
            String songURL = plugin.getConfig().getString("Music." + songName);
            //缓存变量
            String musictempfile = "plugins/CSNKTools/temp/" + songName + ".nbs";

            File musictemp = new File(musictempfile);


            try {
                //判断是否缓存
                if(musictemp.exists()) {
                    if(playsound(songName,player)){
                        String playingsound = plugin.getMessage("Plugins.Music.playingsound");
                        player.sendMessage(playingsound + songName);
                    } else{
                        String playingerror = plugin.getMessage("Plugins.Music.playingerror");
                        player.sendMessage(playingerror);
                    }

                }else{

                    //下载文件到缓存
                    String tempingmusic = plugin.getMessage("Plugins.Music.temping");
                    player.sendMessage(tempingmusic);
                    //下载保存路径
                    String savesongpath = "plugins/CSNKTools/temp/"+songName+".nbs";
                    download.downloadFile(songURL,savesongpath);
                    if(playsound(songName,player)){
                        String playingsound = plugin.getMessage("Plugins.Music.playingsound");
                        player.sendMessage(playingsound + songName);
                    } else{
                        String playingerror = plugin.getMessage("Plugins.Music.playingerror");
                        player.sendMessage(playingerror);
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {
            player.sendMessage("音乐不存在!");
        }
    }
    public Boolean playsound(String songName, Player player){
        //从缓存加载
        Song playingsong = NBSDecoder.parse(new File("plugins/CSNKTools/temp/" + songName +".nbs"));

        // Create EntitySongPlayer.
        EntitySongPlayer esp = new EntitySongPlayer(playingsong);
        // Set entity which position will be used
        esp.setEntity(player);
        // Set distance from target location in which will players hear the SongPlayer
        esp.setDistance(plugin.getConfig().getInt("Music.distance"));
        // Add player to SongPlayer so he will hear the song.
        esp.addPlayer(player);
        // Start RadioSongPlayer playback
        esp.setPlaying(true);
        return true;
    }

    public void getCommandURL(String songURL, Player player) {
        try {

                //下载文件到缓存
                String tempingmusic = plugin.getMessage("Plugins.Music.temping");
                player.sendMessage(tempingmusic);
                //下载保存路径
                Date currentDate = new Date();
                String savesongpath = "plugins/CSNKTools/temp/" + currentDate +player;
                download.downloadFile(songURL, savesongpath);
                if (playURLsound(songURL, player,savesongpath)) {
                    String playingsound = plugin.getMessage("Plugins.Music.playingsound");
                    player.sendMessage(playingsound + songURL);
                } else {
                    String playingerror = plugin.getMessage("Plugins.Music.playingerror");
                    player.sendMessage(playingerror);

                }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public Boolean playURLsound(String songURL, Player player,String musicsavepath){

        //从缓存加载
        Song playingsong = NBSDecoder.parse(new File(musicsavepath));

        // Create EntitySongPlayer.
        EntitySongPlayer esp = new EntitySongPlayer(playingsong);
        // Set entity which position will be used
        esp.setEntity(player);
        // Set distance from target location in which will players hear the SongPlayer
        esp.setDistance(plugin.getConfig().getInt("Music.distance"));
        // Add player to SongPlayer so he will hear the song.
        esp.addPlayer(player);
        // Start RadioSongPlayer playback
        esp.setPlaying(true);
        return true;
    }
    public Boolean isMusic(String songName){
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

        // 将List转换为Array
        String[] fileNamesArray = fileNames.toArray(new String[0]);
        boolean exists = Arrays.stream(fileNamesArray).anyMatch(songName::equals);
        return exists;
    }

}

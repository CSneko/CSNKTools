package com.crystalneko.csnktools.csnktools.CTTool;

import com.crystalneko.csnktools.csnktools.CSNKTools;
import com.crystalneko.csnktools.csnktools.CTcommand.download;

import com.xxmicloxx.NoteBlockAPI.event.SongDestroyingEvent;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.EntitySongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;


import java.io.File;

import java.io.IOException;
import java.util.Date;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class Music {
    private CSNKTools plugin;
    public Music(CSNKTools plugin) {
        this.plugin = plugin;
    }
    public void getCommandValue(String songName, Player player){

        // 获取配置文件的Music节点
        ConfigurationSection musicSection = plugin.getConfig().getConfigurationSection("Music");

        // 检查曲名是否在配置中
        if (musicSection.contains(songName)) {
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

}

[中文用户请点击这里](README.md)

#CSNKTools
### A multifunctional, customizable, open source and free plug-in
## What can it do?
### In the current version, it can do:
- When a player joins the game, the entire server broadcasts a custom message and supports PlaceholdAPI
- Custom scoreboard with PlaceholderAPI support
- Almost perfect cooperation with PlaceholderAPI
- Fully customizable function switches

### Future plan:
- Add Bossbar
- Add gift package function
- More features

### Precautions:
- Music must be in nbs format!!! [How to convert to nbs format?](nbs_en.md)

### Pre-plugin (optional):

- Login message, scoreboard: [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)
- Music: [NoteblockAPI](https://www.spigotmc.org/resources/noteblockapi.19287/)

## Configuration file (config.yml):
```yaml
#Set language (zh-cn/en-eu)
language: zh-cn
player:
   join:
     # Whether to enable login message
     Enable: true
     #Message sent when logging in
     Message: Welcome %player_name% to join the server
Scoreboard:
   # Whether to enable scoreboard
   Enable: true
   # Scoreboard title
   title: CSNKTools
   # Scoreboard content
   line:
     - Name:%player_name%
     - Level:%player_level%
     - Delay:%player_ping%
   # Scoreboard update time (tick)
   update: 20
Music:
   Enable: true
   distance: 4
   #CloudMusicPlease enter the link
   qunqing: "https://w.csk.asia/res/nbs/qunqing.nbs"
   #Please put local music into the music folder, and fill in the link as www
   badapple: "www"
```
## Commands & Permissions:
```yaml
#The first line is the command
#Second line permissions

/csnktools #Plug-in main command (can be abbreviated as /ct or /cst)
ct.command

/csnktools about #Plug-in information
ct.command.about

/csnktools check #Check for updates
ct.command.check

/csnktools playsound <song name> #Play music
ct.command.playsound

/csnktools soundurl <URL> #Get music from the link
ct.command.soundurl
```
## Known issues:
- An error may appear when starting for the first time. Just restart.
## bStats
![bStats](https://bstats.org/signatures/bukkit/CSNKTools.svg "bStats")

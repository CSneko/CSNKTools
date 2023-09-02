[中文用户请点击这里](README.md)

#CSNKTools
### A multifunctional, customizable, open source and free plugin
## What can it do?
### In the current version, it can do:
- When a player joins the game, the whole server broadcasts a custom message, and supports PlaceholdAPI
- Support custom scoreboard of PlaceholderAPI
- Works almost perfectly with PlaceholderAPI
- Fully customizable feature switches

### Future plan:
- Add Bossbar
- Add gift package function
- more functions

### Precautions:
- Requires the [PaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) plugin as a front
- It is currently an Alpha version, and there are not many functions that can be realized, and more functions will be added in the future
- Please use Spigot and its branches (Paper, Purpur, etc.), the plugin may not work on CraftBukkit
- Use 1.20.1 if possible, no support for versions below 1.20.1

#### How to use: drop it into the Plugins folder
## Configuration file (config.yml):
```yaml
# Plugin master switch
Enable: true
#Set language (zh-cn/en-eu)
language: zh-cn
player:
   join:
     # Whether to open the login message
     Enable: true
     # Message sent when logging in
     Message: Welcome %player_name% to join the server
Scoreboard:
   # Whether to enable the scoreboard
   Enable: true
   # Scoreboard title
   title: CSNKTools
   # Scoreboard content
   line:
     - 名称: %player_name%
     - 等级: %player_level%
     - 延迟: %player_ping%
   # Scoreboard update time (tick)
   update: 20
```
## Known issues:
- There will be a flash when the scoreboard is refreshed, just increase the refresh interval
- There will be an error when starting for the first time, just restart

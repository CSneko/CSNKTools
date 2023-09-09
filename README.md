[English user please check there](README_EN.md)
# CSNKTools
### 一个多功能，可自定义，且开源免费的插件
## 它能干什么?
### 目前版本中，它可以做到:
- 当玩家加入游戏时，全服广播自定义消息，且支持PlaceholdAPI
- 支持PlaceholderAPI的自定义计分板
- 与PlaceholderAPI几乎完美的配合
- 完全自定义功能开关

### 未来计划:
- 添加Bossbar
- 添加礼包功能
- 更多的功能

### 注意事项：
- 音乐必须使用nbs格式!!! [如何转换为nbs格式?](nbs.md)

### 前置插件（可选）:

- 登陆消息,计分板：[PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)
- 音乐：[NoteblockAPI](https://www.spigotmc.org/resources/noteblockapi.19287/)

## 配置文件(config.yml):
```yaml
#设置语言(zh-cn/en-eu)
language: zh-cn
player:
  join:
    # 是否开启登陆消息
    Enable: true
    # 登陆时发送的消息
    Message: 欢迎%player_name%加入服务器
Scoreboard:
  # 是否启用计分板
  Enable: true
  # 计分板标题
  title: CSNKTools
  # 计分板内容
  line:
    - 名称:%player_name%
    - 等级:%player_level%
    - 延迟:%player_ping%
  # 计分板更新时间(tick)
  update: 20
Music:
  Enable: true
  distance: 4
  #云端音乐请输入链接
  qunqing: "https://w.csk.asia/res/nbs/qunqing.nbs"
  #本地音乐请放入music文件夹，链接请填写为www
  badapple: "www"
```
## 命令&权限:
```yaml
#第一行为命令
#第二行为权限

/csnktools   #插件主命令（可简写为/ct或/cst）
ct.command

/csnktools about   #插件信息
ct.command.about

/csnktools check   #检查更新
ct.command.check

/csnktools playsound <曲名>  #播放音乐
ct.command.playsound

/csnktools soundurl <URL>   #从链接获取音乐
ct.command.soundurl
```
## 已知问题：
- 第一次启动可能出现报错，重启即可
## bStats
![bStats](https://bstats.org/signatures/bukkit/CSNKTools.svg "bStats")

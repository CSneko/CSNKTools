# CSNKTools
### 一个多功能，可自定义，且开源免费的插件
## 它能干什么?
### 目前版本中，它可以做到:
- 当玩家加入游戏时，向他发出一条消息
- 支持PlaceholderAPI的自定义计分板
- 与PlaceholderAPI几乎完美的配合
- 完全自定义功能开关

### 未来计划:
- 添加Bossbar
- 添加礼包功能
- 更多的功能

### 注意事项：
- 当前为Alpha版本，可实现的功能不多，后续会添加更多功能
- 请使用Spigot及其分支(Paper,Purpur等)，插件可能在CraftBukkit上不起作用
- 尽可能使用1.20.1，对于1.20.1以下版本不会提供支持
- 计分板功能目前有问题，无法正常使用

#### 使用方法：丢入Plugins文件夹即可
## 配置文件(config.yml):
```yaml
# 插件总开关
Enable: true
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
    - null
  # 计分板更新时间
  update: 5
```


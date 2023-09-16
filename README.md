[English user please check there](README_EN.md)
# CSNKTools
### 一个多功能，可自定义，且开源免费的插件
## 它能干什么?
### 目前版本中，它可以做到:
- 当玩家加入游戏时，全服广播自定义消息，且支持PlaceholdAPI
- 支持PlaceholderAPI的自定义计分板
- 与PlaceholderAPI几乎完美的配合
- 完全自定义功能开关
- 在服务器上搭建网站
- 链接数据库
- 玩家向服务器发送反馈
- 服务器启动或关闭时进行邮件提醒

### 未来计划:
- 添加Bossbar
- 添加礼包功能
- 更多的功能

### 注意事项：
- 音乐必须使用nbs格式!!! [如何转换为nbs格式?](nbs.md)
- 目前不支持sqlite

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
website:  #网页
  Enable: false
  #用户信息数据表
  user_table: usertable
  #网页端口
  port: 8080
  email:
    #注册邮件的主题
    register_subject: "注册邮件"
#mysql数据库
mysql:
  Enable: false
  #mysql驱动
  drive: "com.mysql.cj.jdbc.Driver"#旧版驱动: com.mysql.jdbc.Driver
  #mysql域名及端口
  host: localhost
  port: 3306
  #数据库名
  database: mydatabase
  #mysql服务器时区
  time: GMT
  #是否使用ssl
  usessl: true
  #编码
  char: UTF-8
  #用户名及密码
  username: myuser
  password: mypassword
#sqlite相关设置
sqlite:
  #sqlite路径，如非必要，请勿更改
  path: "plugins/CSNKTools/data/data.db"
#邮件系统
smtp:
  Enable: false
  auth: true
  starttls: true
  #smtp服务器
  host: smtp.example.com
  #smtp端口
  port: 587
  #邮箱地址
  username: smtpuser@example.com
  #邮箱密码（授权码）
  password: smtppassword
#服务器开关提示(需要smtp配置)
serverendisable:
  enable: false
  #接收邮件的邮箱地址
  email: expuser@example.com
  #服务器开启时邮件主题
  enable_subject: "服务器启动了"
  #服务器关闭时邮件主题
  disable_subject: "服务器关闭了"
  #是否存储在数据库中
  savedatabase: false
  #表名
  table_name: serverendisable
  #强制使用sqlite
  usesqlite: false
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

/csnktools feedback <主题> <内容>  #发送反馈（存储在data/feedback.yml）
ct.command.feedback

/csnktoolsadmin  #管理员命令（简写为/cta或/csta）
cta.command

/csnktoolsadmin download <URL> <路径>  #将文件下载到服务器上（例如：/cta download https://w.csk.asia/res/nbs/qunqing.nbs plugins/CSNKTools/music/qunqing.nbs）
cta.command.download
```
## 已知问题：
- 第一次启动可能出现报错，重启即可
- 控制台输入命令会报错，但是不会影响执行
## bStats
![bStats](https://bstats.org/signatures/bukkit/CSNKTools.svg "bStats")

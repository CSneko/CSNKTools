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
- 链接数据库，且支持sqlite和mysql
- 玩家向服务器发送反馈
- 服务器启动或关闭时进行邮件提醒

该项目目前更新缓慢，后面基本不会有什么更新，这并不意味着它已经被放弃了，你可以发送issues来反馈问题
### 注意事项：
- 音乐必须使用nbs格式!!! [如何转换为nbs格式?](nbs.md)

### 前置插件:
- 主要:[ctLib](https://github.com/CSneko/ctLib)(必须，插件启动时会自动下载，也可以手动进行下载)
- 登陆消息,计分板：[PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)(可选)
- 音乐：[NoteblockAPI](https://www.spigotmc.org/resources/noteblockapi.19287/)(可选)
### 如何使用：
- 首先，你需要下载它并放入服务器的```plugins```文件夹，如果你没有下载前置插件ctLib，插件会自动下载它，因此不必担心前置问题
- 接下来，插件会生成一个配置文件，你可以根据需要来自行修改，详情请查看下面的配置文件介绍

### 一些主要配置的介绍:
```language```:语言设置，目前支持简体中文(```zh-cn```),English(```en-eu```)和自定义语言(```more```),你可以在```plugins/CSNKTools/language/```下找到它们
```website.port```:网页启动的端口，不能与服务器端口相同,访问地址为```http://ip:port```，请将ip和port换成你自己的ip和端口
### 邮件使用方法:
你可以在`plugins/CSNKTools/email/`文件夹内更改邮件内容

你需要将```smtp.Enable```设置为true,并在相应位置填入你的smtp服务器，smtp端口，邮件地址和密码，以下是一个示例,用于使用地址为```smtpuser@outlook.com```,密码为```smtppassword```的outlook账户：
```yaml
#邮件系统
smtp:
  Enable: true
  auth: true
  starttls: true
  #smtp服务器
  host: smtp.office365.com
  #smtp端口
  port: 587
  #邮箱地址
  username: smtpuser@outlook.com
  #邮箱密码（授权码）
  password: smtppassword
```
### 数据库使用方法：
如果你使用sqlite,你只需要配置表名即可，甚至什么都不用配置

如果你使用mysql,配置可能会比较麻烦，以下是一个示例，用于连接地址为`127.0.0.1`，端口为`3306`，数据库名为`nanoCraft`的服务器，登陆信息为用户名:`crystalneko`,密码:`password`
```yaml
#mysql数据库
mysql:
  Enable: true
  #mysql驱动
  drive: "com.mysql.cj.jdbc.Driver" #旧版驱动: com.mysql.jdbc.Driver
  #mysql域名及端口
  host: 127.0.0.1
  port: 3306
  #数据库名
  database: nanoCraft
  #mysql服务器时区
  time: GMT
  #是否使用ssl
  usessl: true
  #编码
  char: UTF-8
  #用户名及密码
  username: crystalneko
  password: password
```
### 网页使用方法:
插件对于网页的使用有所简化，这意味着你可以轻松搭建网页

注意：网页目前仅支持mysql存储数据，因此你需要配置mysql,网页的登陆方式与[AuthMeReloaded](https://www.spigotmc.org/resources/authmereloaded.6269/)的登陆逻辑完全相同,因此可以直接使用它的数据库

网页文件存储在`plugins/CSNKTools/website`文件夹内,同样可以自行更改，如果你觉得自带的网页不好看，可以自己进行一些美化操作

#### 占位符:
对于`plugins/CSNKTools/website/user/`文件夹内的所有页面，目前提供如下占位符：

- 当前,最大在线,motd：<online />,<max_online /><motd />
- 封禁，管理员列表:<list_ban />,<list_op />
- 玩家名称,uuid:<player_name />,<player_uuid />

## 配置文件(```config.yml```):
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
  #云端音乐请输入链接,本地音乐请放入music文件夹
  qunqing: "https://w.csk.asia/res/nbs/qunqing.nbs"
#反馈功能，使用/ct feedback <主题> <内容> 发送反馈
feedback:
  Enable: true
  email:
    #是否启用邮件提醒（需要配置smtp）
    Enable: false
    #接受提醒的邮箱
    email: expuser@example.com
    #邮件主题,占位符：玩家名称<player_name>,反馈主题<subject>,反馈时间<time>
    subject: "<player_name>提交了反馈<subject>"
website:  #网页
  Enable: false
  #用户信息数据表
  user_table: usertable
  #网页端口
  port: 8080
  #显示目录结构
  dirlist: false
  email:
    #注册邮件的主题
    register_subject: "注册邮件"
#mysql数据库
mysql:
  Enable: false
  #mysql驱动
  drive: "com.mysql.cj.jdbc.Driver" #旧版驱动: com.mysql.jdbc.Driver
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
  #玩家数据存储表
  player_data_table: playerData
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

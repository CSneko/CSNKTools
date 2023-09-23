[中文用户请点击这里](README.md)
# CSNKTools
### A multifunctional, customizable, open source and free plug-in
## What can it do?
### In the current version, it can do:
- When a player joins the game, the entire server broadcasts a custom message and supports PlaceholdAPI
- Custom scoreboard with PlaceholderAPI support
- Almost perfect cooperation with PlaceholderAPI
- Fully customizable function switches
- Build the website on the server
- Link to database and support sqlite and mysql
- Players send feedback to the server
- Email reminder when server starts or shuts down

### Future plan:
- Add Bossbar
- Add gift package function
- More features

### Precautions:
- Music must be in nbs format!!! [How to convert to nbs format?](nbs.md)

### Pre-plugin:
- Main: [ctLib](https://github.com/CSneko/ctLib) (required, the plug-in will be downloaded automatically when it is started, or you can download it manually)
- Login message, scoreboard: [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) (optional)
- Music: [NoteblockAPI](https://www.spigotmc.org/resources/noteblockapi.19287/) (optional)
### how to use:
- First, you need to download it and put it into the ```plugins``` folder of the server. If you have not downloaded the pre-plugin ctLib, the plugin will automatically download it, so you don’t have to worry about the pre-installation problem.
- Next, the plug-in will generate a configuration file, which you can modify according to your needs. For details, please see the configuration file introduction below.

### Introduction to some main configurations:
```language```: Language setting, currently supports Simplified Chinese (```zh-cn```), English (```en-eu```) and custom language (```more`` `), you can find them under ```plugins/CSNKTools/language/```
```website.port```: The port started by the web page cannot be the same as the server port. The access address is ```http://ip:port```. Please replace the ip and port with your own ip and port. port
### How to use email:
You can change the email content in the `plugins/CSNKTools/email/` folder

You need to set ```smtp.Enable``` to true and fill in your smtp server, smtp port, email address and password in the corresponding positions. The following is an example for using the address ```smtpuser@ outlook.com```, outlook account with password ```smtppassword```:
```yaml
#mailsystem
smtp:
   Enable: true
   auth: true
   starttls: true
   #smtpserver
   host: smtp.office365.com
   #smtpport
   port: 587
   #email address
   username: smtpuser@outlook.com
   #Email password (authorization code)
   password: smtppassword
```
### How to use database:
If you use sqlite, you only need to configure the table name, or even nothing else.

If you use mysql, the configuration may be troublesome. The following is an example for connecting to a server with the address `127.0.0.1`, the port `3306`, and the database name `nanoCraft`. The login information is the username: `crystalneko `,Password:`password`
```yaml
#mysqldatabase
mysql:
   Enable: true
   #mysqldriver
   drive: "com.mysql.cj.jdbc.Driver" #Old version driver: com.mysql.jdbc.Driver
   #mysqlDomain name and port
   host: 127.0.0.1
   port: 3306
   #Database name
   database: nanoCraft
   #mysqlservertimezone
   time: GMT
   #Whether to use ssl
   usessl: true
   #coding
   char: UTF-8
   #Username and password
   username: crystalneko
   password: password
```
### How to use the web page:
Plug-ins simplify the use of web pages, which means you can easily build web pages

Note: The webpage currently only supports mysql to store data, so you need to configure mysql. The login method of the webpage is exactly the same as the login logic of [AuthMeReloaded](https://www.spigotmc.org/resources/authmereloaded.6269/), so you can Use its database directly

Web page files are stored in the `plugins/CSNKTools/website` folder and can also be changed by yourself. If you feel that the web page that comes with it does not look good, you can perform some beautification operations yourself.

#### Placeholder:
For all pages in the `plugins/CSNKTools/website/user/` folder, the following placeholders are currently provided:

- Current, maximum online, motd: <online />, <max_online /><motd />
- Ban, administrator list: <list_ban />, <list_op />
- Player name,uuid:<player_name />,<player_uuid />

## Configuration file (```config.yml```):
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
#Feedback function, use /ct feedback <topic> <content> to send feedback
feedback:
   Enable: true
   email:
     #Whether to enable email reminders (smtp needs to be configured)
     Enable: false
     #Email to receive reminders
     email: expuser@example.com
     #Email subject, placeholder: player name <player_name>, feedback subject <subject>, feedback time <time>
     subject: "<player_name> submitted feedback <subject>"
website: #webpage
   Enable: false
   #User information data table
   user_table: usertable
   #Webport
   port: 8080
   #Show directory structure
   dirlist: false
   email:
     #Subject of registration email
     register_subject: "Register Email"
#mysqldatabase
mysql:
   Enable: false
   #mysqldriver
   drive: "com.mysql.cj.jdbc.Driver" #Old version driver: com.mysql.jdbc.Driver
   #mysqlDomain name and port
   host: localhost
   port: 3306
   #Database name
   database: mydatabase
   #mysqlservertimezone
   time: GMT
   #Whether to use ssl
   usessl: true
   #coding
   char: UTF-8
   #Username and password
   username: myuser
   password: mypassword
#sqlite related settings
sqlite:
   #sqlite path, do not change it unless necessary
   path: "plugins/CSNKTools/data/data.db"
   #Player data storage table
   player_data_table: playerData
#mailsystem
smtp:
   Enable: false
   auth: true
   starttls: true
   #smtpserver
   host: smtp.example.com
   #smtpport
   port: 587
   #email address
   username: smtpuser@example.com
   #Email password (authorization code)
   password: smtppassword
#Server switch prompt (requires smtp configuration)
serverendisable:
   enable: false
   #Email address to receive emails
   email: expuser@example.com
   #Email subject when the server is started
   enable_subject: "The server is started"
   #Email subject when the server is shut down
   disable_subject: "The server is down"
   #Whether it is stored in the database
   savedatabase: false
   #Table Name
   table_name: serverendisable
   #Force to use sqlite
   usesqlite: false
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

/csnktools feedback <topic> <content> #Send feedback (stored in data/feedback.yml)
ct.command.feedback

/csnktoolsadmin #Administrator command (abbreviated as /cta or /csta)
cta.command

/csnktoolsadmin download <URL> <path> #Download the file to the server (for example: /cta download https://w.csk.asia/res/nbs/qunqing.nbs plugins/CSNKTools/music/qunqing.nbs)
cta.command.download
```
## Known issues:
- An error may appear when starting for the first time. Just restart.
- An error will be reported when entering commands on the console, but it will not affect the execution.
## bStats
![bStats](https://bstats.org/signatures/bukkit/CSNKTools.svg "bStats")

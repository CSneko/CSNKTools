package com.crystalneko.csnktools.CTTool;

import com.crystalneko.csnktools.CSNKTools;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class mysqlandemail {
    private String smtpHost;
    private int smtpPort;
    private String smtpUsername;
    private String smtpPassword;
    private Boolean smtpAuth;
    private Boolean smtpstarttls;
    private CSNKTools plugin;
    private HtmlPlaceholderConverter htmlPlaceholderConverter;
    private String body;
    public mysqlandemail(CSNKTools plugin,HtmlPlaceholderConverter htmlPlaceholderConverter) {
        this.plugin = plugin;
        this.htmlPlaceholderConverter = htmlPlaceholderConverter;
    }

    public void loadConfig() {

        if (plugin.getConfig().getBoolean("smtp.Enable")) {

            // 解析SMTP配置
            smtpHost = plugin.getConfig().getString("smtp.host");
            smtpPort = plugin.getConfig().getInt("smtp.port");
            smtpUsername = plugin.getConfig().getString("smtp.username");
            smtpPassword = plugin.getConfig().getString("smtp.password");
            smtpAuth = plugin.getConfig().getBoolean("smtp.auth");
            smtpstarttls = plugin.getConfig().getBoolean("smtp.starttls");

            //这里是需要传入的所有参数
            /*String emailto= "minecraft@outlook.com";
            String subject = "测试邮件";
            String[] placeholder = {"<authcode />","<playername />"};
            int authcode = 114514;
            String strauthcode = String.valueOf(authcode);
            String[] replacement = {strauthcode,"mtpfyc"};
            String htmlpath = "plugins/CSNKTools/email/authcode.html";
            setsendmessage(emailto,subject,placeholder,replacement,htmlpath);*/
        }
    }
    public Boolean setsendmessage(String recipient,String subject,String[] placeholder,String[] replacement,String htmlpath){

        //传入参数
        htmlPlaceholderConverter.startConverter(placeholder,replacement,htmlpath);
        //获得参数
        body = htmlPlaceholderConverter.convertedHtml;
        // 配置邮件服务器
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", smtpAuth);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);

        // 邮箱用户名和密码
        final String username = smtpUsername;
        final String password = smtpPassword;

        // 创建会话
        javax.mail.Session session = javax.mail.Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // 创建邮件消息
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(body);

            // 设置内容为HTML格式
            message.setContent(body, "text/html; charset=utf-8");

            // 发送邮件
            Transport.send(message);

            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

}
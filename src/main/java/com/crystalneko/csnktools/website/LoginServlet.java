package com.crystalneko.csnktools.website;


import com.crystalneko.csnktools.CSNKTools;
import com.crystalneko.csnktools.CTTool.mysqlandemail;
import com.crystalneko.csnktools.CTTool.mysqlandemail2;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

public class LoginServlet extends HttpServlet {
    private CSNKTools plugin;
    private mysqlandemail mysqlAndemail;
    private mysqlandemail2 mysqlAndemail2;
    private String table_name;

    public LoginServlet(CSNKTools plugin, mysqlandemail mysqlAndemail, mysqlandemail2 mysqlAndemail2) {
        this.plugin = plugin;
        this.mysqlAndemail = mysqlAndemail;
        this.mysqlAndemail2 = mysqlAndemail2;
        table_name = plugin.getConfig().getString("website.user_table");
    }
    //实现doGet方法
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo.equals("/login")) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            boolean authenticated = authenticate(username, password);

            if (authenticated) {
                // 登录成功，根据权限跳转到不同页面
                String permission = mysqlAndemail2.read_table_where_data("permission", table_name,"username",username);
                if ("admin".equals(permission)) {
                    response.sendRedirect("../admin.html");
                } else {
                    //设置是否登陆
                    // 获取客户端IP地址
                    String ipAddress = request.getRemoteAddr();
                    //获取时间戳
                    long currentTimeStamp = System.currentTimeMillis();
                    mysqlAndemail2.set_table_data(table_name,"username",username,"logged", String.valueOf(currentTimeStamp));
                    String redirectUrl = "../user/user?username=" + username;
                    response.sendRedirect(redirectUrl);
                }
            } else {
                // 登录失败，返回登录页面
                response.sendRedirect("../login.html");
            }
        } else if (pathInfo.equals("/register")) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String email = request.getParameter("email");

            // 创建用户并保存到数据库
            createUser(username, password, email);

        }
    }

    private boolean authenticate(String username, String password) {
        // 从数据库中读取密码哈希值和盐值
        String passwordHash = mysqlAndemail2.read_password_and_salt(username, table_name);
        Boolean hashpassword = comparePassword(password,passwordHash);
        return hashpassword;
    }



    // 处理邮件相关操作
    private void sendEmailVerification(String username, String email) {
        // 生成验证码
        // 创建一个Random对象
        Random random = new Random();
        // 生成一个随机整数
        int authcode = random.nextInt(100000);
        String auth_code = String.valueOf(authcode);
        // 发送邮件
        String subject = plugin.getConfig().getString("website.email.register_subject");
        String[] placeholders = new String[]{"<username />", "<auth_code />"};
        String[] values = new String[]{username, auth_code};
        mysqlAndemail.setsendmessage(email, subject, placeholders, values, "plugins/CSNKTools/email/authcode.html");
    }
    //哈希加密算法
    private boolean comparePassword(String inputPassword, String storedHash) {
        if (storedHash.startsWith("$SHA$")) {
            String[] parts = storedHash.split("\\$");
            if (parts.length == 4) {
                String salt = parts[2];
                String computedHash = computeHash(inputPassword, salt);
                return storedHash.equals(computedHash);
            }
        }
        return false;
    }

    private String computeHash(String password, String salt) {
        return "$SHA$" + salt + "$" + DigestUtils.sha256Hex(DigestUtils.sha256Hex(password) + salt);
    }

    private String computeHash2(String password) {
        //生成随即盐值
        UUID uuid = UUID.randomUUID();

        String salt= uuid.toString().replace("-", "");
        String hashedPassword = DigestUtils.sha256Hex(password);
        String saltedPassword = hashedPassword + salt;
        String combinedHash = DigestUtils.sha256Hex(saltedPassword);
        return "$SHA$" + salt + "$" + combinedHash;
    }

    private void createUser(String username, String password, String email) {
        // 创建用户并保存到数据库
        String hashedPassword = computeHash2(password);
        String[] data = new String[]{username, hashedPassword, email, "user"};
        mysqlAndemail2.writetable(table_name, "username,password,email,permission", data);
    }


}
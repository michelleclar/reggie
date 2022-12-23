package com.carl.reggie.utils;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

/**
 * @program: reggie
 * @description: 发送邮件
 * @author: Mr.Carl
 * @create: 2022-12-21 19:27
 **/
public class EmailSender {

    public static void sendEmail(String front,String code)  {

        //1、 Properties对象封装会话所需的信息
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");//使用协议 smtp
        props.setProperty("mail.smtp.host", "smtp.qq.com");// qq的smtp服务器地址
        props.setProperty("mail.smtp.port", "465");	//协议端口号
        props.setProperty("mail.smtp.auth", "true");// 需要授权

        //是否需要SSL安全认证 qq是要求需要的
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");	//	没有经过SSL认证的邮件不接收
        props.setProperty("mail.smtp.socketFactory.port", "465");  //SSL 认证端口与smtp一样就可以了


        //2、根据Properties对象封装的信息创建Session对象
        Session  session = Session.getInstance(props);

        //3、 根据session创建邮件MimeMessage
        MimeMessage message = creatMimeMessage(session,"2197792192@qq.com",front,"UTF-8",code);

        try {
            //4、通过session建立与stmp服务器的连接
            Transport transport = session.getTransport();

            //connect(String host,String user,String password) host:发件人邮箱地址； user：用户名；  password：邮箱授权码
            transport.connect("2197792192@qq.com","bidttwawxfnwebdg");

            //5、发送邮件
            transport.sendMessage(message, message.getAllRecipients());
            //6、关闭连接
            transport.close();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    public static MimeMessage creatMimeMessage(Session session,String send, String recipient, String charset,String code)  {
        //简单邮件：{发件人（from）、收件人（to）、标题(subject)、正文（content）、日期（）date}   {图片、附件}

        MimeMessage message = new MimeMessage(session);
        //InternetAddress(String address,String personal, String charset)  其中address:邮件地址  ；personal:标识 ； charset：编码
        try {
            Address from = new InternetAddress(send,"我是发件人",charset);
            Address to = new InternetAddress(recipient,"我是收件人",charset);
            message.setFrom(from);

            //收件人类型：普通收件人（TO）、抄送（CC）、密送（BCC）
            message.setRecipient(MimeMessage.RecipientType.TO,to);

            message.setSubject("这是主题",charset);		//	邮件主题
            message.setContent(code,"text/html;charset=utf-8");	//正文内容
            message.setSentDate(new Date());		//发件日期
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return message;

    }



//    public static void main(String[] args) throws Exception {
//        sendEmail("wb2197792192@163.com",ValidateCodeUtils.generateValidateCode(4).toString());
//
//    }
}

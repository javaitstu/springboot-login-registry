package com.example.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Service
public class MailService {

    @Value("${spring.mail.username}")
    private String mailUsername;
    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private TemplateEngine templateEngine;

    /**
     * 发送邮件激活账号
     *
     * @param activationUrl 激活网址
     * @param email         电子邮件
     */
    public void sendMailForActivationAccount(String activationUrl, String email) {
        // 创建邮件对象
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
            // 设置邮件主题
            message.setSubject("欢迎来到德莱联盟");
            // 设置邮件发送者
            message.setFrom(mailUsername);
            // 设置邮件接收者, 可以多个
            message.setTo(email);
            // 设置邮件抄送者, 可以多个
            //message.setCc();
            // 设置邮件秘密抄送者, 可以多个
            //message.setBcc();
            // 设置邮件发送日期
            message.setSentDate(new Date());
            // 设置上下文环境
            Context context = new Context();
            context.setVariable("activationUrl",activationUrl);
            String text = templateEngine.process("activation-account.html", context);
            // 设置邮件正文
            message.setText(text, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        // 邮件发送
        javaMailSender.send(mimeMessage);
    }
}

package com.study.service.impl;

import cn.hutool.extra.template.engine.freemarker.FreemarkerEngine;
import com.study.service.MailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private Configuration configuration;

    public void sendMailForActivationAccount(String activationUrl, String mail) {
        // 创建邮件对象
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper message = null;
        try {
            message = new MimeMessageHelper(mimeMessage, true);
            // 设置邮件主题
            message.setSubject("欢迎来到德莱联盟");
            // 设置发件人
            message.setFrom(mailUsername);
            // 设置收件人
            message.setTo(mail);
            // 设置抄送人
            //message.setCc();
            // 设置隐藏抄送人
            //message.setBcc();
            // 设置邮件发送日期
            message.setSentDate(new Date());
            // 设置上下文环境

            Map<String, Object> mailMap = new HashMap<>();
            mailMap.put("activationUrl", activationUrl);

            Template template = configuration.getTemplate("activation-account.ftlh");
            String text =
                    FreeMarkerTemplateUtils.processTemplateIntoString(template, mailMap);
            message.setText(text, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        javaMailSender.send(mimeMessage);
    }
}

package com.StudShare.utils;


import com.StudShare.config.EmailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
@ComponentScan(basePackageClasses = {EmailConfig.class})
public class MailHelper
{
    @Autowired
    private MailSender mailSender;

    public void sendMail(String from, String to, String subject, String msg)
    {
        //creating message
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(msg);
        //sending message
        mailSender.send(message);
    }
}

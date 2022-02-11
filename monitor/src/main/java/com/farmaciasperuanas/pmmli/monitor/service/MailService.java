package com.farmaciasperuanas.pmmli.monitor.service;

import com.farmaciasperuanas.pmmli.monitor.entity.ConfigMonitor;
import com.farmaciasperuanas.pmmli.monitor.repository.ConfigMonitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ConfigMonitorRepository configMonitorRepository;

    public void sendEmail(String subject, String to, String htmlText) throws MessagingException, MailException {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            List<ConfigMonitor> cmList = configMonitorRepository.findAll();
            if (cmList.size() > 0) {
                to = cmList.get(0).getEmail();
            }
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            MimeMessageHelper helper = new MimeMessageHelper(message, false);
//      helper.setTo(to);

            helper.setSubject(subject);
            // "<html><body><b>Test HTML</b></html>"
            helper.setText(htmlText, true);

            mailSender.send(message);
        } catch (MailException e) {
            throw e;
        } catch (MessagingException e) {
            throw e;
        }
    }
}

package es.us.isa.ideas.app.mail;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.hibernate.annotations.common.util.impl.Log_$logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;



@Component
public class CustomMailer {
    
    @Autowired
    private MailSender mailSender;
    
    @Autowired
    private CustomizationsExtractor customizationExtrator;
    
    @Value("${mail.from}")
    private String from;

    @Value("${mailserver.host}")
    private String host;

    @Value("${mailserver.port}")
    private String port;
    
    private String [] bcc;
    private String [] cc;
    
    
    public CustomMailer()
    {        
        super();
    }
    
    public void sendMail(Collection<String> recipients, String subject, String msg)
    {
        for(String to:recipients)
            sendMail(to,subject,msg);
    }
    
    public void sendMail(String to, String subject, String msg)
    {
        this.sendMailGrid(to, subject, msg);
    }
    
    public void sendMailGrid(String to, String subject, String msg)
    {
        try {
            Properties properties = System.getProperties();

            properties.put("mail.smtp.host", this.host);
            properties.put("mail.smtp.port", this.port);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            String password = System.getenv("GMAIL_PASSWORD");
            if (password == null) {
                System.out.println("No environment GMAIL_PASSWORD found. Please, declare GMAIL_PASSWORD to send emails from workbench");
            }

            Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

                protected PasswordAuthentication getPasswordAuthentication() {
    
                    return new PasswordAuthentication(getFrom(), password);
    
                }
    
            });
            session.setDebug(true);
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(subject);

            // Now set the actual message
            message.setText(msg);
            
            // Send message
            Transport.send(message);
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
    }
    
    public void sendMail(String to, Map<String,String> customizations, TemplateMail template)
    {
        sendMail(to,template.getCustomizedSubject(customizations),template.getCustomizedContent(customizations));
    }
    
    public void sendMail(Map<String,Map<String,String>> recipients, TemplateMail template)
    {
        Map<String,String> customizations;
        for(String to:recipients.keySet())
        {
            customizations=recipients.get(to);
            sendMail(to,customizations,template);
        }
    }

    public Map<String, String> extractCustomizations(Object researcher) {
        return getCustomizationExtrator().extract(researcher);
    }

    /**
     * @return the mailSender
     */
    public MailSender getMailSender() {
        return mailSender;
    }

    /**
     * @param mailSender the mailSender to set
     */
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * @return the customizationExtrator
     */
    public CustomizationsExtractor getCustomizationExtrator() {
        return customizationExtrator;
    }

    /**
     * @param customizationExtrator the customizationExtrator to set
     */
    public void setCustomizationExtrator(CustomizationsExtractor customizationExtrator) {
        this.customizationExtrator = customizationExtrator;
    }

    /**
     * @return the from
     */
    public String getFrom() {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * @return the bcc
     */
    public String[] getBcc() {
        return bcc;
    }

    /**
     * @param bcc the bcc to set
     */
    public void setBcc(String[] bcc) {
        this.bcc = bcc;
    }

    /**
     * @return the cc
     */
    public String[] getCc() {
        return cc;
    }

    /**
     * @param cc the cc to set
     */
    public void setCc(String[] cc) {
        this.cc = cc;
    }
    
    
}

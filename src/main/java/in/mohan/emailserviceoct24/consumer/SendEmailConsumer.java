package in.mohan.emailserviceoct24.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.mohan.emailserviceoct24.dto.SendEmailDto;
import in.mohan.emailserviceoct24.util.EmailUtil;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Service
public class SendEmailConsumer {
    private ObjectMapper objectMapper;

    public SendEmailConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "sendEmail", groupId = "emailService")
    public void handleSendEmailEvent(String message) throws JsonProcessingException {
        SendEmailDto emailDto = objectMapper.readValue(
                message,
                SendEmailDto.class
        );

        String to = emailDto.getTo();
        String subject = emailDto.getSubject();
        String body = emailDto.getBody();

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("siddhesh4programmingpocs@gmail.com",
                        "iwhtmequdeijqpsm");
            }
        };
        Session session = Session.getInstance(props, auth);

        EmailUtil.sendEmail(session, to, subject, body);
    }
}

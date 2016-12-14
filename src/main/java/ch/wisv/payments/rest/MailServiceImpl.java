package ch.wisv.payments.rest;

import ch.wisv.payments.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

@Service
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;

    private final SpringTemplateEngine templateEngine;

    @Value("${payments.mail.sender}")
    String sender;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public MailServiceImpl(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendOrderConfirmation(Order order) {
        // Prepare message using a Spring helper
        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        MimeMessageHelper message;

        try {
            message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setSubject("W.I.S.V. 'Christaan Huygens' Order Confirmation");
            message.setFrom(sender);
            message.setTo(order.getEmail());

            // Create the HTML body using Thymeleaf
            String htmlContent = prepareHtmlContent(order);
            message.setText(htmlContent, true); // true = isHtml

            // Send mail
            this.mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new MailPreparationException("Unable to prepare email", e.getCause());
        } catch (MailException m) {
            throw new MailSendException("Unable to send email", m.getCause());
        }


    }

    private String prepareHtmlContent(Order order) {
        // Prepare the evaluation context
        final Context ctx = new Context(new Locale("en"));
        ctx.setVariable("order", order);
        return this.templateEngine.process("mail/wisvch", ctx);
    }
}

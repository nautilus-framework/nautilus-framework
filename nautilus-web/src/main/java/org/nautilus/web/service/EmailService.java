package org.nautilus.web.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.nautilus.web.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Value("${server.url}")
    private String serverUrl;

	@Async
	public void sendEmail(MimeMessage email) {
		mailSender.send(email);
	}
	
	public void sendConfirmationMail(User user) {
		
		String confirmationUrl = serverUrl + "/user/confirmation?token=" + user.getConfirmationToken();

		String content = format("email/confirmation-email", "url", confirmationUrl);
		
		MimeMessage message = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");

			helper.setTo(user.getEmail());
			helper.setSubject("Confirm your email!");
			helper.setFrom("noreply@domain.com");

			message.setContent(content, "text/html");

		} catch (MessagingException e) {
			e.printStackTrace();
		}

		sendEmail(message);
	}
	
	public String format(String template, String... params) {

		Context context = new Context();

		for (int i = 0; i < params.length; i += 2) {
			context.setVariable(params[i], params[i + 1]);
		}

		return templateEngine.process(template, context);
	}
}

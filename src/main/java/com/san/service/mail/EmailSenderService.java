//-----------------------------------------------------------------------------------------------------------
//					ORGANIZATION NAME
//Group							: Common - Project
//Product / Project				: spring-jpa-mail
//Module						: spring-jpa-mail
//Package Name					: com.san.service.mail
//File Name						: EmailSenderService.java
//Author						: anil
//Contact						: anilagrawal038@gmail.com
//Date written (DD MMM YYYY)	: 22-Sep-2017 11:49:28 PM
//Description					:  
//-----------------------------------------------------------------------------------------------------------
//					CHANGE HISTORY
//-----------------------------------------------------------------------------------------------------------
//Date			Change By		Modified Function 			Change Description (Bug No. (If Any))
//(DD MMM YYYY)
//22-Sep-2017   	anil			N/A							File Created
//-----------------------------------------------------------------------------------------------------------

package com.san.service.mail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.san.domain.mail.DeliverableMail;
import com.san.domain.mail.MailItem;
import com.san.repo.mail.DeliverableMailRepository;
import com.san.repo.mail.MailItemRepository;
import com.san.util.MailHelperUtil;

@Component
public class EmailSenderService {

	@Inject
	JavaMailSender javaMailSender;

	@Inject
	MailItemRepository mailItemRepository;

	@Inject
	DeliverableMailRepository deliverableMailRepository;

	public void sendMailInstantly(String to, String subject, String textContent) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(textContent);
		javaMailSender.send(message);
	}

	public void sendMailInstantly(String to, String subject, String textContent, String attachmentPath) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(textContent);
		FileSystemResource file = new FileSystemResource(new File(attachmentPath));
		helper.addAttachment(file.getFilename(), file);
		javaMailSender.send(message);
	}

	public void sendMailInstantly(MimeMessage message) throws MessagingException {
		javaMailSender.send(message);
	}

	public void sendMail(String to, String subject, String textContent) {
		MailItem mailItem = new MailItem();
		mailItem.setSubject(subject);
		mailItem.setReceiver(to);
		mailItem.setContent(textContent);
		mailItem = mailItemRepository.save(mailItem);
		sendMail(mailItem);
	}

	public void sendMail(String to, String subject, String textContent, String attachmentPath) throws MessagingException, IOException {
		MailItem mailItem = new MailItem();
		mailItem.setSubject(subject);
		mailItem.setReceiver(to);
		mailItem.setContent(textContent);
		mailItem = mailItemRepository.save(mailItem);
		mailItem.setStorePath(MailHelperUtil.findMailContentDumpPath(to, new Date()) + "/" + mailItem.getId());
		mailItem = mailItemRepository.save(mailItem);
		copyFile(attachmentPath, mailItem.getStorePath());
		sendMail(mailItem);
	}

	public void sendMail(MailItem mailItem) {
		DeliverableMail deliverableMail = new DeliverableMail(mailItem);
		deliverableMail = deliverableMailRepository.save(deliverableMail);
	}

	private void copyFile(String filePath, String destFolder) throws IOException {
		File file = new File(filePath);
		Files.copy(file.toPath(), new File(destFolder + "/" + file.getName()).toPath(), StandardCopyOption.COPY_ATTRIBUTES);
	}
}

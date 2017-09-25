//-----------------------------------------------------------------------------------------------------------
//					ORGANIZATION NAME
//Group							: Common - Project
//Product / Project				: spring-jpa-mail
//Module						: spring-jpa-mail
//Package Name					: com.san.job.mail
//File Name						: MailSenderJob.java
//Author						: anil
//Contact						: anilagrawal038@gmail.com
//Date written (DD MMM YYYY)	: 24-Sep-2017 4:29:21 PM
//Description					:  
//-----------------------------------------------------------------------------------------------------------
//					CHANGE HISTORY
//-----------------------------------------------------------------------------------------------------------
//Date			Change By		Modified Function 			Change Description (Bug No. (If Any))
//(DD MMM YYYY)
//24-Sep-2017   	anil			N/A							File Created
//-----------------------------------------------------------------------------------------------------------

package com.san.job.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.san.domain.mail.DeliverableMail;
import com.san.domain.mail.DeliveredMail;
import com.san.domain.mail.MailItem;
import com.san.domain.mail.UndeliveredMail;
import com.san.repo.mail.DeliverableMailRepository;
import com.san.repo.mail.DeliveredMailRepository;
import com.san.repo.mail.MailItemRepository;
import com.san.repo.mail.UndeliveredMailRepository;
import com.san.service.mail.EmailSenderService;
import com.san.util.MailHelperUtil;

@Component
public class MailSenderJob {

	@Inject
	private DeliverableMailRepository deliverableMailRepository;

	@Inject
	private DeliveredMailRepository deliveredMailRepository;

	@Inject
	private UndeliveredMailRepository undeliveredMailRepository;

	@Inject
	private MailItemRepository mailItemRepository;

	@Inject
	private EmailSenderService emailSenderService;

	@Inject
	private JavaMailSender javaMailSender;

	private ExecutorService mailSenderExecutor = Executors.newFixedThreadPool(1);

	@PostConstruct
	public void init() {
		registerDelivarableMailsProcessorThread();
	}

	private void registerDelivarableMailsProcessorThread() {
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						processDelivarableMails();
						Thread.sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	private void processDelivarableMails() {
		List<DeliverableMail> mails = deliverableMailRepository.findAllByIsProcessed(false);
		for (DeliverableMail mail : mails) {
			mail.setProcessed(true);
			mail.setProcessedTime(System.currentTimeMillis());
			mail = deliverableMailRepository.saveAndFlush(mail);
			MailItem mailItem = mailItemRepository.findOne(mail.getMailId());
			mailSenderExecutor.execute(new Runnable() {
				@Override
				public void run() {
					sendMail(mailItem);
				}
			});
		}
	}

	@Transactional
	private void sendMail(MailItem mailItem) {
		if (!mailItem.isProcessed()) {
			try {
				mailItem.setProcessed(true);
				mailItem.setSentTime(System.currentTimeMillis());
				mailItem = mailItemRepository.save(mailItem);
				MimeMessage mimeMessage = populateMessage(mailItem);
				emailSenderService.sendMailInstantly(mimeMessage);
				DeliveredMail deliveredMail = new DeliveredMail(mailItem);
				deliveredMail = deliveredMailRepository.save(deliveredMail);
			} catch (MessagingException | IOException e) {
				UndeliveredMail undeliveredMail = new UndeliveredMail(mailItem);
				undeliveredMail.setReason(e.getMessage());
				undeliveredMail = undeliveredMailRepository.save(undeliveredMail);
			}
		}
	}

	private MimeMessage populateMessage(MailItem mailItem) throws MessagingException, IOException {
		MimeMessage mimeMessage = null;
		String storePath = mailItem.getStorePath();
		if (storePath != null && !storePath.equals("")) {
			if (mailItem.isStoredAsBinary()) {
				mimeMessage = populateMessageForBinaryStorage(mailItem);
			} else {
				mimeMessage = javaMailSender.createMimeMessage();
				mimeMessage.setContent(populateMessageBodyForNonBinaryStorage(mailItem));
			}
		} else {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setText(mailItem.getContent());
		}
		mimeMessage.setSubject(mailItem.getSubject());
		mimeMessage.setRecipients(RecipientType.BCC, MailHelperUtil.populateInternetAddresses(mailItem.getAllBCC()));
		mimeMessage.setRecipients(RecipientType.CC, MailHelperUtil.populateInternetAddresses(mailItem.getAllCC()));
		mimeMessage.setRecipients(RecipientType.TO, MailHelperUtil.populateInternetAddresses(mailItem.getAllReceivers()));
		mimeMessage.addFrom(MailHelperUtil.populateInternetAddresses(mailItem.getAllSenders()));
		mimeMessage.setReplyTo(MailHelperUtil.populateInternetAddresses(mailItem.getAllReplyTo()));
		if (mailItem.getReceiver() != null && !mailItem.getReceiver().equals("")) {
			mimeMessage.addRecipient(RecipientType.TO, new InternetAddress(mailItem.getReceiver()));
		}
		if (mailItem.getSender() != null && !mailItem.getSender().equals("")) {
			mimeMessage.setFrom(new InternetAddress(mailItem.getSender()));
		}
		mimeMessage.setSentDate(new Date());
		return mimeMessage;
	}

	private Multipart populateMessageBodyForNonBinaryStorage(MailItem mailItem) throws MessagingException {
		Multipart multipart = new MimeMultipart();
		File dir = new File(mailItem.getStorePath());
		if (dir.exists() && dir.isDirectory()) {
			File[] files = dir.listFiles();
			String content = mailItem.getContent();
			if (content == null || content.equals("")) {
				for (File file : files) {
					if (file.getName().startsWith(MailHelperUtil.MESSAGE_BODY_FILE_PREFIX)) {
						MimeBodyPart messageBodyPart = new MimeBodyPart();
						DataSource source = new FileDataSource(file);
						messageBodyPart.setDataHandler(new DataHandler(source));
						multipart.addBodyPart(messageBodyPart);
						break;
					}
				}
			}
			for (File file : files) {
				if (file.getName().equals(MailHelperUtil.MESSAGE_SUBJECT_FILE_NAME)) {
					// Ignore Subject File
				} else if (file.getName().startsWith(MailHelperUtil.MESSAGE_BODY_FILE_PREFIX)) {
					// Ignore Message Body
				} else {
					MimeBodyPart messageBodyPart = new MimeBodyPart();
					DataSource source = new FileDataSource(file);
					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(file.getName());
					multipart.addBodyPart(messageBodyPart);
				}
			}
		}
		return multipart;
	}

	private MimeMessage populateMessageForBinaryStorage(MailItem mailItem) throws MessagingException, IOException {
		InputStream source = new FileInputStream(new File(mailItem.getStorePath() + "/" + MailHelperUtil.MESSAGE_BINARY_FILE_NAME));
		MimeMessage message = javaMailSender.createMimeMessage(source);
		return message;
	}

}

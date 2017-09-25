//-----------------------------------------------------------------------------------------------------------
//					ORGANIZATION NAME
//Group							: Common - Project
//Product / Project				: spring-jpa-mail
//Module						: spring-jpa-mail
//Package Name					: com.san.config
//File Name						: MailConfig.java
//Author						: anil
//Contact						: anilagrawal038@gmail.com
//Date written (DD MMM YYYY)	: 22-Sep-2017 10:15:33 PM
//Description					:  
//-----------------------------------------------------------------------------------------------------------
//					CHANGE HISTORY
//-----------------------------------------------------------------------------------------------------------
//Date			Change By		Modified Function 			Change Description (Bug No. (If Any))
//(DD MMM YYYY)
//22-Sep-2017   	anil			N/A							File Created
//-----------------------------------------------------------------------------------------------------------

package com.san.config;

import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.san.domain.mail.Receiver;
import com.san.domain.mail.Sender;
import com.san.job.mail.IMAPMailReceiverJob;
import com.san.job.mail.POP3MailReceiverJob;
import com.san.repo.mail.ReceiverRepository;
import com.san.repo.mail.SenderRepository;
import com.san.service.BootstrapService;
import com.san.service.mail.EmailReceiverService;
import com.san.util.MailHelperUtil;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.pop3.POP3Folder;
import com.sun.mail.pop3.POP3Store;

@ComponentScan({ "com.san.job.mail" })
@Configuration
public class MailConfig {

	// Reference : http://www.baeldung.com/spring-email

	@Inject
	SenderRepository senderRepository;

	@Inject
	ReceiverRepository receiverRepository;

	@Inject
	EmailReceiverService emailReceiverService;

	@Inject
	BootstrapService bootstrapService;

	@Inject
	private AnnotationConfigApplicationContext ctx;

	@Bean(name = "javaMailSender")
	public JavaMailSender getMailSender() {
		// Note : We assume default mail sender will always present at index 1 with name 'default'
		Sender sender = senderRepository.findOneByName("default");
		JavaMailSender mailSender = null;
		if (sender != null) {
			mailSender = populateMailSender(sender);
			MailHelperUtil.mailSender = mailSender;
		} else {
			System.out.println("No default mail sender configuration found");
		}
		initializeAllMailSenders();
		return mailSender;
	}

	@PostConstruct
	public void init() throws MessagingException {
		initializeAllMailReceivers();
	}

	private void initializeAllMailSenders() {
		List<Sender> senders = senderRepository.findByActive(true);
		for (Sender sender : senders) {
			JavaMailSender mailSender = populateMailSender(sender);
			MailHelperUtil.addMailSender(sender.getEmail(), mailSender);
			ctx.getBeanFactory().registerSingleton("javaMailSenderForSender" + sender.getId(), mailSender);
		}
	}

	private JavaMailSender populateMailSender(Sender sender) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(sender.getHost());
		mailSender.setPort(sender.getPort());
		mailSender.setUsername(sender.getEmail());
		mailSender.setPassword(sender.getPassword());

		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.starttls.enable", sender.isEnableStartTLS());
		javaMailProperties.put("mail.smtp.auth", sender.isEnableAuthentication());
		javaMailProperties.put("mail.transport.protocol", sender.getTransportProtocol());
		javaMailProperties.put("mail.debug", "true");// Prints out everything on screen
		mailSender.setJavaMailProperties(javaMailProperties);
		return mailSender;
	}

	private void initializeAllMailReceivers() throws MessagingException {
		bootstrapService.init();
		List<Receiver> receivers = receiverRepository.findByActive(true);
		for (Receiver receiver : receivers) {
			populateMailReceiver(receiver);
		}
	}

	private Folder populateMailReceiver(Receiver receiver) throws MessagingException {
		Session mailSession = null;
		Store mailStore = null;
		Folder mailFolder = null;
		Properties mailProperties = System.getProperties();
		mailProperties.setProperty("mail.store.protocol", receiver.getProtocol());
		if (receiver.getProtocol().startsWith("imap")) {
			mailProperties.setProperty("mail.imap.ssl.enable", receiver.isEnableSSL() + "");
			mailSession = Session.getInstance(mailProperties);
			mailStore = mailSession.getStore(receiver.getStoreType());
			mailStore.connect(receiver.getHost(), receiver.getEmail(), receiver.getPassword());
			mailFolder = mailStore.getFolder(receiver.getStoreFolder());
			ctx.getBeanFactory().registerSingleton("mailReceiverJobForReceiver" + receiver.getId(),
					new IMAPMailReceiverJob(receiver, (IMAPFolder) mailFolder, (IMAPStore) mailStore, emailReceiverService));
		} else if (receiver.getProtocol().startsWith("pop3")) {
			mailProperties.put("mail.pop3.host", receiver.getHost());
			mailProperties.put("mail.pop3.port", receiver.getPort());
			mailProperties.put("mail.pop3.starttls.enable", receiver.isEnableStartTLS());
			mailProperties.put("mail.pop3.ssl.enable", receiver.isEnableSSL());
			// mailProperties.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			// mailProperties.put("mail.pop3.socketFactory.fallback", false);
			mailSession = Session.getInstance(mailProperties);
			mailStore = mailSession.getStore(receiver.getStoreType());
			mailStore.connect(receiver.getHost(), receiver.getEmail(), receiver.getPassword());
			mailFolder = mailStore.getFolder(receiver.getStoreFolder());
			ctx.getBeanFactory().registerSingleton("mailReceiverJobForReceiver" + receiver.getId(),
					new POP3MailReceiverJob(receiver, (POP3Folder) mailFolder, (POP3Store) mailStore, emailReceiverService));
		}
		MailHelperUtil.addMailReceiverSession(receiver.getEmail(), mailSession);
		MailHelperUtil.addMailReceiverStore(receiver.getEmail(), mailStore);
		MailHelperUtil.addMailReceiverFolder(receiver.getEmail(), mailFolder);
		ctx.getBeanFactory().registerSingleton("mailReceiverSessionForReceiver" + receiver.getId(), mailSession);
		ctx.getBeanFactory().registerSingleton("mailReceiverStoreForReceiver" + receiver.getId(), mailStore);
		ctx.getBeanFactory().registerSingleton("mailReceiverFolderForReceiver" + receiver.getId(), mailFolder);
		return mailFolder;
	}

	@PreDestroy
	public void destroy() {
		List<Receiver> receivers = receiverRepository.findAll();
		for (Receiver receiver : receivers) {
			Folder folder = MailHelperUtil.getMailReceiverFolder(receiver.getEmail());
			if (folder != null) {
				// close folder
				try {
					folder.close(true);
				} catch (MessagingException e) {
				}
			}
			Store store = MailHelperUtil.getMailReceiverStore(receiver.getEmail());
			if (store != null) {
				// close store
				try {
					store.close();
				} catch (MessagingException e) {
				}
			}
			Session session = MailHelperUtil.getMailReceiverSession(receiver.getEmail());
			if (session != null) {
				initializeAllMailSenders();
				// close session
			}
		}
	}

}

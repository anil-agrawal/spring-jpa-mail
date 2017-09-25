//-----------------------------------------------------------------------------------------------------------
//					ORGANIZATION NAME
//Group							: Common - Project
//Product / Project				: spring-jpa-mail
//Module						: spring-jpa-mail
//Package Name					: com.san.service.mail
//File Name						: EmailReceiverService.java
//Author						: anil
//Contact						: anilagrawal038@gmail.com
//Date written (DD MMM YYYY)	: 22-Sep-2017 11:49:00 PM
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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.inject.Inject;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.san.domain.mail.DeliverableMail;
import com.san.domain.mail.MailItem;
import com.san.domain.mail.ReceivedMail;
import com.san.domain.mail.Receiver;
import com.san.repo.mail.DeliverableMailRepository;
import com.san.repo.mail.MailItemRepository;
import com.san.repo.mail.ReceivedMailRepository;
import com.san.util.CommonUtil;
import com.san.util.MailHelperUtil;

@Component
public class EmailReceiverService {

	@Inject
	MailItemRepository mailItemRepository;

	@Inject
	ReceivedMailRepository receivedMailRepository;

	@Inject
	DeliverableMailRepository deliverableMailRepository;

	public void onReceiveMails(Receiver receiver, Message messages[]) {
		System.out.println("mail received");
		for (int i = 0; i < messages.length; ++i) {
			try {
				handleMessage(receiver, messages[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Transactional
	private void handleMessage(Receiver receiver, Message message) throws IOException, MessagingException {
		MailItem mailItem = populateMailItem(receiver, message);
		File file = new File(mailItem.getStorePath());
		file.mkdirs();
		FileWriter fw = new FileWriter(mailItem.getStorePath() + "/" + MailHelperUtil.MESSAGE_SUBJECT_FILE_NAME);
		fw.write(message.getSubject());
		fw.close();
		if (receiver.isStoreAsBinary()) {
			storeMailAsBinary(message, mailItem);
		} else {
			saveMailParts(message.getContent(), mailItem.getStorePath());
		}
		mailItem = mailItemRepository.save(mailItem);
		message.setFlag(Flags.Flag.SEEN, true);
		// to delete the message
		// msg.setFlag(Flags.Flag.DELETED, true);
		ReceivedMail receivedMail = new ReceivedMail(mailItem);
		receivedMail = receivedMailRepository.save(receivedMail);
		test(mailItem);
	}

	@Transactional
	private MailItem populateMailItem(Receiver receiver, Message message) throws MessagingException {
		MailItem mailItem = new MailItem();
		mailItem.setAllBCC(MailHelperUtil.listAddresses(message.getRecipients(RecipientType.BCC)));
		mailItem.setAllCC(MailHelperUtil.listAddresses(message.getRecipients(RecipientType.CC)));
		mailItem.setAllReceivers(MailHelperUtil.listAddresses(message.getAllRecipients()));
		mailItem.setAllReplyTo(MailHelperUtil.listAddresses(message.getReplyTo()));
		mailItem.setAllSenders(MailHelperUtil.listAddresses(message.getFrom()));
		mailItem.setEntryTime(new Date().getTime());
		try {
			mailItem.setReceivedTime(message.getReceivedDate().getTime());
		} catch (NullPointerException e) {
		}
		mailItem.setReceiver(receiver.getEmail());
		mailItem.setReplyTo(MailHelperUtil.findFirstAddress(message.getReplyTo()));
		mailItem.setSender(MailHelperUtil.listAddresses(message.getFrom()));
		mailItem.setSentTime(message.getSentDate().getTime());
		mailItem.setSubject(message.getSubject());
		mailItem = mailItemRepository.saveAndFlush(mailItem);
		String storePath = MailHelperUtil.findMailContentDumpPath(receiver.getEmail(), new Date());
		mailItem.setStorePath(storePath + "/" + mailItem.getId());
		return mailItem;

	}

	private void storeMailAsBinary(Message message, MailItem mailItem) throws MessagingException, IOException {
		OutputStream out = new FileOutputStream(mailItem.getStorePath() + "/" + MailHelperUtil.MESSAGE_BINARY_FILE_NAME);
		try {
			message.writeTo(out);
			mailItem.setStoredAsBinary(true);
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}

	private void saveMailParts(Object content, String path) throws IOException, MessagingException {
		OutputStream out = null;
		InputStream in = null;
		try {
			if (content instanceof Multipart) {
				Multipart multi = ((Multipart) content);
				int parts = multi.getCount();
				for (int j = 0; j < parts; ++j) {
					MimeBodyPart part = (MimeBodyPart) multi.getBodyPart(j);
					if (part.getContent() instanceof Multipart) {
						// part-within-a-part, do some recursion...
						saveMailParts(part.getContent(), path);
					} else {
						String filename = null;
						if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
							filename = path + "/" + part.getFileName();
						} else {
							filename = path + "/" + MailHelperUtil.MESSAGE_BODY_FILE_PREFIX;
							String extension = "";
							if (part.isMimeType("text/html")) {
								extension = "html";
							} else {
								if (part.isMimeType("text/plain")) {
									extension = "txt";
								} else {
									// Try to get the name of the attachment
									extension = part.getDataHandler().getName();
								}
							}
							if (extension == null) {
								extension = "html";
							}
							filename = filename + "." + extension;
						}
						out = new FileOutputStream(new File(filename));
						in = part.getInputStream();
						int k;
						while ((k = in.read()) != -1) {
							out.write(k);
						}
					}
				}
			} else {
				String filename = path + "/" + MailHelperUtil.MESSAGE_BODY_FILE_PREFIX + ".txt";
				FileWriter fw = new FileWriter(filename);
				if (content instanceof String) {
					fw.write((String) content);
				} else {
					fw.write("Unknown content provided. Type : " + content.getClass());
				}
				fw.close();
			}
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}

	public void test(MailItem mailItem) {
		// foeward mail to anilagrawal038@gmail.com
		MailItem newItem = new MailItem();
		CommonUtil.mapProperties(mailItem, newItem);
		newItem.setId(null);
		newItem.setReceiver("anilagrawal038@gmail.com");
		newItem.setAllReceivers("anilagrawal038@gmail.com");
		newItem = mailItemRepository.save(newItem);
		DeliverableMail deliverableMail = new DeliverableMail(newItem);
		deliverableMail = deliverableMailRepository.save(deliverableMail);
	}

}

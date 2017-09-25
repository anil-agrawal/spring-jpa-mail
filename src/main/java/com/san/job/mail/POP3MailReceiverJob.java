//-----------------------------------------------------------------------------------------------------------
//					ORGANIZATION NAME
//Group							: Common - Project
//Product / Project				: spring-jpa-mail
//Module						: spring-jpa-mail
//Package Name					: com.san.job.mail
//File Name						: POP3MailReceiverJob.java
//Author						: anil
//Contact						: anilagrawal038@gmail.com
//Date written (DD MMM YYYY)	: 23-Sep-2017 3:48:58 PM
//Description					:  
//-----------------------------------------------------------------------------------------------------------
//					CHANGE HISTORY
//-----------------------------------------------------------------------------------------------------------
//Date			Change By		Modified Function 			Change Description (Bug No. (If Any))
//(DD MMM YYYY)
//23-Sep-2017   	anil			N/A							File Created
//-----------------------------------------------------------------------------------------------------------

package com.san.job.mail;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

import com.san.domain.mail.Receiver;
import com.san.service.mail.EmailReceiverService;
import com.sun.mail.pop3.POP3Folder;
import com.sun.mail.pop3.POP3Store;

@SuppressWarnings("unused")
public class POP3MailReceiverJob {

	private Receiver receiver;
	private POP3Folder pop3Folder;
	private POP3Store pop3Store;
	private EmailReceiverService emailReceiverService;

	public POP3MailReceiverJob(Receiver receiver, POP3Folder pop3Folder, POP3Store pop3Store, EmailReceiverService emailReceiverService) throws MessagingException {
		this.receiver = receiver;
		this.pop3Folder = pop3Folder;
		this.pop3Store = pop3Store;
		this.emailReceiverService = emailReceiverService;
		init();
	}

	public void init() throws MessagingException {
		pop3Folder.open(Folder.READ_WRITE);
		registerPollingThread();
	}

	public void destroy() {

	}

	private void registerPollingThread() {
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(receiver.getPollingInterval());
						try {
							if (pop3Folder.getUnreadMessageCount() > 0) {
								Message[] messages = pop3Folder.getMessages();
								emailReceiverService.onReceiveMails(receiver, messages);
							} else {
								if (pop3Folder.isOpen()) {
									pop3Folder.close(true);
									pop3Folder.open(Folder.READ_WRITE);
								}
							}
						} catch (MessagingException e) {
							e.printStackTrace();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

}

//-----------------------------------------------------------------------------------------------------------
//					ORGANIZATION NAME
//Group							: Common - Project
//Product / Project				: spring-jpa-mail
//Module						: spring-jpa-mail
//Package Name					: com.san.job.mail
//File Name						: IMAPMailReceiverJob.java
//Author						: anil
//Contact						: anilagrawal038@gmail.com
//Date written (DD MMM YYYY)	: 23-Sep-2017 3:48:42 PM
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
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;

import com.san.domain.mail.Receiver;
import com.san.service.mail.EmailReceiverService;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

public class IMAPMailReceiverJob {

	private Receiver receiver;
	private IMAPFolder imapFolder;
	private IMAPStore imapStore;
	private EmailReceiverService emailReceiverService;

	public IMAPMailReceiverJob(Receiver receiver, IMAPFolder imapFolder, IMAPStore imapStore, EmailReceiverService emailReceiverService) throws MessagingException {
		this.receiver = receiver;
		this.imapFolder = imapFolder;
		this.imapStore = imapStore;
		this.emailReceiverService = emailReceiverService;
		init();
	}

	public void init() throws MessagingException {
		imapFolder.open(Folder.READ_WRITE);
		if (imapStore.hasCapability("IDLE")) {
			registerMessageCountListener();
			registerIdleThread();
		} else {
			// registerMessageCountListener();
			registerPollingThread();
		}
	}

	public void destroy() {

	}

	private void registerMessageCountListener() {
		imapFolder.addMessageCountListener(new MessageCountAdapter() {
			@Override
			public void messagesAdded(MessageCountEvent messageCountEvent) {
				emailReceiverService.onReceiveMails(receiver, messageCountEvent.getMessages());
			}
		});
	}

	private void registerIdleThread() {
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(receiver.getPollingInterval());
						try {
							imapFolder.idle(true);
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

	private void registerPollingThread() {
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(receiver.getPollingInterval());
						try {
							if (imapFolder.getUnreadMessageCount() > 0) {
								System.out.println("unread msg found");
								Message[] messages = imapFolder.getMessages();
								emailReceiverService.onReceiveMails(receiver, messages);
							} else {
								if (imapFolder.isOpen()) {
									imapFolder.close(true);
									imapFolder.open(Folder.READ_WRITE);
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

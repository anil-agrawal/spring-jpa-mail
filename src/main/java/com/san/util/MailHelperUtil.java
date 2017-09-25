//-----------------------------------------------------------------------------------------------------------
//					ORGANIZATION NAME
//Group							: Common - Project
//Product / Project				: spring-jpa-mail
//Module						: spring-jpa-mail
//Package Name					: com.san.util
//File Name						: MailHelperUtil.java
//Author						: anil
//Contact						: anilagrawal038@gmail.com
//Date written (DD MMM YYYY)	: 22-Sep-2017 10:34:19 PM
//Description					:  
//-----------------------------------------------------------------------------------------------------------
//					CHANGE HISTORY
//-----------------------------------------------------------------------------------------------------------
//Date			Change By		Modified Function 			Change Description (Bug No. (If Any))
//(DD MMM YYYY)
//22-Sep-2017   	anil			N/A							File Created
//-----------------------------------------------------------------------------------------------------------

package com.san.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.springframework.mail.javamail.JavaMailSender;

public class MailHelperUtil {

	private static final Map<String, JavaMailSender> mailSenderMap = new HashMap<String, JavaMailSender>();
	private static final Map<String, Session> mailReceiverSessionMap = new HashMap<String, Session>();
	private static final Map<String, Store> mailReceiverStoreMap = new HashMap<String, Store>();
	private static final Map<String, Folder> mailReceiverFolderMap = new HashMap<String, Folder>();
	public static JavaMailSender mailSender;

	public static final String MESSAGE_SUBJECT_FILE_NAME = "message_subject.txt";
	public static final String MESSAGE_BINARY_FILE_NAME = "message.eml";
	public static final String MESSAGE_BODY_FILE_PREFIX = "message_body_content";
	public static final String RECEIVERS_FILE_NAME = "receivers.txt";
	public static final String MAILS_STORE_FOLDER = "mails";
	public static final String RECEIVER_LOG_FOLDER = "logs/receiver";
	public static final String SENDER_LOG_FOLDER = "logs/sender";
	public static final String LINE_SEPERATOR = System.lineSeparator();

	public static void addMailSender(String sender, JavaMailSender mailSender) {
		mailSenderMap.put(sender, mailSender);
	}

	public static JavaMailSender getMailSender(String sender) {
		JavaMailSender javaMailSender = mailSenderMap.get(sender);
		if (javaMailSender == null) {
			javaMailSender = mailSender;
		}
		return javaMailSender;
	}

	public static void addMailReceiverSession(String receiver, Session session) {
		mailReceiverSessionMap.put(receiver, session);
	}

	public static Session getMailReceiverSession(String receiver) {
		Session session = mailReceiverSessionMap.get(receiver);
		return session;
	}

	public static void addMailReceiverStore(String receiver, Store store) {
		mailReceiverStoreMap.put(receiver, store);
	}

	public static Store getMailReceiverStore(String receiver) {
		Store store = mailReceiverStoreMap.get(receiver);
		return store;
	}

	public static void addMailReceiverFolder(String receiver, Folder folder) {
		mailReceiverFolderMap.put(receiver, folder);
	}

	public static Folder getMailReceiverFolder(String receiver) {
		Folder folder = mailReceiverFolderMap.get(receiver);
		return folder;
	}

	public static String parseAddress(Address address) {
		if (address == null) {
			return "";
		}
		InternetAddress internetAddress = (InternetAddress) address;
		return internetAddress.getAddress();
	}

	public static String listAddresses(Address[] addresses) {
		StringBuilder sb = new StringBuilder();
		if (addresses == null) {
			addresses = new Address[] {};
		}
		for (Address address : addresses) {
			sb.append(parseAddress(address));
			sb.append(",");
		}
		return sb.toString();
	}

	public static String findFirstAddress(Address[] addresses) {
		StringBuilder sb = new StringBuilder();
		if (addresses == null) {
			addresses = new Address[] {};
		}
		for (Address address : addresses) {
			sb.append(parseAddress(address));
			break;
		}
		return sb.toString();
	}

	public static String findMailContentDumpPath(String email, Date date) {
		return MailHelperUtil.MAILS_STORE_FOLDER + "/" + email + "/" + DateTimeUtil.toDateString(date);
	}

	public static Address[] populateInternetAddresses(String addresses) {
		if (addresses == null || addresses.equals("")) {
			return new Address[] {};
		}
		String[] addressList = addresses.split(",");
		List<Address> internetAddresses = new ArrayList<Address>();
		for (int counter = 0; counter < addressList.length; counter++) {
			if (addressList[counter].length() > 0) {
				try {
					internetAddresses.add(new InternetAddress(addressList[counter].trim()));
				} catch (AddressException e) {
					System.out.println("Address : " + addressList[counter] + " couldn't be added in mail address list");
				}
			}
		}
		return internetAddresses.toArray(new Address[] {});
	}

}

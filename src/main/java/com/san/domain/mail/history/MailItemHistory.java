//-----------------------------------------------------------------------------------------------------------
//					ORGANIZATION NAME
//Group							: Common - Project
//Product / Project				: spring-jpa-mail
//Module						: spring-jpa-mail
//Package Name					: com.san.domain.mail.history
//File Name						: MailItemHistory.java
//Author						: anil
//Contact						: anilagrawal038@gmail.com
//Date written (DD MMM YYYY)	: 22-Sep-2017 9:56:58 PM
//Description					:  
//-----------------------------------------------------------------------------------------------------------
//					CHANGE HISTORY
//-----------------------------------------------------------------------------------------------------------
//Date			Change By		Modified Function 			Change Description (Bug No. (If Any))
//(DD MMM YYYY)
//22-Sep-2017   	anil			N/A							File Created
//-----------------------------------------------------------------------------------------------------------

package com.san.domain.mail.history;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MailItemHistory {

	@Id
	private Long id;
	private String sender;
	private String receiver;
	private String replyTo;
	private String allReplyTo;
	private String allReceivers;
	private String allSenders;
	private String allCC;
	private String allBCC;
	private String subject;
	private long entryTime;
	private String storePath;
	private boolean isProcessed;
	private boolean isSystemGeneratedMail;
	private boolean isStatusMail;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	public String getAllReplyTo() {
		return allReplyTo;
	}

	public void setAllReplyTo(String allReplyTo) {
		this.allReplyTo = allReplyTo;
	}

	public String getAllReceivers() {
		return allReceivers;
	}

	public void setAllReceivers(String allReceivers) {
		this.allReceivers = allReceivers;
	}

	public String getAllSenders() {
		return allSenders;
	}

	public void setAllSenders(String allSenders) {
		this.allSenders = allSenders;
	}

	public String getAllCC() {
		return allCC;
	}

	public void setAllCC(String allCC) {
		this.allCC = allCC;
	}

	public String getAllBCC() {
		return allBCC;
	}

	public void setAllBCC(String allBCC) {
		this.allBCC = allBCC;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public long getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(long entryTime) {
		this.entryTime = entryTime;
	}

	public String getStorePath() {
		return storePath;
	}

	public void setStorePath(String storePath) {
		this.storePath = storePath;
	}

	public boolean isProcessed() {
		return isProcessed;
	}

	public void setProcessed(boolean isProcessed) {
		this.isProcessed = isProcessed;
	}

	public boolean isSystemGeneratedMail() {
		return isSystemGeneratedMail;
	}

	public void setSystemGeneratedMail(boolean isSystemGeneratedMail) {
		this.isSystemGeneratedMail = isSystemGeneratedMail;
	}

	public boolean isStatusMail() {
		return isStatusMail;
	}

	public void setStatusMail(boolean isStatusMail) {
		this.isStatusMail = isStatusMail;
	}
}

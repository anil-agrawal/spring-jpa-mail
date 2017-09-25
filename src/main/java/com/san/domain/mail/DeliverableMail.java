//-----------------------------------------------------------------------------------------------------------
//					ORGANIZATION NAME
//Group							: Common - Project
//Product / Project				: spring-jpa-mail
//Module						: spring-jpa-mail
//Package Name					: com.san.domain.mail
//File Name						: DeliverableMail.java
//Author						: anil
//Contact						: anilagrawal038@gmail.com
//Date written (DD MMM YYYY)	: 22-Sep-2017 8:50:47 PM
//Description					:  
//-----------------------------------------------------------------------------------------------------------
//					CHANGE HISTORY
//-----------------------------------------------------------------------------------------------------------
//Date			Change By		Modified Function 			Change Description (Bug No. (If Any))
//(DD MMM YYYY)
//22-Sep-2017   	anil			N/A							File Created
//-----------------------------------------------------------------------------------------------------------

package com.san.domain.mail;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class DeliverableMail {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "deliverable_mail_seq_gen")
	@SequenceGenerator(name = "deliverable_mail_seq_gen", sequenceName = "deliverable_mail_seq")
	private Long id;
	private Long mailId;
	private boolean isProcessed;
	private long processedTime;

	public DeliverableMail() {
	}

	public DeliverableMail(MailItem mailItem) {
		mailId = mailItem.getId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMailId() {
		return mailId;
	}

	public void setMailId(Long mailId) {
		this.mailId = mailId;
	}

	public boolean isProcessed() {
		return isProcessed;
	}

	public void setProcessed(boolean isProcessed) {
		this.isProcessed = isProcessed;
	}

	public long getProcessedTime() {
		return processedTime;
	}

	public void setProcessedTime(long processedTime) {
		this.processedTime = processedTime;
	}

}

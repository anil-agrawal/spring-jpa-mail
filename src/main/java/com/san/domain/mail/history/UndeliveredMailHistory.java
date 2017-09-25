//-----------------------------------------------------------------------------------------------------------
//					ORGANIZATION NAME
//Group							: Common - Project
//Product / Project				: spring-jpa-mail
//Module						: spring-jpa-mail
//Package Name					: com.san.domain.mail.history
//File Name						: UndeliveredMailHistory.java
//Author						: anil
//Contact						: anilagrawal038@gmail.com
//Date written (DD MMM YYYY)	: 22-Sep-2017 9:57:36 PM
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
public class UndeliveredMailHistory {

	@Id
	private Long id;
	private Long mailId;
	private long entryTime;
	private boolean isProcessed;
	private String reason;

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

	public long getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(long entryTime) {
		this.entryTime = entryTime;
	}

	public boolean isProcessed() {
		return isProcessed;
	}

	public void setProcessed(boolean isProcessed) {
		this.isProcessed = isProcessed;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}

//-----------------------------------------------------------------------------------------------------------
//					ORGANIZATION NAME
//Group							: Common - Project
//Product / Project				: spring-jpa-mail
//Module						: spring-jpa-mail
//Package Name					: com.san.domain.mail
//File Name						: Receiver.java
//Author						: anil
//Contact						: anilagrawal038@gmail.com
//Date written (DD MMM YYYY)	: 22-Sep-2017 11:50:35 PM
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
public class Receiver {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "receiver_seq_gen")
	@SequenceGenerator(name = "receiver_seq_gen", sequenceName = "receiver_seq")
	private Long id;
	private String name;
	private String info;
	private String host;
	private int port;
	private String protocol;
	private String storeType;
	private String storeFolder;
	private String email;
	private String password;
	private boolean enableSSL;
	private boolean enableStartTLS;
	private boolean active;
	private boolean storeAsBinary;
	private long pollingInterval;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public String getStoreFolder() {
		return storeFolder;
	}

	public void setStoreFolder(String storeFolder) {
		this.storeFolder = storeFolder;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public long getPollingInterval() {
		return pollingInterval;
	}

	public void setPollingInterval(long pollingInterval) {
		this.pollingInterval = pollingInterval;
	}

	public boolean isEnableSSL() {
		return enableSSL;
	}

	public void setEnableSSL(boolean enableSSL) {
		this.enableSSL = enableSSL;
	}

	public boolean isStoreAsBinary() {
		return storeAsBinary;
	}

	public void setStoreAsBinary(boolean storeAsBinary) {
		this.storeAsBinary = storeAsBinary;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isEnableStartTLS() {
		return enableStartTLS;
	}

	public void setEnableStartTLS(boolean enableStartTLS) {
		this.enableStartTLS = enableStartTLS;
	}

}

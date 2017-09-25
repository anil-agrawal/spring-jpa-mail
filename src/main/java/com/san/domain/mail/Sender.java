//-----------------------------------------------------------------------------------------------------------
//					ORGANIZATION NAME
//Group							: Common - Project
//Product / Project				: spring-jpa-mail
//Module						: spring-jpa-mail
//Package Name					: com.san.domain.mail
//File Name						: Sender.java
//Author						: anil
//Contact						: anilagrawal038@gmail.com
//Date written (DD MMM YYYY)	: 22-Sep-2017 10:25:50 PM
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
public class Sender {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sender_seq_gen")
	@SequenceGenerator(name = "sender_seq_gen", sequenceName = "sender_seq")
	private Long id;
	private String name;
	private String info;
	private String host;
	private int port;
	private String email;
	private String password;
	private String transportProtocol;
	private boolean enableAuthentication;
	private boolean enableStartTLS;
	private boolean active;

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

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
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

	public String getTransportProtocol() {
		return transportProtocol;
	}

	public void setTransportProtocol(String transportProtocol) {
		this.transportProtocol = transportProtocol;
	}

	public boolean isEnableAuthentication() {
		return enableAuthentication;
	}

	public void setEnableAuthentication(boolean enableAuthentication) {
		this.enableAuthentication = enableAuthentication;
	}

	public boolean isEnableStartTLS() {
		return enableStartTLS;
	}

	public void setEnableStartTLS(boolean enableStartTLS) {
		this.enableStartTLS = enableStartTLS;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}

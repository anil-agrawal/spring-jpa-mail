//-----------------------------------------------------------------------------------------------------------
//					ORGANIZATION NAME
//Group							: Common - Project
//Product / Project				: spring-jpa-mail
//Module						: spring-jpa-mail
//Package Name					: com.san.service
//File Name						: BootstrapService.java
//Author						: anil
//Contact						: anilagrawal038@gmail.com
//Date written (DD MMM YYYY)	: 23-Sep-2017 6:38:20 PM
//Description					:  
//-----------------------------------------------------------------------------------------------------------
//					CHANGE HISTORY
//-----------------------------------------------------------------------------------------------------------
//Date			Change By		Modified Function 			Change Description (Bug No. (If Any))
//(DD MMM YYYY)
//23-Sep-2017   	anil			N/A							File Created
//-----------------------------------------------------------------------------------------------------------

package com.san.service;

import java.io.File;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.san.domain.mail.Receiver;
import com.san.domain.mail.Sender;
import com.san.repo.mail.ReceiverRepository;
import com.san.repo.mail.SenderRepository;
import com.san.util.CommonUtil;

@Component
public class BootstrapService {

	private static ObjectMapper objectMapper = new ObjectMapper();

	@Inject
	ReceiverRepository receiverRepository;

	@Inject
	SenderRepository senderRepository;

	@PostConstruct
	public void init() {
		bootstrapReceivers();
		bootstrapSenders();
	}

	@PreDestroy
	public void destroy() {
	}

	@Transactional
	private void bootstrapReceivers() {
		if (receiverRepository.count() < 1) {
			JsonNode json = CommonUtil.fetchJSONResource("json" + File.separator + "bootstrap" + File.separator + "receivers.json");
			if (json != null && json.get("receivers").isArray()) {
				Iterator<JsonNode> itr = json.get("receivers").elements();
				while (itr.hasNext()) {
					JsonNode receiverJson = itr.next();
					try {
						Receiver receiver = objectMapper.treeToValue(receiverJson, Receiver.class);
						receiver = receiverRepository.save(receiver);
						System.out.println("receiver saved with id : " + receiver.getId());
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Transactional
	private void bootstrapSenders() {
		if (senderRepository.count() < 1) {
			JsonNode json = CommonUtil.fetchJSONResource("json" + File.separator + "bootstrap" + File.separator + "senders.json");
			if (json != null && json.get("senders").isArray()) {
				Iterator<JsonNode> itr = json.get("senders").elements();
				while (itr.hasNext()) {
					JsonNode senderJson = itr.next();
					try {
						Sender sender = objectMapper.treeToValue(senderJson, Sender.class);
						sender = senderRepository.save(sender);
						System.out.println("sender saved with id : " + sender.getId());
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}

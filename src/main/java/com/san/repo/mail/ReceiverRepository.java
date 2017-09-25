//-----------------------------------------------------------------------------------------------------------
//					ORGANIZATION NAME
//Group							: Common - Project
//Product / Project				: spring-jpa-mail
//Module						: spring-jpa-mail
//Package Name					: com.san.repo.mail
//File Name						: ReceiverRepository.java
//Author						: anil
//Contact						: anilagrawal038@gmail.com
//Date written (DD MMM YYYY)	: 22-Sep-2017 11:51:21 PM
//Description					:  
//-----------------------------------------------------------------------------------------------------------
//					CHANGE HISTORY
//-----------------------------------------------------------------------------------------------------------
//Date			Change By		Modified Function 			Change Description (Bug No. (If Any))
//(DD MMM YYYY)
//22-Sep-2017   	anil			N/A							File Created
//-----------------------------------------------------------------------------------------------------------

package com.san.repo.mail;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.san.domain.mail.Receiver;

@Repository
public interface ReceiverRepository extends JpaRepository<Receiver, Long> {

	Receiver findOneByName(String name);

	Receiver findOneByEmail(String email);

	List<Receiver> findByActive(boolean active);
}

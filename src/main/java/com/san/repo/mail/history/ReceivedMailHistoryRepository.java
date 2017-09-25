//-----------------------------------------------------------------------------------------------------------
//					ORGANIZATION NAME
//Group							: Common - Project
//Product / Project				: spring-jpa-mail
//Module						: spring-jpa-mail
//Package Name					: com.san.repo.mail.history
//File Name						: ReceivedMailHistoryRepository.java
//Author						: anil
//Contact						: anilagrawal038@gmail.com
//Date written (DD MMM YYYY)	: 22-Sep-2017 10:05:52 PM
//Description					:  
//-----------------------------------------------------------------------------------------------------------
//					CHANGE HISTORY
//-----------------------------------------------------------------------------------------------------------
//Date			Change By		Modified Function 			Change Description (Bug No. (If Any))
//(DD MMM YYYY)
//22-Sep-2017   	anil			N/A							File Created
//-----------------------------------------------------------------------------------------------------------

package com.san.repo.mail.history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.san.domain.mail.history.ReceivedMailHistory;

@Repository
public interface ReceivedMailHistoryRepository extends JpaRepository<ReceivedMailHistory, Long> {

}

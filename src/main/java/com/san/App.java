package com.san;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.san.config.AppConfig;
import com.san.config.JPAConfig;
import com.san.config.MailConfig;
import com.san.util.AppUtil;

public class App {

	public static void main(String[] args) {
		System.out.println("Initializing Application");
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(JPAConfig.class, AppConfig.class, MailConfig.class);
		AppUtil.ctx = ctx;
		System.out.println("Application Initialized");
	}

}

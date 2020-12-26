package com.lyq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ComponentScan(basePackages= {
		"com.lyq.framework.common",
		"com.lyq.framework.spring",
		"com.lyq.framework.util",
		"com.lyq.framework.quartz"})
@EnableTransactionManagement
@SpringBootApplication
public class EnjoyStarter{
	
    public static void main(String[] args) {
    	SpringApplication.run(EnjoyStarter.class, args);
    }
}

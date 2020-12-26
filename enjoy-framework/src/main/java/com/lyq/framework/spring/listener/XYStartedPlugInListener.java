package com.lyq.framework.spring.listener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class XYStartedPlugInListener implements ApplicationListener<ApplicationStartedEvent>{

	private static SpringApplication atx = null;

	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		System.out.print("-------------------XYStartedPlugInListener--");
		if (atx != null) {
			return;
		}


		atx = event.getSpringApplication();

	}

	private void loadPara() {

	}

	/**
	 * jvm参数优先级最高
	 * 
	 * @author lixinyu 2020-05-21
	 * @version 1.0
	 */
	private void loadJVMPara() {
	}
}

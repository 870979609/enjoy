package com.lyq.framework.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringBeanUtil implements ApplicationContextAware{
	private static Logger log = LoggerFactory.getLogger(SpringBeanUtil.class);
	private static ApplicationContext applicationContext = null;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContextPara) throws BeansException {
		 if (applicationContext == null) {
			 log.info("-----------------applicationContext注入完毕-------------------");
		     applicationContext = applicationContextPara;
		 }
	}
	
	public static ApplicationContext getApplicationContext()
	  {
	    return applicationContext;
	  }

	  public static Object getBean(String name)
	  {
	    return getApplicationContext().getBean(name);
	  }

	  public static <T> T getBean(Class<T> clazz)
	  {
	    ApplicationContext applicationContext = getApplicationContext();
	    return applicationContext.getBean(clazz);
	  }

	  public static <T> T getBean(Class<T> clazz, Object[] objects) {
	    ApplicationContext applicationContext = getApplicationContext();
	    return applicationContext.getBean(clazz, objects);
	  }

	  public static boolean isExistBean(String name) {
	    ApplicationContext applicationContext = getApplicationContext();
	    return applicationContext.containsBean(name);
	  }

}

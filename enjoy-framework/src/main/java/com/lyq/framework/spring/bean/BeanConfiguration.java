package com.lyq.framework.spring.bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(locations={"classpath:bean/applicationContext.xml"})
public class BeanConfiguration{

}

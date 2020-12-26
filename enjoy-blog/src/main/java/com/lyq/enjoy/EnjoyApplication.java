package com.lyq.enjoy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@ComponentScan(basePackages= {
        "com.lyq.framework",
        "com.lyq.enjoy.blog",
        "com.lyq.enjoy.global"})
@SpringBootApplication
@MapperScan({
        "com.lyq.enjoy.blog.mapper",
        "com.lyq.enjoy.global.mapper",
        "com.lyq.framework"
})
@EnableAspectJAutoProxy
@EnableTransactionManagement
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.lyq.enjoy.blog.stub")
@ServletComponentScan
public class EnjoyApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnjoyApplication.class, args);
    }

  //  @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}


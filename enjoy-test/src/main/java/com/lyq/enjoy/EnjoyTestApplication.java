package com.lyq.enjoy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ComponentScan(basePackages= {
        "com.lyq.framework",
        "com.lyq.enjoy.test"})
@SpringBootApplication
@MapperScan({
        "com.lyq.framework"
})
@EnableAspectJAutoProxy
@EnableTransactionManagement
@ServletComponentScan
@EnableEurekaClient
public class EnjoyTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnjoyTestApplication.class, args);
    }

}

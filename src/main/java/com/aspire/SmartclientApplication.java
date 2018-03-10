package com.aspire;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.aspire.mapper") //扫描Mapper包 为其创建代理对象
@EnableTransactionManagement  //开启事务管理
public class SmartclientApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {

		SpringApplication.run(SmartclientApplication.class, args);

	}

	//@Override
	//protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
	//	return builder.sources(SmartclientApplication.class);
	//}
}

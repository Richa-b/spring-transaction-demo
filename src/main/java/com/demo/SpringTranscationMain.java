package com.demo;

import com.demo.service.AccountService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class SpringTranscationMain {


    public static void main(String args[]) {

        ApplicationContext context =
                new ClassPathXmlApplicationContext("spring-config.xml");
        AccountService accountService = context.getBean(AccountService.class);
        accountService.exchangeAmount("user1", "user2", 100);
        accountService.exchangeAmount("user2", "user3", 100);
    }
}

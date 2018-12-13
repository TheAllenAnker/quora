package com.allenanker.quora.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.allenanker.quora.controller.IndexController.*(..))")
    public void beforeMethod() {
        System.out.println("Before method");
    }

    @After("execution(* com.allenanker.quora.controller.IndexController.*(..))")
    public void afterMethod() {
        System.out.println("After method");
    }
}

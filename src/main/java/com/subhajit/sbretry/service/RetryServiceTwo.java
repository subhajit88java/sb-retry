package com.subhajit.sbretry.service;

import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class RetryServiceTwo {
    @Retryable(value = NullPointerException.class)
    public String call2ndLevelMethod() {
        System.out.println("In RetryServiceTwo call2ndLevelMethod starts ......");
        String s = null;
        s.length();
        System.out.println("In RetryServiceTwo call2ndLevelMethod ends ......");
        return "Done";
    }

    @Recover
    public String rteRecover(RuntimeException e, Integer id, String name){
        System.out.println("In rteRecover() : " + id + " - " + name);
        e.printStackTrace();
        return "recovered from rte";
    }
}

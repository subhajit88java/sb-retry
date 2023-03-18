package com.subhajit.sbretry.service;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class RetryService {

    @Retryable(value = NullPointerException.class)
    public String retryOnNullpointerException(){
        System.out.println("In service retryOnNullpointerException starts ......");
        String s = null;
        s.length();
        System.out.println("In service retryOnNullpointerException ends ......");
        return "Done";
    }

    @Retryable(value = NullPointerException.class, maxAttempts = 5, backoff = @Backoff(delay = 2000))
    public String retryOnNullpointerExceptionDelay(){
        System.out.println("In service retryOnNullpointerExceptionDelay starts ......");
        String s = null;
        s.length();
        System.out.println("In service retryOnNullpointerExceptionDelay ends ......");
        return "Done";
    }

    @Retryable(value = NullPointerException.class)
    public String retryOnNullpointerExceptionWithRecover(Integer id, String name){
        System.out.println("In service retryOnNullpointerExceptionWithRecover starts ......");
        String s = null;
        s.length();
        System.out.println("In service retryOnNullpointerExceptionWithRecover ends ......");
        return "Done";
    }

    @Recover
    public String recover(NullPointerException e, Integer id, String name){
        System.out.println("In recover() : " + id + " - " + name);
        e.printStackTrace();
        return "recovered";
    }
}

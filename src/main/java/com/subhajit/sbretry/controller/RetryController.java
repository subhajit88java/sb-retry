package com.subhajit.sbretry.controller;

import com.subhajit.sbretry.service.RetryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class RetryController {

    @Autowired
    private RetryTemplate retryTemplate;

    @Autowired
    private RetryService retryService;

    @GetMapping("/retry-npe")
    public String retryOnNullpointerException(){
        retryService.retryOnNullpointerException();
        return "SUCCESS";
    }

    @GetMapping("/retry-template-npe")
    public String retryTemplateOnNullpointerException(){
        String result = retryTemplate.execute(new RetryCallback<String, NullPointerException>(){
            @Override
            public String doWithRetry(RetryContext retryContext) throws NullPointerException {
                return retryService.retryTemplateOnNullpointerException("Subhajit");
            }
        });

        return result;
    }

    @GetMapping("/retry-template-aie")
    public String retryTemplateOnArrayIndexOutOfBoundsExceptionWithRecover(){
        String result = retryTemplate.execute(new RetryCallback<String, ArrayIndexOutOfBoundsException>() {
            @Override
            public String doWithRetry(RetryContext retryContext) throws NullPointerException {
                return retryService.retryTemplateOnArrayIndexOutOfBoundsException("Pallobi");
            }
        }, new RecoveryCallback<String>() {
            @Override
            public String recover(RetryContext retryContext) throws Exception {
                System.out.println("In aieRecover() : " + Arrays.asList(retryContext.attributeNames()));
                System.out.println("In aieRecover count: " + retryContext.getRetryCount());
                retryContext.getLastThrowable().printStackTrace();
                return "recovered from aie";
            }
        });

        return result;
    }

    @GetMapping("/retry-template-rte")
    public String retryTemplateOnRunTimeExceptionWithRecover(){
        String result = retryTemplate.execute(new RetryCallback<String, RuntimeException>() {
            @Override
            public String doWithRetry(RetryContext retryContext) throws NullPointerException {
                return retryService.retryTemplateOnRunTimeException("Rudrik");
            }
        }, new RecoveryCallback<String>() {
            @Override
            public String recover(RetryContext retryContext) throws Exception {
                System.out.println("In rteRecover() : " + retryContext.getAttribute("name"));
                System.out.println("In rteRecover count: " + retryContext.getRetryCount());
                retryContext.getLastThrowable().printStackTrace();
                return "recovered from rte";
            }
        });

        return result;
    }

    @GetMapping("/retry-template-ne")
    public String retryTemplateOnNoException(){
        String result = retryTemplate.execute(new RetryCallback<String, RuntimeException>(){
            @Override
            public String doWithRetry(RetryContext retryContext) throws NullPointerException {
                return retryService.retryTemplateOnNoException("Happy Family");
            }
        });

        return result;
    }

    @GetMapping("/retry-rte")
    public String retryOnRunTimeExceptionWith(){
        retryService.retryOnRunTimeException(3, "Rudrik");
        return "SUCCESS";
    }

    @GetMapping("/retry-npe-recover")
    public String retryOnNullpointerExceptionWithRecover(){
        retryService.retryOnNullpointerExceptionWithRecover(1, "Subhajit");
        return "SUCCESS";
    }

    @GetMapping("/retry-aie-recover")
    public String retryOnArrayIndexOutOfBoundsExceptionWithRecover(){
        retryService.retryOnArrayIndexOutOfBoundsExceptionWithRecover(2, "Pallobi");
        return "SUCCESS";
    }

    @GetMapping("/retry-npe-delay")
    public String retryOnNullpointerExceptionDelay(){
        System.out.println("In controller starts..........");
        retryService.retryOnNullpointerExceptionDelay();
        System.out.println("In controller ends..........");
        return "SUCCESS";
    }
}

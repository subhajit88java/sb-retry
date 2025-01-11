package com.subhajit.sbretry.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.sql.SQLException;

@Service
public class RetryService {

    private static Integer count = 0;

    @Autowired
    private RetryServiceTwo retryServiceTwo;

    @Autowired
    private RetryTemplate retryTemplate;

    // Works retry template, attempts, delay as well as type of exceptions are all configured in the retry-template
    public String retryTemplateOnNullpointerException(String name){
        System.out.println("In service retryTemplateOnNullpointerException starts ......" + name);
        String s = null;
        s.length();
        System.out.println("In service retryTemplateOnNullpointerException ends ......");
        return "Hello " + name;
    }

    // Works retry template, attempts, delay as well as type of exceptions are all configured in the retry-template
    // This will throw no exceptions
    public String retryTemplateOnNoException(String name){
        System.out.println("In service retryTemplateOnNoException starts ......" + name);
        System.out.println("In service retryTemplateOnNoException ends ......");
        return "Hello " + name;
    }

    public String retryTemplateOnArrayIndexOutOfBoundsException(String name){
        System.out.println("In service retryTemplateOnArrayIndexOutOfBoundsExceptionWithRecover starts ......" + name);
        String[] s = {"a", "b"};
        String obj = s[2];
        System.out.println("In service retryTemplateOnArrayIndexOutOfBoundsExceptionWithRecover ends ......");
        return "Hello " + name;
    }

    public String retryTemplateOnRunTimeException(String name){
        System.out.println("In service retryTemplateOnRunTimeExceptionWithRecover starts ......" + name);
        String s = "abc";
        s.charAt(5);
        System.out.println("In service retryTemplateOnRunTimeExceptionWithRecover ends ......");
        return "Hello " + name;
    }


    // Works without retry template, by default this will make 3(default + 2 retries) attempts with no delay between calls
    // If all retries are exhausted then the NPE will be delegated to the spring framework to handle
    @Retryable(value = NullPointerException.class)
    public String retryOnNullpointerException(){
        System.out.println("In service retryOnNullpointerException starts ......");
        System.out.println("Time : " + System.currentTimeMillis());
        String s = null;
        s.length();
        System.out.println("In service retryOnNullpointerException ends ......");
        return "Done";
    }

    // Works without retry template, will make attempts as mentioned with delay as mentioned between calls
    // If all retries are exhausted then the NPE will be delegated to the spring framework to handle
    @Retryable(value = NullPointerException.class, maxAttempts = 5, backoff = @Backoff(delay = 10000))
    public String retryOnNullpointerExceptionDelay(){
        System.out.println("In service retryOnNullpointerExceptionDelay starts ......");
        String s = null;
        System.out.println("Time : " + System.currentTimeMillis());
        s.length();
        System.out.println("In service retryOnNullpointerExceptionDelay ends ......");
        return "Done";
    }

    // Works without retry template, with multiple exceptions, by default this will make 3(default + 2 retries) attempts with no delay between calls
    // If any other exception is thrown, then retry wil not work
    // Both these exceptions will be counted under the retry count
    @Retryable(value = {NullPointerException.class, ArrayIndexOutOfBoundsException.class})
    public String retryOnMultipleException(){
        System.out.println("In service retryOnMultipleException starts ......");
        System.out.println("Time : " + System.currentTimeMillis());
        count++;
        String s = null;
        String[] array = {"1","2"};
        String var = "";
        if(count<2)
            s.length();
        else
            var = array[5];
        System.out.println("In service retryOnMultipleException ends ......");
        return "Done";
    }

    // Works without retry template, by default this will make 3 attempts with no delay between calls
    // If we catch the exception and handle it then retry will not work
    // If we re-throw the exception then retry will work.
    // This is because if we don't throw the exception, the call will not be passed to the AOP proxy
    @Retryable(value = NullPointerException.class)
    public String retryOnNullpointerCatchedException(){
        try {
            System.out.println("In service retryOnNullpointerCatchedException starts ......");
            String s = null;
            s.length();
            System.out.println("In service retryOnNullpointerCatchedException ends ......");
        }catch(Exception e){
            System.out.println("EXCEPTION CAUGHT........." +  e.getMessage());
            throw e;
        }
        return "Done";
    }

    // Works without retry template, by default this will make 3 attempts with no delay between calls
    // If the retry counts is exhausted then the flow will land in the corresponding recover method
    @Retryable(value = ArithmeticException.class)
    public String retryWithRecover(Integer id, String name){
        System.out.println("In service retryWithRecover starts ......");
        System.out.println("Time : " + System.currentTimeMillis());
        Integer var1 = 10;
        Integer var2 = 0;
        Integer result = var1/var2;
        System.out.println("In service retryWithRecover ends ......");
        return "Done";
    }

    // Works without retry template, with multiple exceptions, by default this will make 3 attempts with no delay between calls
    // At the last attempt the flow will pass to the recover method with the appropriate exception
    @Retryable(value = {NullPointerException.class, ArrayIndexOutOfBoundsException.class})
    public String retryOnMultipleExceptionWithRecover(Integer id, String name){
        System.out.println("In service retryOnMultipleExceptionWithRecover starts ......");
        String s = "";
        s.length();
        String[] array = {"1","2"};
        String var = array[5];
        System.out.println("In service retryOnMultipleExceptionWithRecover ends ......");
        return "Done";
    }

    @Retryable(value = ArrayIndexOutOfBoundsException.class)
    public String retryOnArrayIndexOutOfBoundsExceptionWithRecover(Integer id, String name){
        System.out.println("In service retryOnArrayIndexOutOfBoundsExceptionWithRecover starts ......");
        String[] s = {"a", "b"};
        String obj = s[2];
        System.out.println("In service retryOnArrayIndexOutOfBoundsExceptionWithRecover ends ......");
        return "Done";
    }
    @Retryable(value = SQLException.class)
    public void retryExhaust() throws SQLException {
        System.out.println("In service retryExhaust starts ......");
        throw new SQLException();
    }

    public String retryOnRunTimeException(Integer id, String name){
        System.out.println("In service retryOnRunTimeExceptionWithRecover starts ......");
        String s = "abc";
        s.charAt(5);
        System.out.println("In service retryOnRunTimeExceptionWithRecover ends ......");
        return "Done";
    }

    public String retryNpe2ndLevelMethodFail() {
        System.out.println("In service retryNpe2ndLevelMethod starts ......");
        return call2ndLevelMethod();
    }
    @Retryable(value = NullPointerException.class)
    public String retryNpe2ndLevelMethodPass1() {
        System.out.println("In service retryNpe2ndLevelMethodPass1 starts ......");
        return call2ndLevelMethod();
    }

    public String retryNpe2ndLevelMethodPass2() {
        System.out.println("In service retryNpe2ndLevelMethod starts ......");
        return retryServiceTwo.call2ndLevelMethod();
    }

    // This retry on 2nd level method will not work, reason being retry proxy wil only work of 1st level method which is called from a different class
    private String call2ndLevelMethod() {
        System.out.println("In service call2ndLevelMethod starts ......");
        String s = null;
        s.length();
        System.out.println("In service call2ndLevelMethod ends ......");
        return "Done";
    }

    // Recover method should have same return datatype as that of the actual method. Otherwise recovery method cannot be located
    // Method args are optional
    // If retry attempts are exhausted and recovery method is not located we get ExhaustRetryException
    // If @Recovery annotation is not present on any one of the method then all exceptions will be delegated as usual to spring framework upon retry exhaust
    // If @Recovery annotation is  present on any one of the method and teh actual method is not mapped to corresponding retry method i.e recover method not located then ExhaustRetryException will be delegated to spring framework
    @Recover
    public String aeRecover(ArithmeticException e, Integer id, String name){
        System.out.println("In aeRecover() : " + id + " - " + name);
        e.printStackTrace();
        return "recovered from AE";
    }

    @Recover
    public String aieRecover(ArrayIndexOutOfBoundsException e, Integer id, String name){
        System.out.println("In aieRecover() : " + id + " - " + name);
        e.printStackTrace();
        return "recovered from aie";
    }

    @Recover
    public String rteRecover(RuntimeException e, Integer id, String name){
        System.out.println("In rteRecover() : " + id + " - " + name);
        e.printStackTrace();
        return "recovered from rte";
    }

    public String retryTemplateFnf2ndLevelMethodFail() throws FileNotFoundException {
        System.out.println("In service retryTemplateFnf2ndLevelMethodFail starts ......");
        String result = retryTemplate.execute(new RetryCallback<String, FileNotFoundException>(){
            @Override
            public String doWithRetry(RetryContext retryContext) throws FileNotFoundException {
                return call2ndLevelMethodFnf();
            }
        });
        return result;
    }

    private String call2ndLevelMethodFnf() throws FileNotFoundException {
        System.out.println("In service call2ndLevelMethodFnf starts ......");
        System.out.println("Retry count : " + RetrySynchronizationManager.getContext().getRetryCount());
        throw new FileNotFoundException();
    }
}

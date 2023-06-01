package com.subhajit.sbretry.service;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class RetryService {

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


    // Works without retry template, by default this will make 3 attempts with no delay between calls
    @Retryable(value = NullPointerException.class)
    public String retryOnNullpointerException(){
        System.out.println("In service retryOnNullpointerException starts ......");
        String s = null;
        s.length();
        System.out.println("In service retryOnNullpointerException ends ......");
        return "Done";
    }

    // Works without retry template, will make attempts as mentioned with delay as mentioned between calls
    @Retryable(value = NullPointerException.class, maxAttempts = 5, backoff = @Backoff(delay = 10000))
    public String retryOnNullpointerExceptionDelay(){
        System.out.println("In service retryOnNullpointerExceptionDelay starts ......");
        String s = null;
        System.out.println("Time : " + System.currentTimeMillis());
        s.length();
        System.out.println("In service retryOnNullpointerExceptionDelay ends ......");
        return "Done";
    }

    // Works without retry template, with multiple exceptions, by default this will make 3 attempts with no delay between calls
    // If any other exception is thrown, then retry wil not work
    @Retryable(value = {NullPointerException.class, ArrayIndexOutOfBoundsException.class})
    public String retryOnMultipleException(){
        System.out.println("In service retryOnMultipleException starts ......");
        String s = null;
        s.length();
        String[] array = {"1","2"};
        String var = array[5];
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
    // At the last attempt the flow will pass to the recover method with the appropriate exception
    @Retryable(value = NullPointerException.class)
    public String retryOnNullpointerExceptionWithRecover(Integer id, String name){
        System.out.println("In service retryOnNullpointerExceptionWithRecover starts ......");
        String s = null;
        s.length();
        System.out.println("In service retryOnNullpointerExceptionWithRecover ends ......");
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

   // @Retryable(value = ArrayIndexOutOfBoundsException.class)
    public String retryOnArrayIndexOutOfBoundsExceptionWithRecover(Integer id, String name){
        System.out.println("In service retryOnArrayIndexOutOfBoundsExceptionWithRecover starts ......");
        String[] s = {"a", "b"};
        String obj = s[2];
        System.out.println("In service retryOnArrayIndexOutOfBoundsExceptionWithRecover ends ......");
        return "Done";
    }

    public String retryOnRunTimeException(Integer id, String name){
        System.out.println("In service retryOnRunTimeExceptionWithRecover starts ......");
        String s = "abc";
        s.charAt(5);
        System.out.println("In service retryOnRunTimeExceptionWithRecover ends ......");
        return "Done";
    }

    @Recover
    public String npeRecover(NullPointerException e, Integer id, String name){
        System.out.println("In npeRecover() : " + id + " - " + name);
        e.printStackTrace();
        return "recovered from NPE";
    }

    @Recover
    public String aieRecover(ArrayIndexOutOfBoundsException e, Integer id, String name){
        System.out.println("In aieRecover() : " + id + " - " + name);
        e.printStackTrace();
        return "recovered from aie";
    }
//
//    @Recover
//    public String rteRecover(RuntimeException e, Integer id, String name){
//        System.out.println("In rteRecover() : " + id + " - " + name);
//        e.printStackTrace();
//        return "recovered from rte";
//    }
}

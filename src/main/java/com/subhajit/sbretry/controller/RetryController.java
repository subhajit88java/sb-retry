package com.subhajit.sbretry.controller;

import com.subhajit.sbretry.service.RetryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RetryController {

    @Autowired
    private RetryService retryService;

    @GetMapping("/retry-npe")
    public String retryOnNullpointerException(){
        retryService.retryOnNullpointerException();
        return "SUCCESS";
    }

    @GetMapping("/retry-npe-recover")
    public String retryOnNullpointerExceptionWithRecover(){
        retryService.retryOnNullpointerExceptionWithRecover(1, "Subhajit");
        return "SUCCESS";
    }

    @GetMapping("/retry-npe-delay")
    public String retryOnNullpointerExceptionDelay(){
        retryService.retryOnNullpointerExceptionDelay();
        return "SUCCESS";
    }
}

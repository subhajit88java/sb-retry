package com.subhajit.sbretry.configuration;

import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffContext;
import org.springframework.retry.backoff.BackOffInterruptedException;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.AlwaysRetryPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RetryTemplateConfig {

    private final SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(5);
    private final NeverRetryPolicy neverRetryPolicy = new NeverRetryPolicy();
    private final AlwaysRetryPolicy alwaysRetryPolicy = new AlwaysRetryPolicy();

    @Bean(name="retryTemplate")
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        ExceptionClassifierRetryPolicy policy = new ExceptionClassifierRetryPolicy();
        policy.setExceptionClassifier(configureStatusCodeBasedRetryPolicy());
        retryTemplate.setRetryPolicy(policy);

        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(10000);
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);


        return retryTemplate;
    }

    private Classifier<Throwable, RetryPolicy> configureStatusCodeBasedRetryPolicy() {
        return throwable -> {
            if (throwable instanceof NullPointerException) {
                return alwaysRetryPolicy;
            }else if (throwable instanceof ArrayIndexOutOfBoundsException) {
                return neverRetryPolicy;
            }else
                return simpleRetryPolicy;
        };
    }

}

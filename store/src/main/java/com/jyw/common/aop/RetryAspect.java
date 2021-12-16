package com.jyw.common.aop;

import com.jyw.common.annotation.Retry;
import com.jyw.common.exception.RetryException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Slf4j
@Aspect
@Component
public class RetryAspect {
    @Pointcut("@annotation(com.jyw.common.annotation.Retry)")
    public  void  retryPointCut(){

    }

    @Around("retryPointCut() && @annotation(retry)")
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public  Object tryAgain(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
        int count=0;

        do {
            count++;
            try{
                log.info("更新。。");
                if(count==2)
                    log.info("更新失败，数据已被修改，正在重试。。。");
                if(count>1)
                    log.info("重试次数:{}",count-1);
                return joinPoint.proceed();
            } catch (RetryException throwable) {
                if(count>retry.value()){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    log.info(throwable.getMessage());
                    throw throwable;
                }
            }
        }
        while (true);
    }

}
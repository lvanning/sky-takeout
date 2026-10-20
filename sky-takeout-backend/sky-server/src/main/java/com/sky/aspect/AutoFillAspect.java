package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {
        
        @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
        public void autoFillPointCut(){}
        
        @Before("autoFillPointCut()")
        public void autoFill(JoinPoint joinPoint){
                log.info("before执行");
                Object[] args = joinPoint.getArgs();
                if(args == null || args.length == 0){return;}//无参，往哪里自动填充？被标注的正常都应该有参数
                
                MethodSignature signature = (MethodSignature) joinPoint.getSignature();//切入点方法签名
                AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获取对应的注解
                OperationType type = autoFill.type();//在注解里获取操作类型
                
                Object entity = args[0];//约定把实体类放在参数第一位
                
                LocalDateTime now = LocalDateTime.now();
                Long id = BaseContext.getCurrentId();
                
                if(type == OperationType.INSERT){
                        try {
                                Method setCreatUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                                Method setCreatTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                                
                                setCreatUser.invoke(entity, id);
                                setCreatTime.invoke(entity, now);
                                setUpdateTime.invoke(entity, now);
                                setUpdateUser.invoke(entity, id);
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                }else if(type == OperationType.UPDATE){
                        try {
                                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                                setUpdateTime.invoke(entity, now);
                                setUpdateUser.invoke(entity, id);
                        } catch (Exception e) {
                                e.printStackTrace();
                        }

                }
        }
}

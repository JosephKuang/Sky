package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.context.BaseContext;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

import static com.sky.constant.AutoFillConstant.*;

/**
 * 自定义切面，实现公共字段自动填充处理逻辑
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 切入点
     */

    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointcut() {
    }

    @Before("autoFillPointcut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段自动填充");
        //获取到当前被拦截的方法上的数据库操作类型
        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // 获取目标方法对象
        Method method = signature.getMethod();

        // 获取 @AutoFill 注解
        AutoFill autoFill = method.getAnnotation(AutoFill.class);

        // 获得数据库操作类型
        OperationType operationType = autoFill.value();

        //获取到当前被拦截的方法的参数--实体对象
        Object[] args = joinPoint.getArgs();
        Object entity = args[0];


        //准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentID = BaseContext.getCurrentId();

        //根据当前不同的操作类型，为对应的属性通过反射来赋值
        if (operationType == OperationType.INSERT) {
            //为四个公共字段赋值
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(SET_UPDATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(SET_CREATE_USER, Long.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(SET_UPDATE_USER, Long.class);

                //通过反射为对象属性赋值
                setCreateTime.invoke(entity,now);
                setUpdateTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentID);
                setUpdateUser.invoke(entity,currentID);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        } else if (operationType == OperationType.UPDATE) {
            //为两个公共字段赋值
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(SET_UPDATE_TIME, LocalDateTime.class);

                Method setUpdateUser = entity.getClass().getDeclaredMethod(SET_UPDATE_USER, Long.class);

                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentID);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        log.info("{}",entity);
    }
}

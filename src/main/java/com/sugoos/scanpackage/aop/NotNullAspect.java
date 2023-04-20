package com.sugoos.scanpackage.aop;


import com.sugoos.scanpackage.annotation.NotNullParams;
import com.sugoos.scanpackage.dto.RestResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;
import java.lang.reflect.Field;

@Aspect
@Component
public class NotNullAspect {

    @Around("@annotation(com.sugoos.scanpackage.annotation.NotNullParams)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object[] args = joinPoint.getArgs();
            for (Object arg : args) {
                // 判断参数是否为空
                if (arg == null) throw new IllegalArgumentException("参数不能为空");
                // 如果参数是一个对象，就遍历对象的属性
                validateParams(arg);
            }
            return joinPoint.proceed();
        } catch (IllegalArgumentException e) {
            String errorMsg = e.getMessage() != null ? e.getMessage() : "参数不能为空";
            return RestResult.fail(errorMsg);
        }
    }

    /**
     * 验证参数是否为空以及属性是否带有@NotNullParams注解
     */
    private static void validateParams(Object arg) throws IllegalAccessException {
        Field[] fields = arg.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 判断属性是否带有@NotNullParams注解
            NotNullParams notNullParams = field.getAnnotation(NotNullParams.class);
            if (notNullParams != null) {
                field.setAccessible(true); // 设置为可访问的
                try {
                    Object fieldValue = field.get(arg); // 获取属性值
                    if (fieldValue == null) {
                        String errorMsg = notNullParams.msg();
                        throw new IllegalArgumentException(errorMsg);
                    }
                } catch (IllegalAccessException e) {
                    String errorMsg = notNullParams.msg();
                    throw new IllegalArgumentException(errorMsg);
                }
            }
        }
    }

}

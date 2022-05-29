package com.jy.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解：声明初始化质心方法类型变量
 *
 * @author jinchunzhao
 * @version 1.0
 * @date 2022-05-29 11:25
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClusterArithmeticRegion {

    String factionType() default "random";
}
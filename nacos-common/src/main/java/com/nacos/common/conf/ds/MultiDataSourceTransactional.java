package com.nacos.common.conf.ds;

import java.lang.annotation.*;


/**
 * 多数据源事务注解
 *
 * @author guos
 * @date 2021/2/8 10:45
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface MultiDataSourceTransactional {

    /**
     * 事务管理器数组
     */
    String[] transactionManagers();
}

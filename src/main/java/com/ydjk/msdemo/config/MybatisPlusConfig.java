package com.ydjk.msdemo.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.ydjk.msdemo.injector.CustomizedSqlInjector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement //开启mybatis事物管理
public class MybatisPlusConfig {

    /**
     * 分页插件。不配置，分页插件将不生效
     */
    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor pageInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        pageInterceptor.setMaxLimit(1000L);
        // 指定数据库为 MYSQL
        interceptor.addInnerInterceptor(pageInterceptor);
        return interceptor;
    }
    @Bean
    public CustomizedSqlInjector customizedSqlInjector() {
        return new CustomizedSqlInjector();
    }
}

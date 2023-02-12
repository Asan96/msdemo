package com.ydjk.msdemo.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration //声明该类为配置类
@EnableSwagger2 //声明启动Swagger2
//@ConditionalOnProperty(name = "swagger.enable", havingValue = "false")
public class SwaggerConfig {
    @Bean
    public Docket customDocket() {
        // 添加 head 参数配置 start
        ParameterBuilder token = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        token.name("Authorization").description("token 信息").modelRef(new ModelRef("String"))
                .parameterType("header").required(false).build();
        pars.add(token.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ydjk.msdemo.controller"))//扫描的包路径
                .build()
                .globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("demo")//文档说明
                .version("1.0.0")//文档版本说明
                .build();
    }
}
package com.mvchibernate.config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

//    @Bean
//    public Docket mainapi(){
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("maincontroller")
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.mvchibernate.controller"))
//                .paths(paths())
//                .build().
//                apiInfo(apiInfo());
//
//    }
//
//    @Bean
//    public Docket firstapi(){
//
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("secondcontroller")
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.mvchibernate.controller.pack"))
//                .paths(PathSelectors.ant("/adminpack"))
//                .build().
//                apiInfo(apiInfo());
//
//    }
//
//    @Bean
//    public Docket secondapi(){
//
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("thirdcontroller")
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.mvchibernate.controller.pack2"))
//                .build().
//                apiInfo(apiInfo());
//
//    }

//    Docket docket = new Docket(DocumentationType.OAS_30)
//            .select() //
//            .apis(RequestHandlerSelectors.basePackage("com.mvchibernate.controller") //
//                    .or(RequestHandlerSelectors.basePackage(bPackageBase))) // use or here, not and
//            .paths(PathSelectors.any())
//            .build();

    @Bean
    public Docket api(){ return new Docket(DocumentationType.SWAGGER_2)
            .select() .apis(RequestHandlerSelectors.basePackage("com.mvchibernate.controller")).build().apiInfo(apiInfo()); }


    private ApiInfo apiInfo() { return new ApiInfoBuilder() .title("TITLE")
            .description("DESCRIPTION") .version("VERSION")
            .termsOfServiceUrl("http://terms-of-services.url") .license("LICENSE")
            .licenseUrl("http://url-to-license.com") .build(); }

    private Predicate<String> paths() {

        System.out.println("inside new method");
        return or(PathSelectors.regex("/admin.*"),
                PathSelectors.regex("/user.*"));
    }
}
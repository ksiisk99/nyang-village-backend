package com.ay.talk.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport{
	private static final String API_NAME="냥빌리지 API";
	private static final String API_VERSION="1.0";
	private static final String API_DESCRIPTION="API 명세서";
	
	
	@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
        		.consumes(getConsumeContentTypes())
        		.produces(getProduceContentTypes())
	        	.apiInfo(apiInfo())
	        	.select()                                  
	        	.apis(RequestHandlerSelectors.basePackage("com.ay.talk"))              
	        	.paths(PathSelectors.ant("/**"))                       
	        	.build()
	        	.groupName("NY DOC");                                           
    }
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("/swagger-ui.html")
	      .addResourceLocations("classpath:/META-INF/resources/");

	    registry.addResourceHandler("/webjars/**")
	      .addResourceLocations("classpath:/META-INF/resources/webjars/");
	    super.addResourceHandlers(registry);
	}
	
	public ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title(API_NAME)
				.version(API_VERSION)
				.description(API_DESCRIPTION)
				.build();
	}
	
    private Set<String> getConsumeContentTypes(){        
    	Set<String> consumes = new HashSet<>();        
    	consumes.add("application/json;charset=UTF-8");        
    	consumes.add("application/x-www-form-urlencoded");        
    	return consumes;    
    }

	
    private Set<String> getProduceContentTypes(){        
    	Set<String> produces = new HashSet<>();        
    	produces.add("application/json;charset=UTF-8");       
    	return produces;    
    }

}

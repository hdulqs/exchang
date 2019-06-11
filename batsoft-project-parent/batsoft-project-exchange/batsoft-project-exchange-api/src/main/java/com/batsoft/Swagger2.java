package com.batsoft;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Yuan
 * http://localhost:8080/swagger-ui.html
 * @version 1.0.0
 * @date 16/12/10 下午12:02
 */
@Configuration
@ConditionalOnProperty(value = "swagger.enable",havingValue = "true")
@EnableSwagger2
public class Swagger2 {

	@Value("${batsoft.Swagger2.title}")
	private  String Swagger2_title;
	@Value("${batsoft.Swagger2.description}")
	private String Swagger2_description;
	@Value("${batsoft.Swagger2.termsOfServiceUrl}")
	private String Swagger2_termsOfServiceUrl;
	@Value("${batsoft.Swagger2.contact}")
	private String Swagger2_contact;
	@Value("${batsoft.Swagger2.version}")
	private String Swagger2_version;
	@Value("${batsoft.Swagger2.basePackage}")
	private String Swagger2_basePackage;

	@Bean
	public Docket createRestApi() {
		ParameterBuilder ticketPar = new ParameterBuilder();
		List<Parameter> pars = new ArrayList<Parameter>();
		ticketPar.name("token").description("user ticket")
				.modelRef(new ModelRef("string")).parameterType("header")
				.required(false).build(); //header中的ticket参数非必填，传空也可以
		pars.add(ticketPar.build());

		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.batsoft"))
				.paths(PathSelectors.any())
				.build();
//				.globalOperationParameters(pars);
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title(Swagger2_title)
				.description(Swagger2_description)
				.termsOfServiceUrl(Swagger2_termsOfServiceUrl)
				.contact(Swagger2_contact)
				.version(Swagger2_version)
				.build();
	}


}

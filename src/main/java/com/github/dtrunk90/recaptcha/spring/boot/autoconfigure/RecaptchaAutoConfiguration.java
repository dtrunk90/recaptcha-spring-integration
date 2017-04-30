package com.github.dtrunk90.recaptcha.spring.boot.autoconfigure;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.github.dtrunk90.recaptcha.spring.web.method.RecaptchaServletModelAttributeMethodProcessor;
import com.github.dtrunk90.recaptcha.spring.web.servlet.handler.RecaptchaHandlerInterceptor;

@Configuration
public class RecaptchaAutoConfiguration {

	@Configuration
	public static class RecaptchaWebMvcConfiguration extends WebMvcConfigurerAdapter {

		@Bean
		public HandlerMethodArgumentResolver recaptchaServletModelAttributeMethodProcessor() {
			return new RecaptchaServletModelAttributeMethodProcessor();
		}

		@Bean
		public HandlerInterceptor recaptchaHandlerInterceptor() {
			return new RecaptchaHandlerInterceptor();
		}

		@Override
		public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
			argumentResolvers.add(recaptchaServletModelAttributeMethodProcessor());
		}

		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			registry.addInterceptor(recaptchaHandlerInterceptor());
		}

	}

	@Configuration
	@ConditionalOnMissingBean(RestTemplate.class)
	public static class RestTemplateConfiguration {
		@Bean
		public RestTemplate restTemplate() {
			return new RestTemplate();
		}
	}

}

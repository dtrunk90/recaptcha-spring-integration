package com.github.dtrunk90.recaptcha.spring.boot.autoconfigure;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
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
@ConditionalOnWebApplication
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class RecaptchaAutoConfiguration {

	private static final Logger log = LoggerFactory.getLogger(RecaptchaAutoConfiguration.class);

	@Bean
	@ConditionalOnMissingBean(RecaptchaServletModelAttributeMethodProcessor.class)
	public HandlerMethodArgumentResolver recaptchaServletModelAttributeMethodProcessor() {
		return new RecaptchaServletModelAttributeMethodProcessor();
	}

	@Bean
	@ConditionalOnMissingBean(RecaptchaHandlerInterceptor.class)
	public HandlerInterceptor recaptchaHandlerInterceptor() {
		return new RecaptchaHandlerInterceptor();
	}

	@Configuration
	public static class RecaptchaWebMvcConfiguration extends WebMvcConfigurerAdapter {

		@Autowired
		private HandlerMethodArgumentResolver recaptchaServletModelAttributeMethodProcessor;

		@Autowired
		private HandlerInterceptor recaptchaHandlerInterceptor;

		@Override
		public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
			log.warn("########## adding recaptchaServletModelAttributeMethodProcessor: {}",
					recaptchaServletModelAttributeMethodProcessor);
			argumentResolvers.add(recaptchaServletModelAttributeMethodProcessor);
		}

		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			log.warn("########## adding recaptchaHandlerInterceptor: {}", recaptchaHandlerInterceptor);
			registry.addInterceptor(recaptchaHandlerInterceptor);
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

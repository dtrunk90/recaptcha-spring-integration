package com.github.dtrunk90.recaptcha.spring.boot.autoconfigure;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
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
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.github.dtrunk90.recaptcha.spring.web.method.RecaptchaServletModelAttributeMethodProcessor;
import com.github.dtrunk90.recaptcha.spring.web.servlet.handler.RecaptchaHandlerInterceptor;

@Configuration
@ConditionalOnWebApplication
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
public class RecaptchaAutoConfiguration {

	@Configuration
	public static class RecaptchaWebMvcConfiguration extends WebMvcConfigurerAdapter {

		@Bean
		public BeanPostProcessor recaptchaRequestMappingHandlerAdapterBeanPostProcessor() {
			return new BeanPostProcessor() {
				@Override
				public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
					return bean;
				}

				@Override
				public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
					if (bean instanceof RequestMappingHandlerAdapter) {
						RequestMappingHandlerAdapter adapter = (RequestMappingHandlerAdapter) bean;
						List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<HandlerMethodArgumentResolver>(adapter.getArgumentResolvers());
						argumentResolvers.add(0, new RecaptchaServletModelAttributeMethodProcessor(adapter, false));
						adapter.setArgumentResolvers(argumentResolvers);
					}

					return bean;
				}
			};
		}

		@Bean
		@ConditionalOnMissingBean(RecaptchaHandlerInterceptor.class)
		public HandlerInterceptor recaptchaHandlerInterceptor() {
			return new RecaptchaHandlerInterceptor();
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

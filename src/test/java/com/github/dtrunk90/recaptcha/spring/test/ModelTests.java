package com.github.dtrunk90.recaptcha.spring.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import com.github.dtrunk90.recaptcha.spring.web.servlet.handler.RecaptchaHandlerInterceptor;

public class ModelTests extends AbstractRecaptchaSpringIntegrationTests {

	@Value("${recaptcha.site-key}")
	private String recaptchaSiteKey;

	@Test
	public void hasModelAttributeRecaptchaSiteKey() throws Exception {
		mockMvc.perform(get("/test")).andExpect(
				model().attribute(RecaptchaHandlerInterceptor.RECAPTCHA_SITE_KEY_ATTRIBUTE_NAME, recaptchaSiteKey));
	}

}

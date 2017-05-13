package com.github.dtrunk90.recaptcha.spring.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.test.context.TestPropertySource;

import com.github.dtrunk90.recaptcha.spring.web.method.RecaptchaServletModelAttributeMethodProcessor;

@RunWith(Enclosed.class)
public abstract class ValidationTests {

	private static final String RECAPTCHA_RESPONSE_PARAMETER_VALUE = "03AIezHSbPAzsttFF0vagn-V5yy_WrmstSNbkqgcQThGAuUb"
			+ "S6lrHQQOh8XNiQEmR_WGkJYpfNppoxZQ8xFht4g8ybMRqNq1tvXD1JUNX0ALmPlyKWoUreMMrBKKAVzHYXn8Uwm8D_LYnvoXMAwxDEJ"
			+ "QdJmZeCCovPG8isjYDdUeZIfvfk_Qbb-_4lTlK6IQeIbhF8cedMl2hQvKRUOIN43-9XYAtgjADMK9fbPZT-tp6dyQO9eH7GcfLZg8kw"
			+ "DYw1niArGu4nptPooj2l5xP4IFobG4o4djosEkoPFinXMHclnBnoNdMcV8A";

	@TestPropertySource(properties = "recaptcha.secret-key=6LeIxAcTAAAAAGG-vFI1TnRWxMZNFuojJ4WifJWe")
	public static class ValidationValidTests extends AbstractRecaptchaSpringIntegrationTests {

		@Test
		public void postTest_hasNoErrors() throws Exception {
			mockMvc.perform(
					post("/test").param(RecaptchaServletModelAttributeMethodProcessor.RECAPTCHA_RESPONSE_PARAMETER_NAME,
							RECAPTCHA_RESPONSE_PARAMETER_VALUE))
					.andExpect(model().attributeHasNoErrors("testForm"));
		}

	}

	@TestPropertySource(properties = "recaptcha.secret-key=test")
	public static class ValidationInvalidTests extends AbstractRecaptchaSpringIntegrationTests {

		@Test
		public void postTest_hasFieldError() throws Exception {
			mockMvc.perform(
					post("/test").param(RecaptchaServletModelAttributeMethodProcessor.RECAPTCHA_RESPONSE_PARAMETER_NAME,
							RECAPTCHA_RESPONSE_PARAMETER_VALUE))
					.andExpect(model().attributeHasFieldErrorCode("testForm", "recaptchaResponse", "Recaptcha"));
		}

	}

}

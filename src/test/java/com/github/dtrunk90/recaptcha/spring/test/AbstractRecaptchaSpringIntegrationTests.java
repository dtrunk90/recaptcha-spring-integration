package com.github.dtrunk90.recaptcha.spring.test;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.github.dtrunk90.recaptcha.spring.boot.autoconfigure.RecaptchaAutoConfiguration;
import com.github.dtrunk90.recaptcha.spring.test.configuration.TestConfiguration;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(properties = "recaptcha.site-key=test")
@ContextConfiguration(classes = { RecaptchaAutoConfiguration.class, TestConfiguration.class })
public abstract class AbstractRecaptchaSpringIntegrationTests {

	@Autowired
	private WebApplicationContext context;

	protected MockMvc mockMvc;

	@Before
	public void init() {
		mockMvc = webAppContextSetup(context).build();
	}

}

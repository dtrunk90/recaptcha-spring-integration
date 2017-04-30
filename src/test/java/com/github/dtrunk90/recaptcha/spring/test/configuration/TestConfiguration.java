package com.github.dtrunk90.recaptcha.spring.test.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
@ComponentScan("com.github.dtrunk90.recaptcha.spring.test")
public class TestConfiguration {
}

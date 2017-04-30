package com.github.dtrunk90.recaptcha.spring.web.servlet.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class RecaptchaHandlerInterceptor extends HandlerInterceptorAdapter {

	public static final String RECAPTCHA_SITE_KEY_ATTRIBUTE_NAME = "recaptchaSiteKey";

	@Value("${recaptcha.site-key}")
	private String recaptchaSiteKey;

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
		if (modelAndView != null) {
			modelAndView.addObject(RECAPTCHA_SITE_KEY_ATTRIBUTE_NAME, recaptchaSiteKey);
		}
	}

}

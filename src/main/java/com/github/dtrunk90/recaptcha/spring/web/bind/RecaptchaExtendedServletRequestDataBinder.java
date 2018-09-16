package com.github.dtrunk90.recaptcha.spring.web.bind;

import javax.servlet.ServletRequest;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

public class RecaptchaExtendedServletRequestDataBinder extends ExtendedServletRequestDataBinder {

	public static final String RECAPTCHA_RESPONSE_PARAMETER_NAME = "g-recaptcha-response";

	private final String recaptchaFieldName;

	public RecaptchaExtendedServletRequestDataBinder(Object target, String objectName, String recaptchaFieldName) {
		super(target, objectName);
		this.recaptchaFieldName = recaptchaFieldName;
	}

	@Override
	protected void addBindValues(MutablePropertyValues mpvs, ServletRequest request) {
		super.addBindValues(mpvs, request);

		if (mpvs.contains(RECAPTCHA_RESPONSE_PARAMETER_NAME)) {
			mpvs.add(recaptchaFieldName, mpvs.getPropertyValue(RECAPTCHA_RESPONSE_PARAMETER_NAME).getValue());
		}
	}

}

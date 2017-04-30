package com.github.dtrunk90.recaptcha.spring.web.bind;

import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

public class RecaptchaExtendedServletRequestDataBinder extends ExtendedServletRequestDataBinder {

	private final Map.Entry<String, String> recaptchaFieldMapping;

	public RecaptchaExtendedServletRequestDataBinder(Object target, String objectName, Map.Entry<String, String> recaptchaFieldMapping) {
		super(target, objectName);
		this.recaptchaFieldMapping = recaptchaFieldMapping;
	}

	@Override
	protected void addBindValues(MutablePropertyValues mpvs, ServletRequest request) {
		super.addBindValues(mpvs, request);

		String alias = recaptchaFieldMapping.getKey();
		if (mpvs.contains(alias)) {
			mpvs.add(recaptchaFieldMapping.getValue(), mpvs.getPropertyValue(alias));
		}
	}

}

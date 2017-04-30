package com.github.dtrunk90.recaptcha.spring.model;

import com.github.dtrunk90.recaptcha.spring.validation.constraints.Recaptcha;

public abstract class AbstractRecaptchaForm {

	@Recaptcha
	private String recaptchaResponse;

	public String getRecaptchaResponse() {
		return recaptchaResponse;
	}

	public void setRecaptchaResponse(String recaptchaResponse) {
		this.recaptchaResponse = recaptchaResponse;
	}

}

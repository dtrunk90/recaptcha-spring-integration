package com.github.dtrunk90.recaptcha.spring.validation.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dtrunk90.recaptcha.spring.validation.RecaptchaValidator;
import com.github.dtrunk90.recaptcha.spring.web.bind.RecaptchaExtendedServletRequestDataBinder;

@Documented
@Retention(RUNTIME)
@Target({ ANNOTATION_TYPE, FIELD })
@Constraint(validatedBy = RecaptchaValidator.class)
@JsonProperty(RecaptchaExtendedServletRequestDataBinder.RECAPTCHA_RESPONSE_PARAMETER_NAME)
public @interface Recaptcha {

	String message() default "{com.github.dtrunk90.recaptcha.spring.validation.constraints.Recaptcha.message}";

	Class<? extends Payload>[] payload() default {};

	boolean skipAuthenticated() default true;

	Class<?>[] groups() default {};

}

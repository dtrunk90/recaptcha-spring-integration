package com.github.dtrunk90.recaptcha.spring.validation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ClassUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dtrunk90.recaptcha.spring.validation.constraints.Recaptcha;

public class RecaptchaValidator implements ConstraintValidator<Recaptcha, String> {

	private static final Logger log = LoggerFactory.getLogger(RecaptchaValidator.class);

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private RestOperations restTemplate;

	@Value("${recaptcha.verify-url:https://www.google.com/recaptcha/api/siteverify}")
	private String recaptchaVerifyUrl;

	@Value("${recaptcha.secret-key}")
	private String recaptchaSecretKey;

	private boolean skipAuthenticated;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonPropertyOrder({ "success", "challenge_ts", "hostname", "error-codes" })
	private static class VerifyResponse {

		@JsonProperty("success")
		private boolean success;

		@JsonProperty("challenge_ts")
		private String challengeTs;

		@JsonProperty("hostname")
		private String hostname;

		@JsonProperty("error-codes")
		private Collection<ErrorCode> errorCodes;

		@JsonIgnore
		public boolean hasSecretError() {
			if (errorCodes == null) {
				return false;
			}

			for (ErrorCode error : errorCodes) {
				if (ErrorCode.MissingSecret.equals(error) || ErrorCode.InvalidSecret.equals(error)) {
					return true;
				}
			}

			return false;
		}

		public static enum ErrorCode {
			MissingSecret, InvalidSecret, MissingResponse, InvalidResponse;

			private static Map<String, ErrorCode> errorsMap = new HashMap<String, ErrorCode>(4);

			static {
				errorsMap.put("missing-input-secret", MissingSecret);
				errorsMap.put("invalid-input-secret", InvalidSecret);
				errorsMap.put("missing-input-response", MissingResponse);
				errorsMap.put("invalid-input-response", InvalidResponse);
			}

			@JsonCreator
			public static ErrorCode forValue(String value) {
				return errorsMap.get(value.toLowerCase());
			}
		}

		@Override
		public String toString() {
			return String.format("VerifyResponse{success=%s, challenge_ts=%s, hostname=%s, error-codes=%s}", success,
					challengeTs, hostname, errorCodes);
		}
	}

	@Override
	public void initialize(Recaptcha constraintAnnotation) {
		skipAuthenticated = constraintAnnotation.skipAuthenticated();
	}

	@Override
	public boolean isValid(String recaptchaResponse, ConstraintValidatorContext context) {
		if (skipAuthenticated && isAuthenticated()) {
			return true;
		}

		try {
			MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
			parameters.add("secret", recaptchaSecretKey);
			parameters.add("response", recaptchaResponse);
			parameters.add("remoteip", request.getRemoteAddr());

			VerifyResponse verifyResponse = restTemplate.postForObject(recaptchaVerifyUrl, parameters, VerifyResponse.class);

			if (log.isInfoEnabled()) {
				parameters.remove("secret");
				parameters.add("secret", recaptchaSecretKey.replaceAll(".", "*"));
				log.info("reCAPTCHA Verification Request sent to {}: {}", recaptchaVerifyUrl, parameters);
			}

			log.info("reCAPTCHA Verification Response: {}", verifyResponse);

			if (verifyResponse.hasSecretError()) {
				log.error("reCAPTCHA Verification failed. Please check your secret key.");
			}

			return verifyResponse.success;
		} catch (RestClientException e) {
			log.error("reCAPTCHA Verification failed. Exception occured:", e);
		}

		return false;
	}

	private boolean isAuthenticated() {
		if (!ClassUtils.isPresent("org.springframework.security.core.context.SecurityContextHolder", getClass().getClassLoader())) {
			return false;
		}

		return SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				&& !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
	}

}

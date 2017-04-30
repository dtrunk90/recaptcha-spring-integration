package com.github.dtrunk90.recaptcha.spring.web.method;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import com.github.dtrunk90.recaptcha.spring.validation.constraints.Recaptcha;
import com.github.dtrunk90.recaptcha.spring.web.bind.RecaptchaExtendedServletRequestDataBinder;

public class RecaptchaServletModelAttributeMethodProcessor extends ServletModelAttributeMethodProcessor {

    public static final String RECAPTCHA_RESPONSE_PARAMETER_NAME = "g-recaptcha-response";

	@Autowired
	private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

	private final Map<Class<?>, Map.Entry<String, String>> recaptchaFieldMap = new ConcurrentHashMap<Class<?>, Map.Entry<String, String>>();

	public RecaptchaServletModelAttributeMethodProcessor() {
		super(true);
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return super.supportsParameter(parameter);
	}

	@Override
	protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
		Object target = binder.getTarget();
		Class<?> targetClass = target.getClass();

		Map.Entry<String, String> recaptchaFieldMapping = recaptchaFieldMap.get(targetClass);
		if (recaptchaFieldMapping == null) {
			recaptchaFieldMapping = analyzeClass(targetClass);
			recaptchaFieldMap.put(targetClass, recaptchaFieldMapping);
		}

		RecaptchaExtendedServletRequestDataBinder a = new RecaptchaExtendedServletRequestDataBinder(target, binder.getObjectName(), recaptchaFieldMapping);
		requestMappingHandlerAdapter.getWebBindingInitializer().initBinder(a, request);

		super.bindRequestParameters(binder, request);
	}

	private static Map.Entry<String, String> analyzeClass(Class<?> targetClass) {
		for (Field field : targetClass.getDeclaredFields()) {
			Recaptcha recaptchaAnnotation = field.getAnnotation(Recaptcha.class);

			if (recaptchaAnnotation != null) {
				return new AbstractMap.SimpleImmutableEntry<String, String>(RECAPTCHA_RESPONSE_PARAMETER_NAME, field.getName());
			}
		}

		return null;
	}

}

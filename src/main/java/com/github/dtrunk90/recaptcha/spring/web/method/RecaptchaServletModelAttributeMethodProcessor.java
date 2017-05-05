package com.github.dtrunk90.recaptcha.spring.web.method;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import com.github.dtrunk90.recaptcha.spring.validation.constraints.Recaptcha;
import com.github.dtrunk90.recaptcha.spring.web.bind.RecaptchaExtendedServletRequestDataBinder;

public class RecaptchaServletModelAttributeMethodProcessor extends ServletModelAttributeMethodProcessor {

	public static final String RECAPTCHA_RESPONSE_PARAMETER_NAME = "g-recaptcha-response";

	private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

	private final Map<Class<?>, Map.Entry<String, String>> recaptchaFieldMap = new ConcurrentHashMap<Class<?>, Map.Entry<String, String>>();

	public RecaptchaServletModelAttributeMethodProcessor(RequestMappingHandlerAdapter requestMappingHandlerAdapter, boolean annotationNotRequired) {
		super(annotationNotRequired);
		this.requestMappingHandlerAdapter = requestMappingHandlerAdapter;
	}

	@Override
	protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
		Object target = binder.getTarget();
		Class<?> targetClass = target.getClass();

		Map.Entry<String, String> recaptchaFieldMapping = recaptchaFieldMap.get(targetClass);

		if (recaptchaFieldMapping == null) {
			recaptchaFieldMapping = analyzeClass(targetClass);

			if (recaptchaFieldMapping != null) {
				recaptchaFieldMap.put(targetClass, recaptchaFieldMapping);
			}
		}

		if (recaptchaFieldMapping != null) {
			binder = new RecaptchaExtendedServletRequestDataBinder(target, binder.getObjectName(), recaptchaFieldMapping);
			requestMappingHandlerAdapter.getWebBindingInitializer().initBinder(binder, request);
		}

		super.bindRequestParameters(binder, request);
	}

	private static Map.Entry<String, String> analyzeClass(Class<?> targetClass) {
		for (Field field : targetClass.getDeclaredFields()) {
			Recaptcha recaptchaAnnotation = field.getAnnotation(Recaptcha.class);

			if (recaptchaAnnotation != null) {
				return new AbstractMap.SimpleImmutableEntry<String, String>(field.getName(), RECAPTCHA_RESPONSE_PARAMETER_NAME);
			}
		}

		for (Method method : targetClass.getDeclaredMethods()) {
			Recaptcha recaptchaAnnotation = method.getAnnotation(Recaptcha.class);

			if (recaptchaAnnotation != null) {
				try {
					Field field = targetClass.getField(StringUtils.uncapitalize(method.getName().substring(3)));
					return new AbstractMap.SimpleImmutableEntry<String, String>(field.getName(), RECAPTCHA_RESPONSE_PARAMETER_NAME);
				} catch (NoSuchFieldException e) {
				}
			}
		}

		Class<?> targetSuperclass = targetClass.getSuperclass();
		if (targetSuperclass != null) {
			return analyzeClass(targetSuperclass);
		}

		return null;
	}

}

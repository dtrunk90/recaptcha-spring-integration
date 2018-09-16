package com.github.dtrunk90.recaptcha.spring.web.method;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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

	private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

	private final Map<Class<?>, String> recaptchaFieldMap = new ConcurrentHashMap<>();

	public RecaptchaServletModelAttributeMethodProcessor(RequestMappingHandlerAdapter requestMappingHandlerAdapter, boolean annotationNotRequired) {
		super(annotationNotRequired);
		this.requestMappingHandlerAdapter = requestMappingHandlerAdapter;
	}

	@Override
	protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
		Object target = binder.getTarget();
		Class<?> targetClass = target.getClass();

		String recaptchaFieldName = recaptchaFieldMap.get(targetClass);

		if (recaptchaFieldName == null) {
			recaptchaFieldName = analyzeClass(targetClass);

			if (recaptchaFieldName != null) {
				recaptchaFieldMap.put(targetClass, recaptchaFieldName);
			}
		}

		if (recaptchaFieldName != null) {
			binder = new RecaptchaExtendedServletRequestDataBinder(target, binder.getObjectName(), recaptchaFieldName);
			requestMappingHandlerAdapter.getWebBindingInitializer().initBinder(binder, request);
		}

		super.bindRequestParameters(binder, request);
	}

	private static String analyzeClass(Class<?> targetClass) {
		for (Field field : targetClass.getDeclaredFields()) {
			Recaptcha recaptchaAnnotation = field.getAnnotation(Recaptcha.class);

			if (recaptchaAnnotation != null) {
				return field.getName();
			}
		}

		for (Method method : targetClass.getDeclaredMethods()) {
			Recaptcha recaptchaAnnotation = method.getAnnotation(Recaptcha.class);

			if (recaptchaAnnotation != null) {
				try {
					return targetClass.getField(StringUtils.uncapitalize(method.getName().substring(3))).getName();
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

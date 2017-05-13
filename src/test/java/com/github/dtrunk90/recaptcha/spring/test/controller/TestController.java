package com.github.dtrunk90.recaptcha.spring.test.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.dtrunk90.recaptcha.spring.test.model.TestForm;

@Controller
public class TestController {

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String getTest() {
		return "";
	}

	@RequestMapping(value = "/test", method = RequestMethod.POST)
	public String postTest(@ModelAttribute @Valid TestForm testForm, BindingResult bindingResult) {
		return "";
	}

}

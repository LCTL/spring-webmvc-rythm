package com.ctlok.springframework.web.servlet.view.rythm.test;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.springframework.beans.BeansException;
import org.springframework.context.NoSuchMessageException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.View;

import com.ctlok.springframework.web.servlet.view.rythm.RythmViewResolver;

/**
 * @author Lawrence Cheung
 *
 */
public class WebBaseTest {
	
	public static final String CONTEXT_PATH = "/spring-rythm/";
	
	protected XmlWebApplicationContext webApplicationContext;
	protected MockHttpServletRequest request;
	
	@Before
	public void before() {
		MockServletContext servletContext = new MockServletContext();
		webApplicationContext = new XmlWebApplicationContext();
		webApplicationContext.setConfigLocations(new String[]{
		        "classpath:test-context.xml", "classpath:test-security-context.xml"});
		webApplicationContext.setServletContext(servletContext);
		webApplicationContext.refresh();
		
		servletContext.setAttribute(
		        WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, 
		        webApplicationContext);
		servletContext.setContextPath(CONTEXT_PATH);
		
		request = new MockHttpServletRequest();
		request.setContextPath(CONTEXT_PATH);
		
		RequestAttributes attributes = new ServletRequestAttributes(request);
		RequestContextHolder.setRequestAttributes(attributes);
	}
	
	public String renderTemplate(String template, Map<String, Object> model) throws Exception{
	    RythmViewResolver rythmViewResolver = this.getBean(RythmViewResolver.class);
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        View view = rythmViewResolver.resolveViewName(template, null);
        view.render(model, request, response);
        
        return this.processString(response.getContentAsString());
	}
	
	public void changeLocale(Locale locale){
		final LocaleResolver localeResolver = this.getBean(LocaleResolver.class);
		localeResolver.setLocale(request, new MockHttpServletResponse(), locale);
	}
	
	public String processString(String str) throws UnsupportedEncodingException{
		return new String(str.getBytes(), "UTF-8").trim();
	}

	public <T> T getBean(String name, Class<T> requiredType)
			throws BeansException {
		return webApplicationContext.getBean(name, requiredType);
	}

	public <T> T getBean(Class<T> requiredType) throws BeansException {
		return webApplicationContext.getBean(requiredType);
	}

	public Object getBean(String name, Object... args) throws BeansException {
		return webApplicationContext.getBean(name, args);
	}

	public String getMessage(String code, Object[] args, String defaultMessage,
			Locale locale) {
		return webApplicationContext.getMessage(code, args, defaultMessage,
				locale);
	}

	public String getMessage(String code, Object[] args, Locale locale)
			throws NoSuchMessageException {
		return webApplicationContext.getMessage(code, args, locale);
	}
	
}

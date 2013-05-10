package com.ctlok.springframework.web.servlet.view.rythm.tag;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.LocaleUtils;
import org.rythmengine.template.JavaTagBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.NoSuchMessageException;
import org.springframework.web.servlet.LocaleResolver;

import com.ctlok.springframework.web.servlet.view.rythm.Helper;

/**
 * @author Lawrence Cheung
 *
 */
public class Message extends JavaTagBase {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Message.class);
	
	private final ApplicationContext applicationContext;
	private LocaleResolver localeResolver;
	private Locale defaultLocale;
	
	@Autowired
	public Message(
			final ApplicationContext applicationContext){
		this.applicationContext = applicationContext;
		try{
			this.localeResolver = applicationContext.getBean(LocaleResolver.class);
		} catch (final NoSuchBeanDefinitionException e){
			LOGGER.warn("LocaleResolver not found. Default locale set to [{}]", Locale.ENGLISH);
		}
	}
	
	@Override
	public String __getName() {
		return "message";
	}
	
	@Override
	protected void call(__ParameterList params, __Body body) {
		final String code = (String) (params.getByName("code") == null ? params.getDefault() : params.getByName("code"));
		final String defaultMessage = (String) params.getByName("default");
		final String localeString = (String) params.getByName("locale");

		final Locale locale = getCurrentLocale(localeString);
		final Object[] args = getMessageArgument(params);
		
		String message = null;
		
		try{
			message = defaultMessage == null ?
					applicationContext.getMessage(code, args, locale) :
						applicationContext.getMessage(code, args, defaultMessage, locale);
		} catch (final NoSuchMessageException e){
			LOGGER.warn("Message code [{}] not found. Default output message code.", code);
			message = code;
		}
		
		this.p(message);
	}
	
	protected Locale getCurrentLocale(final String localeString){
		try{
			if (localeString == null){
				return this.getDefaultLocale();
			}else{
				return LocaleUtils.toLocale(localeString);
			}
		} catch (final IllegalArgumentException e){
			final Locale locale = this.getDefaultLocale();
			LOGGER.warn("Invalid locale string: [{}]. Set locale to [{}]", localeString, locale);
			return locale;
		}
	}
	
	protected Locale getDefaultLocale(){
	    
	    Locale result = null;
	    
	    if (this.localeResolver == null || Helper.getCurrentRequest() == null){
	        result = defaultLocale;
	    } else {
	        result = localeResolver.resolveLocale(Helper.getCurrentRequest());
	    }
	    
		return result;
		
	}
	
	protected Object[] getMessageArgument(final __ParameterList params){
		final List<Object> args = new ArrayList<Object>();
		int i = 0;
		for (final Iterator<__Parameter> iterator = params.iterator(); iterator.hasNext(); i++){
			final __Parameter parameter = iterator.next();
			if (i > 0 && parameter.name == null){
				args.add(parameter.value);
			}
		}
		return args.toArray();
	}

}

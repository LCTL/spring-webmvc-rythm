package com.ctlok.springframework.web.servlet.view.rythm.test;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Lawrence Cheung
 *
 */
public class MessageTagTest extends WebBaseTest {
	
	@Test
	public void normalMessageEnTest() throws Exception{
		this.normalMessageTest(Locale.ENGLISH);
	}
	
	@Test
	public void normalMessageZhTest() throws Exception{
		this.normalMessageTest(Locale.CHINESE);
	}
	
	@Test
	public void messageWithArgumentEnTest() throws Exception{
		this.messageWithArgumentTest(Locale.ENGLISH);
	}
	
	@Test
	public void messageWithArgumentZhTest() throws Exception{
		this.messageWithArgumentTest(Locale.CHINESE);
	}
	
	@Test
	public void messageWithDefaultTest() throws Exception{
		this.compareMessage("message_with_default_message", "null", Locale.ENGLISH, "Default Message", true);
	}
	
	@Test
	public void messageWithLocaleTest() throws Exception{
		this.compareMessage("message_with_locale", "label.welcome", Locale.CHINESE, false);
	}
	
	protected void normalMessageTest(Locale locale) throws Exception {
		this.compareMessage("message", "label.welcome", locale, true);
	}
	
	protected void messageWithArgumentTest(Locale locale) throws Exception {
		this.compareMessage("message_with_args", "label.loginInfo", locale, null, true, "Lawrence");
	}
	
	protected void compareMessage(String templateName, String messageCode, Locale locale, 
			boolean changeLocale, Object ... args) throws Exception{
		this.compareMessage(templateName, messageCode, locale, null, changeLocale, args);
	}
	
	protected void compareMessage(String templateName, String messageCode, 
			Locale locale, String defaultMessage, boolean changeLocale, Object ... args) throws Exception{

		if (changeLocale){
			this.changeLocale(locale);
		}

		String expected = defaultMessage == null ? 
				this.getMessage(messageCode, args, locale) :
					this.getMessage(messageCode, args, defaultMessage, locale);
		expected = this.processString(expected);
		
		Assert.assertEquals(expected.trim(), this.renderTemplate(templateName, null));
		
	}

}

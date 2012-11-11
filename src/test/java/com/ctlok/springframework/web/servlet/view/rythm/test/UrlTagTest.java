package com.ctlok.springframework.web.servlet.view.rythm.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.View;

import com.ctlok.springframework.web.servlet.view.rythm.RythmViewResolver;

/**
 * @author Lawrence Cheung
 *
 */
public class UrlTagTest extends WebBaseTest {

	@Test
	public void contextPathTest() throws Exception{
		RythmViewResolver rythmViewResolver = this.getBean(RythmViewResolver.class);
		MockHttpServletResponse response = new MockHttpServletResponse();
		Map<String, Object> model = new HashMap<String, Object>();
		
		View view = rythmViewResolver.resolveViewName("url", null);
		
		view.render(model, this.request, response);
		
		Assert.assertEquals(CONTEXT_PATH + "/javascripts/jQuery.min.js", this.processString(response.getContentAsString()));
	}
	
}

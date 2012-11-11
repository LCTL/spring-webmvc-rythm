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
public class RenderTemplateTest extends WebBaseTest {
	
	@Test
	public void standaloneTest() throws Exception{
		templateRenderTest("standalone");
	}
	
	@Test
	public void extendsTest() throws Exception{
		templateRenderTest("extends");
	}
	
	protected void templateRenderTest(String template) throws Exception{
		RythmViewResolver rythmViewResolver = this.getBean(RythmViewResolver.class);
		MockHttpServletResponse response = new MockHttpServletResponse();
		Map<String, Object> model = new HashMap<String, Object>();
		
		View view = rythmViewResolver.resolveViewName(template, null);
		
		model.put("result", "success");
		view.render(model, this.request, response);
		
		Assert.assertEquals("success", this.processString(response.getContentAsString()));
	}
	
}

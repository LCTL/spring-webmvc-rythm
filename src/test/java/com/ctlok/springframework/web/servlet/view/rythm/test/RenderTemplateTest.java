package com.ctlok.springframework.web.servlet.view.rythm.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

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
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("result", "success");
		
		Assert.assertEquals("success", this.renderTemplate(template, model));
	}
	
}

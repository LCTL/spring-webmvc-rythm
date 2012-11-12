package com.ctlok.springframework.web.servlet.view.rythm.test;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Lawrence Cheung
 *
 */
public class UrlTagTest extends WebBaseTest {

	@Test
	public void contextPathTest() throws Exception{
		Assert.assertEquals(CONTEXT_PATH + "/javascripts/jQuery.min.js", this.renderTemplate("url", null));
	}
	
}

package com.ctlok.springframework.web.servlet.view.rythm.variable;

import com.ctlok.springframework.web.servlet.view.rythm.Helper;

/**
 * @author Lawrence Cheung
 *
 */
public class HttpServletRequestVariable implements ImplicitVariable {
	
	@Override
	public String getName() {
		return "request";
	}

	@Override
	public String getType() {
		return "javax.servlet.http.HttpServletRequest";
	}

	@Override
	public Object getValue() {
		return Helper.getCurrentRequest();
	}

}

package com.ctlok.springframework.web.servlet.view.rythm;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lawrence Cheung
 *
 */
public class Helper {

	public static HttpServletRequest getCurrentRequest(){
	    try{
    		final ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    		return attr.getRequest();
	    } catch (IllegalStateException e){
	        return null;
	    }
	}
	
}

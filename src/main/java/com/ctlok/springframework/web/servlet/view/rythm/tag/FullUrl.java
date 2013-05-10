package com.ctlok.springframework.web.servlet.view.rythm.tag;

import javax.servlet.http.HttpServletRequest;

import com.ctlok.springframework.web.servlet.view.rythm.Helper;
import org.rythmengine.template.ITag;
import org.rythmengine.template.JavaTagBase;

public class FullUrl extends JavaTagBase {

	@Override
	public String __getName() {
		return "fullUrl";
	}

	@Override
	protected void call(ITag.__ParameterList params, __Body body) {
		
		final Object obj = params.getDefault();
		final HttpServletRequest request = Helper.getCurrentRequest();
        final String contextPath = request.getContextPath();
        final StringBuilder builder = new StringBuilder();
    	
    	builder.append(request.getScheme());
    	builder.append("://");
    	builder.append(request.getServerName());
    	
    	if (request.getServerPort() != 80){
        	builder.append(":");
        	builder.append(request.getServerPort());
    	}
    	
    	builder.append(contextPath);
		
        if (obj != null && obj instanceof String){

        	builder.append(obj.toString());

        } 
        
        this.p(builder.toString());
		
	}

}

package com.ctlok.springframework.web.servlet.view.rythm.tag;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.rythmengine.template.JavaTagBase;

import com.ctlok.springframework.web.servlet.view.rythm.Helper;

/**
 * 
 * @author Lawrence Cheung
 *
 */
public class CookieValue extends JavaTagBase {

    @Override
    public String __getName() {
        return "cookieValue";
    }
    
    @Override
    protected void call(__ParameterList params, __Body body) {

        final HttpServletRequest request = Helper.getCurrentRequest();
        final String name = (String) params.getDefault();
        String value = "";
        
        if (request != null && request.getCookies() != null){
            
            for (final Cookie cookie: request.getCookies()){
                
                if (cookie.getName().equals(name)){
                    
                    value = cookie.getValue();
                    
                }
                
            }
            
        }
        
        this.p(value);
        
    }

}

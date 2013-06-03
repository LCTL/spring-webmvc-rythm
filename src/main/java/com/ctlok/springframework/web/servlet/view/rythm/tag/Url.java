package com.ctlok.springframework.web.servlet.view.rythm.tag;

import javax.servlet.http.HttpServletRequest;

import com.ctlok.springframework.web.servlet.view.rythm.Helper;
import org.rythmengine.template.JavaTagBase;

/**
 * @author Lawrence Cheung
 *
 */
public class Url extends JavaTagBase {

    @Override
    public String __getName() {
        return "url";
    }

    @Override
    protected void call(__ParameterList params, __Body body) {
        final Object obj = params.getDefault();
        final HttpServletRequest request = Helper.getCurrentRequest();
        final String contextPath = request.getContextPath();
        
        if (obj != null && obj instanceof String){
            final String url = (String) obj;
            if (url.startsWith("http") || url.startsWith("//")){
                this.p(url);
            }else{
                this.p(contextPath + url);
            }
        }else{
            this.p(contextPath);
        }
    }

}

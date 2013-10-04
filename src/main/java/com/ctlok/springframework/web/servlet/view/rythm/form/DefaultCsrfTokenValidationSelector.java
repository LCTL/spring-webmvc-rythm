package com.ctlok.springframework.web.servlet.view.rythm.form;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author Lawrence Cheung
 *
 */
public class DefaultCsrfTokenValidationSelector implements
        CsrfTokenValidationSelector {

    @Override
    public boolean isRequireToValidate(HttpServletRequest request) {
        
        final boolean postMethod = "POST".equalsIgnoreCase(request.getMethod());
        final boolean notForwardedRequest = 
                request.getAttribute("javax.servlet.forward.request_uri") == null;
        
        return postMethod && notForwardedRequest;
        
    }

}

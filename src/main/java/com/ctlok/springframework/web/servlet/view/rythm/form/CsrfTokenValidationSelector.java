package com.ctlok.springframework.web.servlet.view.rythm.form;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author Lawrence Cheung
 *
 */
public interface CsrfTokenValidationSelector {

    public boolean isRequireToValidate(HttpServletRequest request);
    
}

package com.ctlok.springframework.web.servlet.view.rythm.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ctlok.springframework.web.servlet.view.rythm.constant.DefaultRequestParameterName;
import com.ctlok.springframework.web.servlet.view.rythm.constant.DefaultSessionAttributeName;
import com.ctlok.springframework.web.servlet.view.rythm.form.CsrfTokenValidationSelector;
import com.ctlok.springframework.web.servlet.view.rythm.form.DefaultCsrfTokenValidationSelector;

/**
 * 
 * @author Lawrence Cheung
 *
 */
public class CsrfTokenInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsrfTokenInterceptor.class);
    
    private final String csrfTokenSessionName;
    private final String csrfTokenRequestName;
    
    private CsrfTokenValidationSelector csrfTokenValidationSelector = new DefaultCsrfTokenValidationSelector();
    
    public CsrfTokenInterceptor() {
        super();
        this.csrfTokenSessionName = DefaultSessionAttributeName.CSRF_TOKEN_NAME;
        this.csrfTokenRequestName = DefaultRequestParameterName.CSRF_TOKEN_NAME;
    }

    public CsrfTokenInterceptor(String csrfTokenSessionName, String csrfTokenRequestName) {
        super();
        this.csrfTokenSessionName = csrfTokenSessionName;
        this.csrfTokenRequestName = csrfTokenRequestName;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        
        boolean validToken = true;
        
        if (csrfTokenValidationSelector.isRequireToValidate(request)){
            
            final HttpSession session = request.getSession(true);
            final String sessionToken = (String) session.getAttribute(csrfTokenSessionName);
            final String requestToken = request.getParameter(csrfTokenRequestName);
            
            LOGGER.debug("Session CSRF Token: [{}], Request CSRF Token: [{}]", sessionToken, requestToken);
            
            if (StringUtils.isNotEmpty(sessionToken) && StringUtils.isNotEmpty(requestToken)){
                
                validToken = sessionToken.equals(requestToken);
                
            } else {
                
                validToken = false;
                
            }
            
            LOGGER.debug("Is valid CSRF Token: [{}]", validToken);
            
        }
        
        if (!validToken){
            
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bad or missing CSRF token");
            
        }
        
        return validToken;
        
    }

    public CsrfTokenValidationSelector getCsrfTokenValidationSelector() {
        return csrfTokenValidationSelector;
    }

    public void setCsrfTokenValidationSelector(
            CsrfTokenValidationSelector csrfTokenValidationSelector) {
        this.csrfTokenValidationSelector = csrfTokenValidationSelector;
    }
    
}

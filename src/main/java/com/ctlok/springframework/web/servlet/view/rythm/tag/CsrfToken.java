package com.ctlok.springframework.web.servlet.view.rythm.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.rythmengine.template.JavaTagBase;

import com.ctlok.springframework.web.servlet.view.rythm.Helper;
import com.ctlok.springframework.web.servlet.view.rythm.constant.DefaultSessionAttributeName;
import com.ctlok.springframework.web.servlet.view.rythm.form.CsrfTokenGenerator;

/**
 * 
 * @author Lawrence Cheung
 *
 */
public class CsrfToken extends JavaTagBase {

    private final CsrfTokenGenerator csrfTokenGenerator;
    private final String csrfTokenSessionName;
    
    public CsrfToken(CsrfTokenGenerator csrfTokenGenerator) {
        super();
        this.csrfTokenGenerator = csrfTokenGenerator;
        this.csrfTokenSessionName = DefaultSessionAttributeName.CSRF_TOKEN_NAME;
    }

    public CsrfToken(
            CsrfTokenGenerator csrfTokenGenerator,
            String csrfTokenSessionName) {
        super();
        this.csrfTokenGenerator = csrfTokenGenerator;
        this.csrfTokenSessionName = csrfTokenSessionName;
    }

    @Override
    public String __getName() {
        
        return "csrfToken";
        
    }
    
    @Override
    protected void call(__ParameterList params, __Body body) {

        final HttpServletRequest request = Helper.getCurrentRequest();
        final HttpSession session = request.getSession(true);
        String csrfToken = (String) session.getAttribute(csrfTokenSessionName);
        
        if (csrfToken == null){
            
            csrfToken = csrfTokenGenerator.generate();
            session.setAttribute(csrfTokenSessionName, csrfToken);
            
        }
        
        this.p(csrfToken);
        
    }

}

package com.ctlok.springframework.web.servlet.view.rythm.form;

import java.util.UUID;

/**
 * 
 * @author Lawrence Cheung
 *
 */
public class UUIDCsrfTokenGenerator implements CsrfTokenGenerator {

    @Override
    public String generate() {
        
        return UUID.randomUUID().toString().replaceAll("-", "");
        
    }

}

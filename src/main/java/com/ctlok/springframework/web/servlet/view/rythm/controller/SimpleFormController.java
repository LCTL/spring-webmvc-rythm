package com.ctlok.springframework.web.servlet.view.rythm.controller;

import com.ctlok.springframework.web.servlet.view.rythm.form.FormFactory;
import com.ctlok.springframework.web.servlet.view.rythm.form.SimpleFormFactory;

/**
 * 
 * @author Lawrence Cheung
 *
 */
public class SimpleFormController extends AbstractFormController {

    @Override
    protected <T> FormFactory<T> createFormFactory(Class<T> formObjectClass) {
        
        return new SimpleFormFactory<T>(getApplicationContext(), formObjectClass);
        
    }

}

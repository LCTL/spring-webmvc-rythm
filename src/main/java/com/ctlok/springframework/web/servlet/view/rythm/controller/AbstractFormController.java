package com.ctlok.springframework.web.servlet.view.rythm.controller;

import java.util.Arrays;

import org.springframework.validation.Validator;
import org.springframework.web.context.support.WebApplicationObjectSupport;

import com.ctlok.springframework.web.servlet.view.rythm.form.FormFactory;

/**
 * 
 * @author Lawrence Cheung
 *
 */
public abstract class AbstractFormController extends WebApplicationObjectSupport {

    protected abstract <T> FormFactory<T> createFormFactory(final Class<T> formObjectClass);
    
    protected <T> FormFactory<T> createFormFactory(final Class<T> formObjectClass, Validator ... validator){
        
        final FormFactory<T> formFactory = createFormFactory(formObjectClass);
        
        formFactory.setValidators(Arrays.asList(validator));
        
        return formFactory;
        
    }
    
}

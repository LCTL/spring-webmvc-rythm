package com.ctlok.springframework.web.servlet.view.rythm.form;

import org.springframework.context.ApplicationContext;

/**
 * 
 * @author Lawrence Cheung
 *
 */
public class SimpleFormFactory<T> extends AbstractFormFactory<T> {

    public SimpleFormFactory(ApplicationContext applicationContext,
            Class<T> formObjectClass) {
        super(applicationContext, formObjectClass);
    }

    protected Form<T> createSimpleForm(){

        return new SimpleForm<T>();
        
    }
    
}

package com.ctlok.springframework.web.servlet.view.rythm.form;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.LocaleResolver;

import com.ctlok.springframework.web.servlet.view.rythm.Helper;
import com.ctlok.springframework.web.servlet.view.rythm.constant.DefaultApplicationVariable;

/**
 * 
 * @author Lawrence Cheung
 *
 */
public abstract class AbstractFormFactory<T> implements FormFactory<T> {

    private final ApplicationContext applicationContext;
    private final Class<T> formObjectClass;
    private List<Validator> validators;
    
    public AbstractFormFactory(
            final ApplicationContext applicationContext,
            final Class<T> formObjectClass){
        
        this.applicationContext = applicationContext;
        this.formObjectClass = formObjectClass;
        
    }
    
    protected abstract Form<T> createSimpleForm();

    @Override
    public Form<T> createBlankForm(){
        
        final Form<T> form = createSimpleForm();
        
        assignMessageSourceAndLocale(form);
        
        form.setWrappedInstanceClass(formObjectClass);
        
        form.init();
        
        return form;
        
    }
    
    @Override
    public Form<T> createForm(T formObject){
        
        return createForm(formObject, null);
        
    }
    
    @Override
    public Form<T> createForm(T formObject, BindingResult bindingResult){
        
        final Form<T> form = createSimpleForm();
        
        assignMessageSourceAndLocale(form);
        
        form.setWrappedInstance(formObject);
        
        form.setBindingResult(bindingResult);
        
        form.setValidators(validators);
        
        form.init();
        
        return form;
        
    }

    @Override
    public List<Validator> getValidators() {
        
        return validators;
        
    }

    @Override
    public void setValidators(List<Validator> validators) {
        
        this.validators = validators;
        
    }
    
    public ApplicationContext getApplicationContext() {
        
        return applicationContext;
        
    }

    public Class<T> getFormObjectClass() {
        
        return formObjectClass;
        
    }

    protected void assignMessageSourceAndLocale(Form<T> form){
        
        final LocaleResolver localeResolver = 
                getApplicationContext().getBean(LocaleResolver.class);
        
        form.setMessageSource(getApplicationContext());
        
        if (localeResolver == null){
            
            form.setLocale(DefaultApplicationVariable.LOCALE);
            
        } else {
            
            form.setLocale(localeResolver.resolveLocale(Helper.getCurrentRequest()));
            
        }
        
    }
    
}

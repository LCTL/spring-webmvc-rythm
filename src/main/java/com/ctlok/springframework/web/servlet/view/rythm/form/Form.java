package com.ctlok.springframework.web.servlet.view.rythm.form;

import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

/**
 * 
 * @author Lawrence Cheung
 *
 */
public interface Form<T> {
    
    public void init();
    
    public Object get(String propertyName);
    public String getValue(String propertyName);
    public boolean isChecked(final String propertyName);
    
    public boolean isActionSuccess();
    public void setActionSuccess(boolean actionSuccess);
    
    public boolean hasGlobalErrors();
    public String getGlobalErrorMessage();
    public List<String> getGlobalErrorMessages();
    
    public boolean hasErrors();
    public boolean hasError(String propertyName);
    public String getErrorMessage(String propertyName);
    
    public boolean hasMaxLength(String propertyName);
    public int getMaxLength(String propertyName);
    
    public void setWrappedInstanceClass(Class<T> wrappedInstanceClass);
    public void setWrappedInstance(T wrappedInstance);
    public T getWrappedInstance();
    
    public MessageSource getMessageSource();
    public void setMessageSource(MessageSource messageSource);
    
    public Locale getLocale();
    public void setLocale(Locale locale);
    
    public List<Validator> getValidators();
    public void setValidators(List<Validator> validators);
    
    public BindingResult getBindingResult();
    public void setBindingResult(BindingResult bindingResult);
    
}

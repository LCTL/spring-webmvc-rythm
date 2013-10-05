package com.ctlok.springframework.web.servlet.view.rythm.form;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

/**
 * 
 * @author Lawrence Cheung
 *
 */
public class SimpleForm<T> implements Form<T> {
    
    private Class<T> wrappedInstanceClass;
    private T wrappedInstance;
    
    private BeanWrapper beanWrapper;
    private List<Validator> validators;
    private BindingResult bindingResult;
    
    private MessageSource messageSource;
    private Locale locale;
    
    private boolean actionSuccess;

    @Override
    public void init() {
        
        if (wrappedInstance == null){
            
            beanWrapper = new BeanWrapperImpl(wrappedInstanceClass);
            
        } else {
            
            beanWrapper = new BeanWrapperImpl(wrappedInstance);
            
            if (bindingResult != null && validators != null){
            
                for (final Validator validator: validators){
                    
                    validator.validate(wrappedInstance, bindingResult);
                    
                }
            
            }
            
        }
        
    }
    
    public Object get(final String propertyName){
        
        return beanWrapper.getPropertyValue(propertyName);
        
    }
    
    public String getValue(final String propertyName){
        
        final Object obj = get(propertyName);
        
        String result = null;
        
        if (obj != null){
            
            result = obj.toString();
            
        }
        
        return result;
        
    }
    
    public boolean isChecked(final String propertyName){
        
        final Class<?> type = beanWrapper.getPropertyDescriptor(propertyName).getPropertyType();
        
        boolean checked = false;
        
        if (Boolean.class == type || Boolean.TYPE == type){
            
            checked = (Boolean) get(propertyName);
            
        } else {
            
            throw new IllegalStateException("Property: " + propertyName + " is not boolean type. Property type: " + type.getName());
            
        }
        
        return checked;
        
    }

    public boolean isActionSuccess() {
        
        return actionSuccess;
        
    }

    public void setActionSuccess(boolean actionSuccess) {
        
        this.actionSuccess = actionSuccess;
        
    }

    public boolean hasGlobalErrors(){
        
        boolean result = false;
        if (bindingResult != null){
            result = bindingResult.hasGlobalErrors();
        }
        return result;
        
    }
    
    public String getGlobalErrorMessage(){
        
        String message = null;
        
        if (bindingResult != null && bindingResult.getGlobalError() != null){
            final ObjectError error = bindingResult.getGlobalError();
            message = error.getDefaultMessage();
        }
        
        return message;
        
    }
    
    public List<String> getGlobalErrorMessages(){
        
        final List<String> messages = new ArrayList<String>();
        
        if (bindingResult != null && bindingResult.getGlobalError() != null){
            for (final ObjectError error: bindingResult.getGlobalErrors()){
                messages.add(error.getDefaultMessage());
            }
        }
        
        return messages;
        
    }
    
    public boolean hasErrors(){
        
        boolean result = false;
        if (bindingResult != null){
            result = bindingResult.hasErrors();
        }
        return result;
        
    }
    
    public boolean hasError(final String propertyName){
        
        return this.getFieldError(propertyName) != null;
        
    }
    
    public String getErrorMessage(final String propertyName){
        
        final FieldError error = this.getFieldError(propertyName);
        String message = null;
        
        if (error != null){
            message = this.getFieldErrorMessage(error);
        }
        
        return message;
        
    }
    
    public boolean hasMaxLength(final String propertyName){
        
        return getPropertyLengthAnnotation(propertyName) != null;
        
    }
    
    public int getMaxLength(final String propertyName){
        
        final Length maxLength = getPropertyLengthAnnotation(propertyName);
                
        return maxLength == null ? -1 : maxLength.max();
        
    }
    
    public void setWrappedInstanceClass(Class<T> wrappedInstanceClass){
        
        this.wrappedInstanceClass = wrappedInstanceClass;
        
    }
    
    public void setWrappedInstance(T wrappedInstance){
        
        this.wrappedInstance = wrappedInstance;
        
    }
    
    @SuppressWarnings("unchecked")
    public T getWrappedInstance(){
        
        return (T) beanWrapper.getWrappedInstance();
        
    }
    
    public List<Validator> getValidators() {
        
        return validators;
        
    }

    public void setValidators(List<Validator> validators) {
        
        this.validators = validators;
        
    }

    public BindingResult getBindingResult() {
        
        return bindingResult;
        
    }

    public void setBindingResult(BindingResult bindingResult) {
        
        this.bindingResult = bindingResult;
        
    }
    
    public MessageSource getMessageSource() {
        
        return messageSource;
        
    }

    public void setMessageSource(MessageSource messageSource) {
        
        this.messageSource = messageSource;
        
    }

    public Locale getLocale() {
        
        return locale;
        
    }

    public void setLocale(Locale locale) {
        
        this.locale = locale;
        
    }
    
    protected FieldError getFieldError(final String propertyName){
        
        FieldError error = null;
        
        if (bindingResult != null){
            error = bindingResult.getFieldError(propertyName);
        }
        
        return error;
        
    }

    protected String getFieldErrorMessage(final FieldError fieldError){
        
        String message = null;
        
        if (fieldError.isBindingFailure()){
            message = messageSource.getMessage("form.error.binding", null, 
                    "Invalid Value", locale);
        }else{
            message = fieldError.getDefaultMessage();
        }
        
        return message;
        
    }
    
    protected String buildErrorMessage(final String code, Object ... params){
        
        return messageSource.getMessage(code, params, code, locale);
        
    }
    
    protected Length getPropertyLengthAnnotation(final String propertyName){
        
        Length length = getGetterLengthAnnotation(propertyName);
        
        if (length == null){
            
            length = getFieldLengthAnnotation(propertyName);
            
        }
        
        return length;
        
    }
    
    protected Length getGetterLengthAnnotation(final String propertyName){
        
        final PropertyDescriptor descriptor = beanWrapper.getPropertyDescriptor(propertyName);
        final Method getter = descriptor.getReadMethod();

        return getter.getAnnotation(Length.class);
        
    }
    
    protected Length getFieldLengthAnnotation(final String propertyName) {
        
        try {
            
            final Field field = beanWrapper.getWrappedClass().getDeclaredField(propertyName);
            
            field.setAccessible(true);
            
            return field.getAnnotation(Length.class);
            
        } catch (SecurityException e) {
            
            throw new IllegalStateException(e);
            
        } catch (NoSuchFieldException e) {
            
            throw new IllegalStateException(e);
            
        }
        
    }

    protected BeanWrapper getBeanWrapper() {
        return beanWrapper;
    }

    protected void setBeanWrapper(BeanWrapper beanWrapper) {
        this.beanWrapper = beanWrapper;
    }
    
}

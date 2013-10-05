package com.ctlok.springframework.web.servlet.view.rythm.form;

import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

/**
 * 
 * @author Lawrence Cheung
 *
 */
public interface FormFactory<T> {

    public Form<T> createBlankForm();
    public Form<T> createForm(T formObject);
    public Form<T> createForm(T formObject, BindingResult bindingResult);
    
    public List<Validator> getValidators();
    public void setValidators(List<Validator> validators);
    
}

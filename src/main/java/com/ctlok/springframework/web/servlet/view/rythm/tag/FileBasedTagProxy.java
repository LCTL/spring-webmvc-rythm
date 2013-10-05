package com.ctlok.springframework.web.servlet.view.rythm.tag;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.rythmengine.Rythm;
import org.rythmengine.template.ITemplate;
import org.rythmengine.template.JavaTagBase;
import org.rythmengine.template.TagBase;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.Resource;

import com.ctlok.springframework.web.servlet.view.rythm.Helper;

/**
 * 
 * @author Lawrence Cheung
 *
 */
public class FileBasedTagProxy extends JavaTagBase {
    
    private final File file;
    private final String tagName;
    private final Method templateSetBodyMethod;
    
    public FileBasedTagProxy(final FileBasedTag fileBasedTag) throws IOException{
        
        this.file = Helper.copyResourceToTempDirectory(fileBasedTag.getResource());
        this.tagName = fileBasedTag.getTagName() == null ? 
                getDefaultTagName(fileBasedTag.getResource()) : fileBasedTag.getTagName();
                
        this.templateSetBodyMethod = findTagBaseSetBodyMethod();
    }
    
    private String getDefaultTagName(final Resource resource){
        
        final String templateFileName = resource.getFilename();
        final int indexOfExt = templateFileName.lastIndexOf(".");
        
        return indexOfExt == -1 ? templateFileName : templateFileName.substring(0, indexOfExt);
        
    }
    
    private Method findTagBaseSetBodyMethod(){
        
        final Method method = 
                BeanUtils.findDeclaredMethod(TagBase.class, "setBody", 
                        new Class<?>[]{org.rythmengine.template.ITag.__Body.class});
        
        method.setAccessible(true);
        
        return method;
        
    }
    
    @Override
    public String __getName() {
        
        return tagName;
        
    }

    @Override
    protected void call(__ParameterList params, __Body body) {

        ITemplate template = null;
        
        if (params.asMap().isEmpty()){
        
            final List<Object> templateArguments = new ArrayList<Object>();
            
            for (final Iterator<__Parameter> iter = params.iterator(); iter.hasNext();){
                
                final __Parameter param = iter.next();
                
                templateArguments.add(param.value);
                
            }
            
            template = Rythm.engine().getTemplate(file, templateArguments.toArray());
            
        } else {
        
            template = Rythm.engine().getTemplate(file, params.asMap());
        
        }

        try {
            
            templateSetBodyMethod.invoke(template, body);
            
        } catch (Exception e) {
            
            throw new IllegalStateException(e);
            
        } 
        
        this.p(template.render());
        
    }

}

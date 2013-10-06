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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.Resource;

import com.ctlok.springframework.web.servlet.view.rythm.Helper;

/**
 * 
 * @author Lawrence Cheung
 *
 */
public class FileBasedTagProxy extends JavaTagBase {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FileBasedTagProxy.class);
    
    private final boolean disableFileWrite;
    private final File file;
    private final String templateString;
    private final String tagName;
    private final Method templateSetBodyMethod;
    
    public FileBasedTagProxy(final FileBasedTag fileBasedTag, final boolean disableFileWrite) throws IOException{

        this.disableFileWrite = disableFileWrite;

        if (disableFileWrite){
        
            this.templateString = Helper.inputStreamToString(fileBasedTag.getResource().getInputStream());
            this.file = null;
            
            LOGGER.debug("Disable file write. Load template file as String: [{}]", this.templateString);
            
        } else {
            
            this.file = Helper.copyResourceToTempDirectory(fileBasedTag.getResource());
            this.templateString = null;
            
            LOGGER.debug("Enable file write. Copy template file to: [{}]", file.getAbsolutePath());
            
        }
        
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

        final ITemplate template = createTemplate();
        
        if (params.asMap().isEmpty()){
        
            final List<Object> templateArguments = new ArrayList<Object>();
            
            for (final Iterator<__Parameter> iter = params.iterator(); iter.hasNext();){
                
                final __Parameter param = iter.next();
                
                templateArguments.add(param.value);
                
            }
            
            template.__setRenderArgs(templateArguments.toArray());
            
        } else {
        
            template.__setRenderArgs(params.asMap());
            
        }
        
        if (body != null){
        
            try {
                
                templateSetBodyMethod.invoke(template, body);
                
            } catch (Exception e) {
                
                throw new IllegalStateException(e);
                
            } 
        
        }
        
        this.p(template.render());
        
    }
    
    protected ITemplate createTemplate(){
        
        ITemplate template = null;
        
        if (disableFileWrite) {
            
            template = Rythm.engine().getTemplate(templateString);
            
        } else {
            
            template = Rythm.engine().getTemplate(file);
            
        }
        
        return template;
        
    }

}

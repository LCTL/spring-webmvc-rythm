package com.ctlok.springframework.web.servlet.view.rythm.tag;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.rythmengine.Rythm;
import org.rythmengine.template.ITemplate;
import org.rythmengine.template.JavaTagBase;

/**
 * 
 * @author Lawrence Cheung
 *
 */
public class FileBasedTagProxy extends JavaTagBase {
    
    private final File file;
    private final String tagName;
    
    public FileBasedTagProxy(final FileBasedTag fileBasedTag) throws IOException{
        
        this.file = fileBasedTag.getResource().getFile();
        this.tagName = fileBasedTag.getTagName() == null ? getDefaultTagName(file) : fileBasedTag.getTagName();
        
    }
    
    private String getDefaultTagName(final File file){
        
        final String templateFileName = file.getName();
        final int indexOfExt = templateFileName.lastIndexOf(".");
        
        return indexOfExt == -1 ? templateFileName : templateFileName.substring(0, indexOfExt);
        
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
        
        this.p(template.render());
        
    }

}

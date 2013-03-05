package com.ctlok.springframework.web.servlet.view.rythm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;

import com.ctlok.springframework.web.servlet.view.rythm.tag.DateFormat;
import com.ctlok.springframework.web.servlet.view.rythm.tag.FullUrl;
import com.ctlok.springframework.web.servlet.view.rythm.tag.Message;
import com.ctlok.springframework.web.servlet.view.rythm.tag.Secured;
import com.ctlok.springframework.web.servlet.view.rythm.tag.Url;
import com.ctlok.springframework.web.servlet.view.rythm.variable.HttpServletRequestVariable;
import com.ctlok.springframework.web.servlet.view.rythm.variable.ImplicitVariable;
import com.greenlaw110.rythm.Rythm;
import com.greenlaw110.rythm.template.ITag;

/**
 * @author Lawrence Cheung
 *
 */
public class RythmViewResolver extends AbstractTemplateViewResolver {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RythmViewResolver.class);
    
    private final RythmConfigurator configurator;
    
    public RythmViewResolver(final RythmConfigurator configurator) {
        super();
        this.configurator = configurator;
        this.setViewClass(this.requiredViewClass());
        this.setContentType("text/html");
    }

	@Override
    protected Class<?> requiredViewClass() {
        return RythmView.class;
    }

    @Override
    protected void initServletContext(ServletContext servletContext) {
        super.initServletContext(servletContext);
        this.setupSpringRythmConfig();
        this.initRythm();
    }
    
    protected void initRythm(){
        Rythm.init(configurator.generateConfig());
        
        if (configurator.getTags() != null){
        	for (final ITag tag: configurator.getTags()){
        		LOGGER.debug("Register tag: [{}]", tag.__getName());
        		Rythm.registerTag(tag);
        	}
        }
        
        if (configurator.isPreCompiledRoot() != null 
        		&& configurator.getRootDirectory() != null
        		&& configurator.isPreCompiledRoot()){
        	
        	final File root = new File(this.getServletContext().getRealPath(configurator.getRootDirectory()));
        	for (final File templateFile: this.findTemplateFile(root)){
        		LOGGER.debug("Pre compile template: [{}]", templateFile.getAbsolutePath());
        		Rythm.engine().getTemplate(templateFile);
        	}
        	
        }
        
        LOGGER.info("Rythm version [{}] setup success.", Rythm.engine().version());
    }
    
    protected void setupSpringRythmConfig(){
    	this.configBuildInImplicitVariables();
        this.configBuildInTag();
    }
    
    protected void configBuildInImplicitVariables(){
    	if (configurator.getImplicitVariables() == null){
    		configurator.setImplicitVariables(new ArrayList<ImplicitVariable>());
    	}
        			
    	configurator.getImplicitVariables().add(new HttpServletRequestVariable());
    }
    
    protected void configBuildInTag(){
    	if (configurator.getTags() == null){
    		configurator.setTags(new ArrayList<ITag>());
    	}
    	
    	final AutowireCapableBeanFactory factory = this.getApplicationContext().getAutowireCapableBeanFactory();
    	for (final Class<? extends ITag> clazz: this.defaultTagClasses()){
    		final Object tag = factory.autowire(
    				clazz, AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR, false);
    		
    		configurator.getTags().add((ITag) tag);
    	}
    }
 
	protected List<Class<? extends ITag>> defaultTagClasses(){
		List<Class<? extends ITag>> classes = new ArrayList<Class<? extends ITag>>();
		classes.add(Url.class);
		classes.add(FullUrl.class);
		classes.add(Message.class);
		classes.add(Secured.class);
		classes.add(DateFormat.class);
		return classes;
    }
	
	protected List<File> findTemplateFile(final File root){
		final List<File> templateFiles = new ArrayList<File>();
		
		if (root.isDirectory()){
			for (final File file: root.listFiles()){
				
				if (file.isFile()){
					templateFiles.add(file);
				} else {
					templateFiles.addAll(findTemplateFile(file));
				}
				
			}
		}
		
		return templateFiles;
	}

}

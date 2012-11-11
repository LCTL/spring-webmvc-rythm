package com.ctlok.springframework.web.servlet.view.rythm;

import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;

import com.ctlok.springframework.web.servlet.view.rythm.tag.Message;
import com.ctlok.springframework.web.servlet.view.rythm.tag.Url;
import com.ctlok.springframework.web.servlet.view.rythm.variable.HttpServletRequestVariable;
import com.ctlok.springframework.web.servlet.view.rythm.variable.ImplicitVariable;
import com.greenlaw110.rythm.Rythm;
import com.greenlaw110.rythm.runtime.ITag;

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
        		LOGGER.debug("Register tag: [{}]", tag.getName());
        		Rythm.registerTag(tag);
        	}
        }
        
        LOGGER.info("Rythm version [{}] setup success.", Rythm.version);
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
    	
    	configurator.getTags().add(new Url());
    	configurator.getTags().add(new Message(this.getApplicationContext()));
    }

}

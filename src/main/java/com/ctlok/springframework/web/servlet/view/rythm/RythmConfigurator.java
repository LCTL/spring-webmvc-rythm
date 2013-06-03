package com.ctlok.springframework.web.servlet.view.rythm;

import com.ctlok.springframework.web.servlet.view.rythm.cache.SpringRythmCache;
import com.ctlok.springframework.web.servlet.view.rythm.log.RythmLoggerFactory;
import com.ctlok.springframework.web.servlet.view.rythm.variable.ImplicitVariable;
import org.rythmengine.Rythm;
import org.rythmengine.extension.*;
import org.rythmengine.template.ITemplate;
import org.springframework.cache.CacheManager;
import org.springframework.web.context.support.WebApplicationObjectSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lawrence Cheung
 */
public class RythmConfigurator extends WebApplicationObjectSupport {

    private Boolean cacheInProductionModeOnly;
    private Boolean compactOutput;
    private Boolean enableJavaExtensions;
    private Boolean loadPreCompiled;
    private Boolean logRenderTime;
    private Boolean preCompiledRoot;
    private Boolean noFileWrite;

    private String mode;
    private String rootDirectory;
    private String tempDirectory;
    private Integer cacheDefaultTTL;

    private List<String> implicitPackages;
    private List<ImplicitVariable> implicitVariables;
    private List<ITemplate> tags;

    private ICacheService cacheService;
    private ClassLoader classLoader;
    private IDurationParser durationParser;
    private ILoggerFactory loggerFactory;
    private ITemplateResourceLoader resourceLoader;
    private IByteCodeHelper byteCodeHelper;

    /*
      * Spring Cache config
      */
    private CacheManager cacheManager;
    private String springCacheName = "RYTHM_TEMPLATE_CACHE";

    public Map<String, Object> generateConfig() {
        final Map<String, Object> map = new HashMap<String, Object>();

        this.setConfig(map, "cache.prod_only.enabled", cacheInProductionModeOnly);
        this.setConfig(map, "codegen.compact.enabled", compactOutput);
        this.setConfig(map, "feature.transformer.enabled", enableJavaExtensions);
        this.setConfig(map, "engine.load_precompiled.enabled", loadPreCompiled);
        this.setConfig(map, "log.time.render.enabled", logRenderTime);
        this.setConfig(map, "engine.file_write.enabled", noFileWrite);

        this.modeConfig(map);
        this.preCompiledRootConfig(map);
        this.rootDirectoryConfig(map);
        this.setConfig(map, "home.tmp", tempDirectory);

        this.setConfig(map, "default.cache_ttl", cacheDefaultTTL);

        this.implicitConfig(map);

        this.cacheServiceConfig(map);
        this.setConfig(map, "engine.class_loader.parent.impl", classLoader);
        this.setConfig(map, "cache.duration_parser.impl", durationParser);
        this.loggerFactoryConfig(map);
        this.setConfig(map, "resource.loader.impl", resourceLoader);
        this.setConfig(map, "engine.class_loader.bytecode_helper.impl", byteCodeHelper);

        return map;
    }

    protected void setConfig(final Map<String, Object> map, final String key, final Object value) {
        if (value != null) {
            map.put(key, value);
        }
    }

    protected void implicitConfig(final Map<String, Object> map) {
        if (this.implicitVariables != null || this.implicitPackages != null) {
            map.put("codegen.source_code_enhancer.impl",
                    new ISourceCodeEnhancer() {

                        @Override
                        public List<String> imports() {
                            final List<String> packages = new ArrayList<String>();
                            if (implicitPackages != null) {
                                packages.addAll(implicitPackages);
                            }
                            return packages;
                        }

                        @Override
                        public Map<String, ?> getRenderArgDescriptions() {

                            final Map<String, Object> descriptions = new HashMap<String, Object>();

                            if (implicitVariables != null) {
                                for (final ImplicitVariable implicitVariable : implicitVariables) {
                                    descriptions.put(
                                            implicitVariable.getName(),
                                            implicitVariable.getType());
                                }
                            }

                            return descriptions;
                        }

                        @Override
                        public void setRenderArgs(ITemplate template) {

                            if (implicitVariables != null) {
                                for (final ImplicitVariable implicitVariable : implicitVariables) {
                                    template.__setRenderArg(
                                            implicitVariable.getName(),
                                            implicitVariable.getValue());
                                }
                            }

                        }

                        @Override
                        public String sourceCode() {
                            return null;
                        }

                    });
        }
    }

    protected void loggerFactoryConfig(final Map<String, Object> map) {
        if (this.loggerFactory == null) {
            this.loggerFactory = new RythmLoggerFactory();
        }

        map.put("log.factory.impl", this.loggerFactory);
    }

    protected void modeConfig(final Map<String, Object> map) {
        if (this.mode != null) {
            if (Rythm.Mode.prod.name().equalsIgnoreCase(this.mode)) {
                map.put("engine.mode", Rythm.Mode.prod);
            } else if (Rythm.Mode.dev.name().equalsIgnoreCase(this.mode)) {
                map.put("engine.mode", Rythm.Mode.dev);
            }
        }
    }

    protected void rootDirectoryConfig(final Map<String, Object> map) {
        if (this.rootDirectory != null) {
            map.put("home.template",
                    this.getServletContext().getRealPath(this.rootDirectory));
        }
    }

    protected void cacheServiceConfig(final Map<String, Object> map) {
        if (cacheManager == null) {
            this.setConfig(map, "cache.service.impl", this.cacheService);
        } else {
            final SpringRythmCache springRythmCache =
                    new SpringRythmCache(cacheManager.getCache(this.springCacheName));

            this.setConfig(map, "cache.service.impl", springRythmCache);
        }
    }

    protected void preCompiledRootConfig(final Map<String, Object> map) {
        if (preCompiledRoot != null && rootDirectory != null) {
            final File root = new File(this.getServletContext().getRealPath(this.rootDirectory));
            map.put("home.precompiled", root);
        }
    }

    public Boolean isCacheInProductionModeOnly() {
        return cacheInProductionModeOnly;
    }

    public void setCacheInProductionModeOnly(Boolean cacheInProductionModeOnly) {
        this.cacheInProductionModeOnly = cacheInProductionModeOnly;
    }

    public Boolean isCompactOutput() {
        return compactOutput;
    }

    public void setCompactOutput(Boolean compactOutput) {
        this.compactOutput = compactOutput;
    }

    public Boolean isEnableJavaExtensions() {
        return enableJavaExtensions;
    }

    public void setEnableJavaExtensions(Boolean enableJavaExtensions) {
        this.enableJavaExtensions = enableJavaExtensions;
    }

    public Boolean isLoadPreCompiled() {
        return loadPreCompiled;
    }

    public void setLoadPreCompiled(Boolean loadPreCompiled) {
        this.loadPreCompiled = loadPreCompiled;
    }

    public Boolean isNoFileWrite() {
        return noFileWrite;
    }

    public void setNoFileWrite(Boolean noFileWrite) {
        this.noFileWrite = noFileWrite;
    }

    public Boolean isLogRenderTime() {
        return logRenderTime;
    }

    public void setLogRenderTime(Boolean logRenderTime) {
        this.logRenderTime = logRenderTime;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Boolean isPreCompiledRoot() {
        return preCompiledRoot;
    }

    public void setPreCompiledRoot(Boolean preCompiledRoot) {
        this.preCompiledRoot = preCompiledRoot;
    }

    public String getRootDirectory() {
        return rootDirectory;
    }

    public void setRootDirectory(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public String getTempDirectory() {
        return tempDirectory;
    }

    public void setTempDirectory(String tempDirectory) {
        this.tempDirectory = tempDirectory;
    }

    public Integer getCacheDefaultTTL() {
        return cacheDefaultTTL;
    }

    public void setCacheDefaultTTL(Integer cacheDefaultTTL) {
        this.cacheDefaultTTL = cacheDefaultTTL;
    }

    public List<String> getImplicitPackages() {
        return implicitPackages;
    }

    public void setImplicitPackages(List<String> implicitPackages) {
        this.implicitPackages = implicitPackages;
    }

    public List<ImplicitVariable> getImplicitVariables() {
        return implicitVariables;
    }

    public void setImplicitVariables(List<ImplicitVariable> implicitVariables) {
        this.implicitVariables = implicitVariables;
    }

    public List<ITemplate> getTags() {
        return tags;
    }

    public void setTags(List<ITemplate> tags) {
        this.tags = tags;
    }

    public ICacheService getCacheService() {
        return cacheService;
    }

    public void setCacheService(ICacheService cacheService) {
        this.cacheService = cacheService;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public IDurationParser getDurationParser() {
        return durationParser;
    }

    public void setDurationParser(IDurationParser durationParser) {
        this.durationParser = durationParser;
    }

    public ILoggerFactory getLoggerFactory() {
        return loggerFactory;
    }

    public void setLoggerFactory(ILoggerFactory loggerFactory) {
        this.loggerFactory = loggerFactory;
    }

    public ITemplateResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    public void setResourceLoader(ITemplateResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public IByteCodeHelper getByteCodeHelper() {
        return byteCodeHelper;
    }

    public void setByteCodeHelper(IByteCodeHelper byteCodeHelper) {
        this.byteCodeHelper = byteCodeHelper;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public String getSpringCacheName() {
        return springCacheName;
    }

    public void setSpringCacheName(String springCacheName) {
        this.springCacheName = springCacheName;
    }

}

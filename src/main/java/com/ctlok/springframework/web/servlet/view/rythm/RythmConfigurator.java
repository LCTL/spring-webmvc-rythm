package com.ctlok.springframework.web.servlet.view.rythm;

import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.web.context.support.WebApplicationObjectSupport;

import com.ctlok.springframework.web.servlet.view.rythm.log.RythmLoggerFactory;
import com.ctlok.springframework.web.servlet.view.rythm.variable.ImplicitVariable;
import com.greenlaw110.rythm.IByteCodeHelper;
import com.greenlaw110.rythm.IHotswapAgent;
import com.greenlaw110.rythm.Rythm;
import com.greenlaw110.rythm.cache.ICacheService;
import com.greenlaw110.rythm.logger.ILoggerFactory;
import com.greenlaw110.rythm.resource.ITemplateResourceLoader;
import com.greenlaw110.rythm.runtime.ITag;
import com.greenlaw110.rythm.template.ITemplate;
import com.greenlaw110.rythm.utils.IDurationParser;
import com.greenlaw110.rythm.utils.IImplicitRenderArgProvider;

/**
 * @author Lawrence Cheung
 *
 */
public class RythmConfigurator extends WebApplicationObjectSupport {

	private Boolean autoScanTag;
	private Boolean cacheInProductionModeOnly;
	private Boolean compactOutput;
	private Boolean enableJavaExtensions;
	private Boolean loadPreCompiled;
	private Boolean logJavaSource;
	private Boolean logRenderTime;
	private Boolean noFileWrite;
	private Boolean refreshOnRender;
	
	private String mode;
	private String preCompiledRoot;
	private String reloadMethod;
	private String rootDirectory;
	private String tagRootDirectory;
	private String tempDirectory;
	
	private Integer cacheDefaultTTL;
	
	private List<String> implicitPackages;
	private List<ImplicitVariable> implicitVariables;
	private List<ITag> tags;
	
	private ICacheService cacheService;
	private ClassLoader classLoader;
	private IDurationParser durationParser;
	private FileFilter fileNameFilter;
	private IHotswapAgent hotswapAgent;
	private ILoggerFactory loggerFactory;
	private ITemplateResourceLoader resourceLoader;
	private IByteCodeHelper byteCodeHelper;

	public Properties generateConfig() {
		final Properties props = new Properties();
		
		this.setProperties(props, "rythm.tag.autoscan", autoScanTag);
		this.setProperties(props, "rythm.cache.prodOnly", cacheInProductionModeOnly);
		this.setProperties(props, "rythm.compactOutput", compactOutput);
		this.setProperties(props, "rythm.enableJavaExtensions", enableJavaExtensions);
		this.setProperties(props, "rythm.loadPreCompiled", loadPreCompiled);
		this.setProperties(props, "rythm.logJavaSource", logJavaSource);
		this.setProperties(props, "rythm.logRenderTime", logRenderTime);
		this.setProperties(props, "rythm.noFileWrite", noFileWrite);
		this.setProperties(props, "rythm.resource.refreshOnRender", refreshOnRender);

		this.modeConfig(props);
		this.setProperties(props, "rythm.preCompiled.root", preCompiledRoot);
		this.reloadMethodConfig(props);
		this.rootDirectoryConfig(props);
		this.tagRootDirectoryConfig(props);
		this.setProperties(props, "rythm.tmpDir", tempDirectory);
		
		this.setProperties(props, "rythm.cache.defaultTTL", cacheDefaultTTL);
		
		this.implicitConfig(props);
		
		this.setProperties(props, "rythm.cache.service", cacheService);
		this.setProperties(props, "rythm.classLoader.parent", classLoader);
		this.setProperties(props, "rythm.cache.durationParser", durationParser);
		this.setProperties(props, "rythm.tag.fileNameFilter", fileNameFilter);
		this.setProperties(props, "rythm.classLoader.hotswapAgent", hotswapAgent);
		this.loggerFactoryConfig(props);
		this.setProperties(props, "rythm.resource.loader", resourceLoader);
		this.setProperties(props, "rythm.classLoader.byteCodeHelper", byteCodeHelper);
		
		return props;
	}
	
	protected void setProperties(final Properties props, final String key, final Object value){
		if (value != null){
			props.put(key, value);
		}
	}
	
	protected void reloadMethodConfig(final Properties props){
		if (this.reloadMethod != null){
			if (Rythm.ReloadMethod.RESTART.name().equalsIgnoreCase(this.reloadMethod)){
				props.put("rythm.reloadMethod", Rythm.ReloadMethod.RESTART);
			}else if (Rythm.ReloadMethod.V_VERSION.name().equalsIgnoreCase(this.reloadMethod)){
				props.put("rythm.reloadMethod", Rythm.ReloadMethod.V_VERSION);
			}
		}
	}

	protected void implicitConfig(final Properties props) {
		if (this.implicitVariables != null || this.implicitPackages != null) {
			props.put("rythm.implicitRenderArgProvider",
					new IImplicitRenderArgProvider() {

						@Override
						public List<String> getImplicitImportStatements() {
							final List<String> packages = new ArrayList<String>();
							if (implicitPackages != null){
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
									template.setRenderArg(
											implicitVariable.getName(),
											implicitVariable.getValue());
								}
							}

						}

					});
		}
	}

	protected void loggerFactoryConfig(final Properties props) {
		if (this.loggerFactory == null) {
			this.loggerFactory = new RythmLoggerFactory();
		}

		props.put("rythm.logger.factory", this.loggerFactory);
	}

	protected void modeConfig(final Properties props) {
		if (this.mode != null) {
			if (Rythm.Mode.prod.name().equalsIgnoreCase(this.mode)) {
				props.put("rythm.mode", Rythm.Mode.prod);
			} else if (Rythm.Mode.dev.name().equalsIgnoreCase(this.mode)) {
				props.put("rythm.mode", Rythm.Mode.dev);
			}
		}
	}

	protected void rootDirectoryConfig(final Properties props) {
		if (this.rootDirectory != null) {
			props.put("rythm.root",
					this.getServletContext().getRealPath(this.rootDirectory));
		}
	}

	protected void tagRootDirectoryConfig(final Properties props) {
		if (this.tagRootDirectory != null) {
			props.put("rythm.tag.root",
					this.getServletContext().getRealPath(this.tagRootDirectory));
		}
	}

	public Boolean isAutoScanTag() {
		return autoScanTag;
	}

	public void setAutoScanTag(Boolean autoScanTag) {
		this.autoScanTag = autoScanTag;
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

	public Boolean isRefreshOnRender() {
		return refreshOnRender;
	}

	public void setRefreshOnRender(Boolean refreshOnRender) {
		this.refreshOnRender = refreshOnRender;
	}

	public Boolean isLogJavaSource() {
		return logJavaSource;
	}

	public void setLogJavaSource(Boolean logJavaSource) {
		this.logJavaSource = logJavaSource;
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

	public String getPreCompiledRoot() {
		return preCompiledRoot;
	}

	public void setPreCompiledRoot(String preCompiledRoot) {
		this.preCompiledRoot = preCompiledRoot;
	}

	public String getReloadMethod() {
		return reloadMethod;
	}

	public void setReloadMethod(String reloadMethod) {
		this.reloadMethod = reloadMethod;
	}

	public String getRootDirectory() {
		return rootDirectory;
	}

	public void setRootDirectory(String rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

	public String getTagRootDirectory() {
		return tagRootDirectory;
	}

	public void setTagRootDirectory(String tagRootDirectory) {
		this.tagRootDirectory = tagRootDirectory;
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

	public List<ITag> getTags() {
		return tags;
	}

	public void setTags(List<ITag> tags) {
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

	public FileFilter getFileNameFilter() {
		return fileNameFilter;
	}

	public void setFileNameFilter(FileFilter fileNameFilter) {
		this.fileNameFilter = fileNameFilter;
	}

	public IHotswapAgent getHotswapAgent() {
		return hotswapAgent;
	}

	public void setHotswapAgent(IHotswapAgent hotswapAgent) {
		this.hotswapAgent = hotswapAgent;
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

}

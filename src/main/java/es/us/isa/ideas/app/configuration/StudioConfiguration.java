package es.us.isa.ideas.app.configuration;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

public class StudioConfiguration implements Serializable {

    private static final Logger LOG = Logger.getLogger(StudioConfiguration.class.getName());

    protected static final String CONFIG_PATH = "/WEB-INF/config/studio-configuration.json";
    protected static final String DEVELOP_PATH = "/WEB-INF/config/develop-configuration.json";

    private static final long serialVersionUID = 1L;

    private String workbenchName;

    private Map<String, String> images;
    private Map<String, String> configurationFiles;
    private String helpURI;
    private String helpMode;
    private String googleAnalyticsID;
    private Boolean advancedMode;
    private List<String> extensionsFilter;
    private List<String> extensionsPreferences;
    private String proxyUri;
    private String dockerProxyUri;
    private List<String> modules;

    public StudioConfiguration() {
        super();
    }

    public static StudioConfiguration load(String realPath) {
        StudioConfiguration config = null;

        try {
            Gson gson = new Gson();
            String json = FileUtils.readFileToString(new File(realPath + CONFIG_PATH));
            config = gson.fromJson(json, StudioConfiguration.class);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, null, e);
        }
        
        return config;
    }

    public List<String> getModules() {
        return modules;
    }

    public void setModules(List<String> modules) {
        this.modules = modules;
    }

    public String getProxyUri() {
		return proxyUri;
	}

	public void setProxyUri(String proxyUri) {
		this.proxyUri = proxyUri;
	}

    public String getDockerProxyUri() {
		return dockerProxyUri;
	}

	public void setDockerProxyUri(String dockerProxyUri) {
		this.dockerProxyUri = dockerProxyUri;
	}

	public Map<String, String> getImages() {
        return images;
    }

    public void setImages(Map<String, String> images) {
        this.images = images;
    }

    public void setConfigurationFiles(Map<String, String> configFiles) {
        this.configurationFiles = configFiles;
    }

    public Map<String, String> getConfigurationFiles() {
        return configurationFiles;
    }

    public String getWorkbenchName() {
        return workbenchName;
    }

    public void setWorkbenchName(String workbenchName) {
        this.workbenchName = workbenchName;
    }

    public String getHelpURI() {
        return helpURI;
    }

    public void setHelpURI(String helpURI) {
        this.helpURI = helpURI;
    }

    public String getHelpMode() {
        return helpMode;
    }

    public void setHelpMode(String helpMode) {
        this.helpMode = helpMode;
    }

    public String getGoogleAnalyticsID() {
        return googleAnalyticsID;
    }

    public void setGoogleAnalyticsID(String ga) {
        this.googleAnalyticsID = ga;
    }

    public Boolean getAdvancedMode() {
        return advancedMode;
    }

    public void setAdvancedMode(Boolean advancedMode) {
        this.advancedMode = advancedMode;
    }

    public List<String> getExtensionsFilter() {
        return extensionsFilter;
    }

    public void setExtensionsFilter(List<String> extensionsFilter) {
        this.extensionsFilter = extensionsFilter;
    }

    public List<String> getExtensionsPreferences() {
        return extensionsPreferences;
    }

    public void setExtensionsPreferences(List<String> extensionsPreferences) {
        this.extensionsPreferences = extensionsPreferences;
    }
}

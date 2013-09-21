package com.ctlok.springframework.web.servlet.view.rythm.tag;

import org.springframework.core.io.Resource;

public class FileBasedTag {

    private Resource resource;
    private String tagName;

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

}

package com.ctlok.springframework.web.servlet.view.rythm;

import org.rythmengine.Rythm;
import org.springframework.web.servlet.view.AbstractTemplateView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Locale;
import java.util.Map;

/**
 * @author Lawrence Cheung
 */
public class RythmView extends AbstractTemplateView {

    @Override
    public boolean checkResource(Locale locale) throws Exception {
        final File file = new File(this.getTemplatePath());
        return file.exists() && file.isFile();
    }

    @Override
    protected void renderMergedTemplateModel(Map<String, Object> model,
                                             HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final String templatePath = this.getTemplatePath();
        response.getWriter().append(Rythm.render(templatePath, model));
    }

    protected String getTemplatePath() {
        return this.getServletContext().getRealPath(this.getUrl());
    }

}

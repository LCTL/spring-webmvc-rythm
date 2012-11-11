package com.ctlok.springframework.web.servlet.view.rythm;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractTemplateView;

import com.greenlaw110.rythm.Rythm;

/**
 * @author Lawrence Cheung
 *
 */
public class RythmView extends AbstractTemplateView {
    
    @Override
    protected void renderMergedTemplateModel(Map<String, Object> model,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final String templatePath = this.getServletContext().getRealPath(this.getUrl());
        response.getWriter().append(Rythm.render(templatePath, model));
    }

}

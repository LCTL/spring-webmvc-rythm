package com.ctlok.springframework.web.servlet.view.rythm.tag.helper;

/**
 * 
 * @author Lawrence Cheung
 *
 */
public class SpringRythmFormHelper {

    public static boolean isNotNull(Object obj){
        return obj != null;
    }
    
    public static boolean isNotNullNotEmpty(String str){
        return isNotNull(str) && !"".equals(str);
    }
    
}

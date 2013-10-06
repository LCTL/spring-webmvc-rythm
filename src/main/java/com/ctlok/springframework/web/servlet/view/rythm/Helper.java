package com.ctlok.springframework.web.servlet.view.rythm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author Lawrence Cheung
 *
 */
public class Helper {

    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir") + "/spring-webmvc-rythm";
    
	public static HttpServletRequest getCurrentRequest(){
	    try{
    		final ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    		return attr.getRequest();
	    } catch (IllegalStateException e){
	        return null;
	    }
	}
	
	public static String inputStreamToString(InputStream inputStream){
	    
	    final StringBuilder builder = new StringBuilder();
        final BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
	    
	    try{

            String line = bufferedReader.readLine();
            
            while(line != null){
                builder.append(line);
                builder.append('\n');
                line = bufferedReader.readLine();
            }
            
            bufferedReader.close();
    	    
    	    return builder.toString();
	    
	    } catch (IOException e){
	        
	        throw new IllegalStateException(e);
	        
	    } finally {
	        
	        try {
                
	            bufferedReader.close();
                
            } catch (IOException e) {
                
                throw new IllegalStateException(e);
                
            }
	        
	    }
	    
	}
	
	public static void inputStreamToOutputStream(InputStream inputStream, OutputStream outputStream){
	    
	    try{
	    
    	    int read = 0;
            byte[] bytes = new byte[1024];
     
            while ((read = inputStream.read(bytes)) != -1) {
                
                outputStream.write(bytes, 0, read);
                
            }
        
	    } catch (IOException e){
	        
	        throw new IllegalStateException(e);
	        
	    }
	    
	}
	
	public static File copyResourceToTempDirectory(Resource resource){
	    
	    final File file = createTempFile(resource.getFilename());
	    InputStream inputStream = null;
	    OutputStream outputStream = null;
	    
	    try{
	        
	        inputStream = resource.getInputStream();
	        outputStream = new FileOutputStream(file);
	        
	        inputStreamToOutputStream(inputStream, outputStream);
	        
	        return file;
	        
	    } catch (IOException e) {
	        
	        throw new IllegalStateException(e);
	        
        } finally {
	        
	        if (inputStream != null){
	            
	            try {
	                
                    inputStream.close();
                    
                } catch (IOException e) {
                    
                    throw new IllegalStateException(e);
                    
                }
	            
	        }
	        
	        if (outputStream != null){
	            
	            try {
	                
                    outputStream.close();
                    
                } catch (IOException e) {

                    throw new IllegalStateException(e);
                    
                }
	            
	        }
	        
	    }
	    
	}
	
	private static File createTempFile(String fileName){
	    
	    final File tempDir = new File(TEMP_DIR);
	    final File file = new File(TEMP_DIR + "/" + fileName);
	    
	    tempDir.mkdirs();
	    
	    return file;
	    
	}
	
}

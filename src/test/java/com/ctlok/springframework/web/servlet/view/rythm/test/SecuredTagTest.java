package com.ctlok.springframework.web.servlet.view.rythm.test;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecuredTagTest extends WebBaseTest {

    @Test
    public void adminTest() throws Exception{
        this.loginAsAdmin();
        Assert.assertEquals("admin content", this.renderTemplate("secured_admin", null));
    }
    
    @Test
    public void userTest() throws Exception{
        this.loginAsUser();
        Assert.assertEquals("user content", this.renderTemplate("secured_user", null));
    }
    
    @Test
    public void anonymousTest() throws Exception{
        this.loginAsAnonymous();
        Assert.assertEquals("anonymous content", this.renderTemplate("secured_anonymous", null));
    }
    
    @Test
    public void adminGetUserContentTest() throws Exception{
        this.loginAsAdmin();
        Assert.assertEquals("user content", this.renderTemplate("secured_user", null));
    }
    
    @Test
    public void userGetAdminContentTest() throws Exception{
        this.loginAsUser();
        Assert.assertEquals("", this.renderTemplate("secured_admin", null)); 
    }
    
    @Test
    public void anonymousGetAdminContentTest() throws Exception{
        this.loginAsAnonymous();
        Assert.assertEquals("", this.renderTemplate("secured_admin", null)); 
    }
    
    @Test
    public void userGetAnonymousContentTest() throws Exception{
        this.loginAsUser();
        Assert.assertEquals("anonymous content", this.renderTemplate("secured_anonymous", null)); 
    }
    
    protected void loginAsAdmin(){
        this.login("admin", "admin");
    }
    
    protected void loginAsUser(){
        this.login("user", "user");
    }
    
    protected void loginAsAnonymous(){
        this.login("anonymous", "anonymous");
    }
    
    protected void login(String username, String password){
        AuthenticationManager authenticationManager = this.getBean(AuthenticationManager.class);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    
}

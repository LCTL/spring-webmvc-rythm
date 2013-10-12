# Rythm Template Engine for Spring Web

This is a plugin for Spring to adapt Rythm template engine. 
Rythm template engine is a high performance templates engine which is multiple times faster than groovy, velocity and freemarker.

Rythm template engine source: [https://github.com/greenlaw110/Rythm] (https://github.com/greenlaw110/Rythm)

Rythm template document: [http://rythmengine.org] (http://rythmengine.org)

Demo Project: [https://github.com/lawrence0819/spring-webmvc-rythm-demo] (https://github.com/lawrence0819/spring-webmvc-rythm-demo)

## Dependencies:

* Rythm 1.0-b9 or above
* Servlet 2.5 or above
* Spring web-mvc 3.1.0 or above
* Slf4j 1.6.6 or above

## Integrate with Spring Web

### Maven:

```
<dependency>
    <groupId>com.ctlok</groupId>
    <artifactId>spring-webmvc-rythm</artifactId>
    <version>1.4.0</version>
</dependency>
```

### Java Project:

1. Download latest release: <http://search.maven.org/remotecontent?filepath=com/ctlok/spring-webmvc-rythm/1.3.6/spring-webmvc-rythm-1.3.6.jar>
2. Download Spring Framework with web mvc: <http://projects.spring.io/spring-framework/>
3. Download Slf4j: <http://www.slf4j.org/>
4. Download logback: <http://logback.qos.ch/download.html>

### Spring config example

```
<bean id="rythmConfigurator" class="com.ctlok.springframework.web.servlet.view.rythm.RythmConfigurator">
    <property name="mode" value="dev" />
    <property name="rootDirectory" value="/WEB-INF/views/" />
</bean>

<bean id="rythmViewResolver" class="com.ctlok.springframework.web.servlet.view.rythm.RythmViewResolver">
    <constructor-arg>
        <ref bean="rythmConfigurator" />
    </constructor-arg>
    <property name="prefix" value="/WEB-INF/views/" />
    <property name="suffix" value=".html" />
</bean>
```

## Support Spring Internationalization (I18N)

```
<bean class="org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter" />
    
<bean class="org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping">
    <property name="interceptors">
        <ref bean="localeChangeInterceptor" />
    </property>
</bean>

<bean id="messageSource" class="org.springframework.context.support.ResourceBundleMessageSource">
    <property name="basename" value="language/messages" />
    <property name="defaultEncoding" value="UTF-8" />
</bean>

<bean id="localeResolver" class="org.springframework.web.servlet.i18n.CookieLocaleResolver">
    <property name="defaultLocale" value="en" />
    <property name="cookieName" value="locale" />
    <property name="cookieMaxAge" value="31536000" />
</bean>

<bean id="localeChangeInterceptor" class="org.springframework.web.servlet.i18n.LocaleChangeInterceptor">
    <property name="paramName" value="lang" />
</bean>
```

***

`@message` tag help you to get current locale message in template: 

```
<p>@message("message.code")</p>
<p>@message("message.code", "arg0", "arg1")</p>
<p>@message({code: "message.code.null", default: "Output me if code not found"})</p>
<p>@message({code: "message.code.null", default: "Output me if code not found", "arg0", "arg1"})</p>
<p>@message({code: "message.code", locale: "fr"})</p>
<p>@message({code: "message.code", locale: "fr", "arg0", "arg1"})</p>
<p>@message({code: "message.code", default: "Output me if code not found", locale: "fr"})</p>
<p>@message({code: "message.code", default: "Output me if code not found", locale: "fr", "arg0", "arg1"})</p>
```

## Support Spring cache in template

```
<bean id="cacheManagerFactory" class="org.springframework.cache.ehcache.EhCacheManagerFactoryBean">
    <property name="configLocation" value="classpath:ehcache.xml" />
</bean>

<bean id="cacheManager" class="org.springframework.cache.ehcache.EhCacheCacheManager">
    <property name="cacheManager" ref="cacheManagerFactory" />
</bean>

<bean id="rythmConfigurator" class="com.ctlok.springframework.web.servlet.view.rythm.RythmConfigurator">
    <property name="mode" value="dev" />
    <property name="rootDirectory" value="/WEB-INF/views/" />
    <property name="tagRootDirectory" value="/WEB-INF/views/tags/" />
    <property name="cacheInProductionModeOnly" value="false" />
    <property name="cacheManager" ref="cacheManager" />
    <property name="springCacheName" value="RYTHM_CACHE" />
</bean>
```

ehcache.xml:
```
<?xml version="1.0" encoding="UTF-8"?>
<ehcache>

    <diskStore path="java.io.tmpdir" />
    
    <defaultCache 
        maxElementsInMemory="300" eternal="false" timeToIdleSeconds="500"
        timeToLiveSeconds="500" overflowToDisk="true" />
        
    <cache name="RYTHM_CACHE" maxElementsInMemory="300" eternal="false" timeToIdleSeconds="500"
        timeToLiveSeconds="3600" overflowToDisk="false" />
        
</ehcache>
```

How to use cache in Rythm template: [http://play-rythm-demo.appspot.com/demo/testcache] (http://play-rythm-demo.appspot.com/demo/testcache)

## Support Spring Security (Optional)

Dependencies:

* Spring Security Core 3.1.0 or above

```
<authentication-manager>
    <authentication-provider>
        <user-service>
            <user name="admin" password="admin" authorities="ROLE_ADMIN, ROLE_USER" />
            <user name="user" password="user" authorities="ROLE_USER" />
            <user name="anonymous" password="anonymous" authorities="ROLE_ANONYMOUS" />
        </user-service>
    </authentication-provider>
</authentication-manager> 
```

***

`@secured` tag help you to check current user is it has role and display content:

```
@secured("ROLE_ADMIN"){
    admin content
}

@secured("ROLE_USER"){
    user content
}

@secured("ROLE_USER", "ROLE_ANONYMOUS"){
    anonymous content
}
```

## Build in Tag

Use `@url` tag to add context path before resource path:

```
<script type="text/javascript" src="@url("/javascripts/jQuery.min.js")"></script>

Result: <script type="text/javascript" src="my-app/javascripts/jQuery.min.js"></script>
```


Use `@fullUrl` tag to print current server full URL path with resource:

```
<img src="@fullUrl("/images/xyz.png")" />

Result: <img src="http://pro.ctlok.com/images/xyz.png" />
```

Use `@dateFormat` tag to print formatted date string:

```
@dateFormat(user.getCreateDate(), format: "dd-MM-yyyy HH:mm")

Result: 22-09-2013 14:28
```

Use `@cookieValue` tag to print cookie or assign to variable:

```
@cookieValue("mode").assign("mode")
```

### CARF Token

Support auto CSRF token validation in POST request to prevent cross-site request forgery.

#### Spring Config:

```
<mvc:interceptors>
    <bean class="com.ctlok.springframework.web.servlet.view.rythm.interceptor.CsrfTokenInterceptor" />
</mvc:interceptors>
```

#### Template:

```
<form action="register.html" method="post">
    @hiddenCsrfToken()
</form>
```

#### Template result:

```
<form action="register.html" method="post">
    <input type="hidden" value="8e81a756f8884b70994c1111475d78e4" name="_t" />
</form>
```

## Custom Tag

spring-webmvc-rythm support 2 type custom tag.

### Java Based Tag

1. Create class inherit from org.rythmengine.template.JavaTagBase
2. Override `__getName()` method and return your tag name
3. Implement `call(__ParameterList params, __Body body)`
4. Config com.ctlok.springframework.web.servlet.view.rythm.RythmConfigurator

```
<bean id="rythmConfigurator" class="com.ctlok.springframework.web.servlet.view.rythm.RythmConfigurator">
    <property name="mode" value="dev" />
    <property name="rootDirectory" value="/WEB-INF/views/" />
    <property name="tags">
        <list>
            <bean class="com.xyz.tags.HelloWorld" />
        </list>
    </property>
</bean>
```

### File Based Tag

1. Create template file
2. Add template file to classpath
3. Config com.ctlok.springframework.web.servlet.view.rythm.RythmConfigurator

```
<bean id="rythmConfigurator" class="com.ctlok.springframework.web.servlet.view.rythm.RythmConfigurator">
    <property name="mode" value="dev" />
    <property name="rootDirectory" value="/WEB-INF/views/" />
    <property name="fileBasedTags">
        <list>
            <bean class="com.ctlok.springframework.web.servlet.view.rythm.tag.FileBasedTag">
                <property name="resource" value="classpath:custom_tags/helloWorld.html" />
                <property name="tagName" value="helloWorld" />
            </bean>
        </list>
    </property>
</bean>
```

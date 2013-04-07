# Rythm Template Engine for Spring Web

This is a plugin for Spring to adapt Rythm template engine. 
Rythm template engine is a high performance templates engine which is multiple times faster than groovy, velocity and freemarker.

Rythm template engine source: [https://github.com/greenlaw110/Rythm] (https://github.com/greenlaw110/Rythm)

Rythm template engine tutorial: [http://play-rythm-demo.appspot.com/] (http://play-rythm-demo.appspot.com/)


# How to integrate with Spring Web

Dependencies:

* Rythm 1.0-b4 or above
* Servlet 2.5 or above
* Spring web-mvc 3.1.0 or above
* Slf4j 1.6.6 or above

***

Maven project follow below step:

1. Download this plugin source.
2. Also run Maven command: mvn install
3. Add dependency in your pom.xml:

```
<dependency>
    <groupId>com.ctlok</groupId>
    <artifactId>spring-webmvc-rythm</artifactId>
    <version>${spring-web-rythm-template.version}</version>
</dependency>
```

***

## Spring config example

```
<bean id="rythmConfigurator" class="com.ctlok.springframework.web.servlet.view.rythm.RythmConfigurator">
    <property name="mode" value="dev" />
    <property name="rootDirectory" value="/WEB-INF/views/" />
    <property name="tagRootDirectory" value="/WEB-INF/views/tags/" />
</bean>

<bean id="rythmViewResolver" class="com.ctlok.springframework.web.servlet.view.rythm.RythmViewResolver">
    <constructor-arg>
        <ref bean="rythmConfigurator" />
    </constructor-arg>
    <property name="prefix" value="/WEB-INF/views/" />
    <property name="suffix" value=".html" />
</bean>
```

### Support Spring Internationalization (I18N)

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

### Support Spring cache in template

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

### Support Spring Security (Optional)

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

`@secured` tag help you check current user is it has role and display conten:

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

`@url` tag help you to add context path before resource path:

```
@url("/javascripts/jQuery.min.js")
```

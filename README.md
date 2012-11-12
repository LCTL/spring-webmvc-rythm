# Rythm Template Engine for Spring Web

This is a plugin for Spring to adapt Rythm template engine. 
Rythm template engine is a high performance templates engine which is multiple times faster than groovy, velocity and freemarker.

Rythm template engine source: [https://github.com/greenlaw110/Rythm] (https://github.com/greenlaw110/Rythm)

Rythm template engine tutorial: [http://play-rythm-demo.appspot.com/] (http://play-rythm-demo.appspot.com/)


# How to integrate with Spring Web

Dependencies:

* Rythm 1.0.0-20121110 or above
* Servlet 2.5 or above
* Spring Webmvc 3.0.0 or above
* Slf4j 1.6.6 or above

***

Maven project follow below step:

1. Download Rythm template engine source.
2. Run Maven command: mvn install
3. Download this plugin source.
4. Also run Maven command: mvn install
5. Add dependency in your pom.xml:

```
<dependency>
    <groupId>com.ctlok</groupId>
    <artifactId>spring-web-rythm-template</artifactId>
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

### Support Spring Security (Optional)

Dependencies:

* Spring Security Core 3.0.0 or above

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
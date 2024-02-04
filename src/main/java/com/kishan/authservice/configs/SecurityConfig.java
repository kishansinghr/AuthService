package com.kishan.authservice.configs;

import com.kishan.authservice.beans.SessionDataBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
public class SecurityConfig {

    private static final ThreadLocal<SessionDataBean> sessionDataBeanThreadLocal = new ThreadLocal<>();

    public static void setSessionDataBean(SessionDataBean sessionDataBean) {
        sessionDataBeanThreadLocal.set(sessionDataBean);
    }

    @Bean
    @Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public SessionDataBean getSessionDataBean() {
        return sessionDataBeanThreadLocal.get();
    }
}

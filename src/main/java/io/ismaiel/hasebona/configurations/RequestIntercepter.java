/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.ismaiel.hasebona.configurations;

import io.ismaiel.hasebona.entities.Sheet;
import io.ismaiel.hasebona.entities.User;
import io.ismaiel.hasebona.models.Session;
import io.ismaiel.hasebona.repos.SheetRepo;
import io.ismaiel.hasebona.repos.UserRepo;
import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

/**
 * @author Ismaiel Nosairat <ismaiel.nosairat@gmail.com>
 */
@Configuration
public class RequestIntercepter {

    @Autowired
    UserRepo userRepo;
    @Autowired
    SheetRepo sheetRepo;

    @Bean
    ServletRegistrationBean h2servletRegistration() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public Session gerSessionVariables(HttpServletRequest req) {
        Session session = new Session();
        String token = req.getHeader("token");
        String sheetId = req.getHeader("sheetId");

        Map<String, Object> tokenMap = null;
        try {
            tokenMap = GV.symClient.parseJWT(token);
            Long userId = tokenMap.get("userId") == null ? null : Long.valueOf(tokenMap.get("userId").toString());
            if (userId != null) {
                Optional<User> found = userRepo.findById(userId);
                if (found.isPresent()) {
                    session.setUser(found.get());
                }
            }
        } catch (Exception e) {

        }
        if (sheetId != null) {
            Long sheetIdAsLong = Long.decode(sheetId);
            Optional<Sheet> foundSheet = sheetRepo.findById(sheetIdAsLong);
            foundSheet.ifPresent(session::setSheet);
        }
        return session;
    }

    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }

}

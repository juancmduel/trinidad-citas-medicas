package com.trinidad.citas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // WebJars (Bootstrap, jQuery)
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .setCachePeriod((int) TimeUnit.DAYS.toSeconds(30));

        // Recursos estaticos con cache de 7 dias + versionado via ?v=hash
        // Para forzar actualizacion, cambiar el numero de version en los templates
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/")
                .setCachePeriod((int) TimeUnit.DAYS.toSeconds(7));

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/")
                .setCachePeriod((int) TimeUnit.DAYS.toSeconds(7));

        registry.addResourceHandler("/img/**")
                .addResourceLocations("classpath:/static/img/")
                .setCachePeriod((int) TimeUnit.DAYS.toSeconds(7));
    }
}

package no.itminds.movies.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//package no.itminds.movies.config;
//
//import org.springframework.boot.web.server.ErrorPage;
//import org.springframework.boot.web.server.WebServerFactoryCustomizer;
//import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//
//@Component
//public class MvcConfiguration implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
//	
//	@Override
//    public void customize(ConfigurableServletWebServerFactory factory) {
//        factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/notfound"));
//        factory.addErrorPages(new ErrorPage("/error"));
//    }
//
//}


//@Configuration
//@EnableWebMvc
//public class WebConfig implements WebMvcConfigurer {
//
//	@Bean
//	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
//	    return new PropertySourcesPlaceholderConfigurer();
//	}
//	
//	@Value("${ALLOWED_ORIGINS}")
//	private String allowedOrigins;
//	
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//
//        registry.addMapping("/api/**")
//            .allowedOrigins(allowedOrigins)
//            .allowedMethods("PUT", "DELETE", "GET", "POST")
//            .allowedHeaders("*")
//            .allowCredentials(true).maxAge(3600);
//        
//        registry.addMapping("/login")
//        .allowedOrigins(allowedOrigins)
//        .allowedMethods("POST")
//        .allowedHeaders("*")
//        .allowCredentials(true).maxAge(3600);
//    }
//}

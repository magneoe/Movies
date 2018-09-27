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
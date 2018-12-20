package no.itminds.movies.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import no.itminds.movies.filters.MyCustomFilter;

//@Configuration
//public class WebConfig {
//	
//	@Bean
//	public FilterRegistrationBean<MyCustomFilter> myCustomFilter(){
//		
//		FilterRegistrationBean<MyCustomFilter> filterRegBean = new FilterRegistrationBean<>();
//		filterRegBean.setFilter(new MyCustomFilter()); 
//		filterRegBean.addUrlPatterns("/test/");
//		filterRegBean.setOrder(Ordered.LOWEST_PRECEDENCE-1);
//		
//		return filterRegBean;
//	}
//}
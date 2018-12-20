package no.itminds.movies.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyCustomFilter implements Filter {

	private final static Logger logger = LoggerFactory.getLogger(MyCustomFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		logger.info("MyCustomFilter: doFilter");
		
		ServletOutputStream sos = response.getOutputStream();
		String value = "";
		if((value = request.getParameter("testAtr")) != null) {
			logger.info("Attr=" + value);
			sos.println("Parameter is: " + value);
		}
		else 
			chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}

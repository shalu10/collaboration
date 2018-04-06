package com.niit.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;



public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		
		return new Class[]{ApplicationContextConfig.class, WebSocketConfig.class, WebConfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {

		return new Class[]{WebSocketConfig.class, WebConfig.class};
	}

	@Override
	protected String[] getServletMappings() {

		return new String[]{"/"};
	
	}
	
	//System.getProperty("catalina.home");
	//Path.transferimage
	

}

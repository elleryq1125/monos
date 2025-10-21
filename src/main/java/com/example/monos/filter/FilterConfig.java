package com.example.monos.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>各FilterをBeanに登録する。</p>
 * @author t.ueta
 */
@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean<AccessLogingFilter> accessLogingFilter(){
		FilterRegistrationBean<AccessLogingFilter> filterRetistBean = new FilterRegistrationBean<>();
		filterRetistBean.setFilter(new AccessLogingFilter());
		filterRetistBean.addUrlPatterns("/*");
		filterRetistBean.setOrder(1);
		return filterRetistBean;
	}
}

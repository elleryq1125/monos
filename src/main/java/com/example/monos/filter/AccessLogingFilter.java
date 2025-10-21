package com.example.monos.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>共通のアクセス情報をログに記録する。</p>
 * @author t.ueta
 */
public class AccessLogingFilter extends OncePerRequestFilter {
	private final static Logger  log = LoggerFactory.getLogger(AccessLogingFilter.class);
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		filterChain.doFilter(request, response);
	
		String method = request.getMethod();
		String uri = request.getRequestURI();
		String query = (request.getQueryString() == null ? "":request.getQueryString()) ;
		int status = response.getStatus();
		
		log.info("{} {} {} {}", method, uri, query, status);
	}

}

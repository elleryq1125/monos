package com.example.monos.filter;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.monos.domain.UserDetailsImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>MDCにログで利用する認証情報を格納する。</p>
 * @author t.ueta
 */
public class UserMdcLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
    		throws ServletException, IOException {

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            	// ユーザ名をMDCに格納
                var userDetails = (UserDetailsImpl)auth.getPrincipal();
            	MDC.put("user", Integer.toString(userDetails.getUserId())); 
            } else {
            	// 未ログイン時
                MDC.put("user", "anonymous");    
            }

            filterChain.doFilter(request, response);
        } finally {
        	// スレッド再利用対策（必ず削除）
            MDC.remove("user"); 
        }
    }
}

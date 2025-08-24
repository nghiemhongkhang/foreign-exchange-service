package com.nhk.fx.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class LoggingFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        long start = System.currentTimeMillis();
        log.info("Incoming request: {} {}", request.getMethod(), request.getRequestURI());

        filterChain.doFilter(request, response);

        long time = System.currentTimeMillis() - start;
        log.info("Response: {} {} ({} ms)", request.getMethod(), request.getRequestURI(), time);
    }
}

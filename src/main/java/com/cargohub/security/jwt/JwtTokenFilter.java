package com.cargohub.security.jwt;

import com.cargohub.config.RestAuthenticationEntryPoint;
import com.cargohub.exceptions.JwtAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtTokenFilter extends GenericFilterBean {

    private JwtTokenProvider jwtTokenProvider;
    private RestAuthenticationEntryPoint authenticationEntryPoint;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, RestAuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {

        try {
            String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);

                if (auth != null) {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
            filterChain.doFilter(req, res);
        } catch (JwtAuthenticationException ex) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence((HttpServletRequest)req, (HttpServletResponse) res, ex);
        }
    }

}

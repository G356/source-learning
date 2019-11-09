package com.springboot.security.config;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class CsrfTokenSetFilter extends OncePerRequestFilter {

  private static final String REQUEST_ATTRIBUTE_NAME = "_csrf";
  private static final String RESPONSE_HEADER_NAME = "X-CSRF-HEADER";
  private static final String RESPONSE_PARAM_NAME = "X-CSRF-PARAM";
  private static final String RESPONSE_TOKEN_NAME = "X-CSRF-TOKEN";

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      javax.servlet.FilterChain filterChain) throws ServletException, IOException {
    CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    if (token != null) {
      log.info("token : " + token.getToken());
      response.setHeader(RESPONSE_HEADER_NAME, token.getHeaderName());
      response.setHeader(RESPONSE_PARAM_NAME, token.getParameterName());
      response.setHeader(RESPONSE_TOKEN_NAME, token.getToken());
    }
    filterChain.doFilter(request, response);
  }

}

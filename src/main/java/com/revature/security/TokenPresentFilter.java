package com.revature.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.Span;
import co.elastic.apm.api.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author William Gentry
 */
@Component
public class TokenPresentFilter extends OncePerRequestFilter {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final AuthenticationManager authenticationManager;

  public TokenPresentFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
    final String uri = httpServletRequest.getRequestURI();
    if (uri.contains("actuator")) {
      logger.debug("Request to actuator endpoint. Ignoring");
      filterChain.doFilter(httpServletRequest, httpServletResponse);
      return;
    }
    final String header = httpServletRequest.getHeader("Authorization");
    String token = null;
    Authentication authentication = null;
    if (header != null && header.startsWith("Bearer ")) {
      Transaction transaction = ElasticApm.currentTransaction();
      transaction.ensureParentId();
      Span authenticationSpan = transaction.startSpan();
      authenticationSpan.setName("Token Authentication");
      token = header.replace("Bearer ", "");
      authentication = authenticationManager.authenticate(new UnauthenticatedLibraryCard(token));
      authenticationSpan.end();
    } else {
      logger.info("No JWT Token present. Ignoring header");
    }

    if (authentication instanceof LibraryCard) {
      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
              authentication.getName(), "", AuthorityUtils.createAuthorityList("ROLE_USER"));
      auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
      SecurityContextHolder.getContext().setAuthentication(auth);
    }

    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }
}

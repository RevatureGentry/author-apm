package com.revature.security;

import java.util.Map;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

/**
 * @author William Gentry
 */
public class LibraryCard extends AbstractAuthenticationToken {

  private final Map<String, Object> claims;

  public LibraryCard(Map<String, Object> claims) {
    super(AuthorityUtils.NO_AUTHORITIES);
    this.claims = claims;
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return claims.getOrDefault("email", "");
  }

  @Override
  public String getName() {
    return (String) claims.getOrDefault("email", "");
  }
}

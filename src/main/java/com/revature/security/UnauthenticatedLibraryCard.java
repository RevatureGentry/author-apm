package com.revature.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * @author William Gentry
 */
public class UnauthenticatedLibraryCard extends UsernamePasswordAuthenticationToken {

  public UnauthenticatedLibraryCard(String bearerToken) {
    super(null, bearerToken);
  }
}

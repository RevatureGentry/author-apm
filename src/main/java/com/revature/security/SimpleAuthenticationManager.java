package com.revature.security;

import java.util.Collections;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * @author William Gentry
 */
@Component
public class SimpleAuthenticationManager implements AuthenticationManager {

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    return new LibraryCard(Collections.singletonMap("email", "mock@test.com"));
  }
}

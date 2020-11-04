package com.revature.author;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author William Gentry
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class AuthorNotFoundException extends RuntimeException {

  public AuthorNotFoundException() {
    super("Failed to locate author");
  }
}

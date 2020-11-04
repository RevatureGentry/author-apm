package com.revature.author;

import java.net.URI;
import java.util.*;

import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author William Gentry
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/author")
public class AuthorController {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final AuthorService authorService;

  public AuthorController(AuthorService authorService) {
    this.authorService = authorService;
  }

  @GetMapping
  public ResponseEntity<List<Author>> findAll(@AuthenticationPrincipal String user) {
    Transaction transaction = ElasticApm.currentTransaction();
    transaction.ensureParentId();
    logger.debug("{} attempting to find all authors", user);
    List<Author> authors = authorService.findAll();
    if (authors == null || authors.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(authors);
  }

  @GetMapping(path = "/{id}")
  public ResponseEntity<Author> findById(@PathVariable("id") int id, @AuthenticationPrincipal String user) {
    Transaction transaction = ElasticApm.currentTransaction();
    transaction.ensureParentId();
    logger.debug("{} attempting to find author with id {}", user, id);
    Author author = authorService.findById(id);
    return ResponseEntity.ok(author);
  }

  @PostMapping
  public ResponseEntity<Void> create(@RequestBody Author author, @AuthenticationPrincipal String user) {
    Transaction transaction = ElasticApm.currentTransaction();
    transaction.ensureParentId();
    logger.debug("{} attempting to create author", user);
    try {
      Author created = authorService.save(author);
      return ResponseEntity.created(URI.create(String.format("/author/%d", created.getId()))).build();
    } catch (IllegalArgumentException e) {
      logger.warn("Failed to create author", e);
      return ResponseEntity.badRequest().build();
    }
  }
}

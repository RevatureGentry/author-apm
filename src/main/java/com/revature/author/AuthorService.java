package com.revature.author;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author William Gentry
 */
@Service
public class AuthorService {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final AuthorRepository authorRepository;

  public AuthorService(AuthorRepository authorRepository) {
    this.authorRepository = authorRepository;
  }

  public List<Author> findAll() {
    logger.debug("Finding all authors!");
    return authorRepository.findAll();
  }

  public Author findById(int id) {
    logger.debug("Finding author by id! {}", id);
    return authorRepository.findById(id).orElseThrow(AuthorNotFoundException::new);
  }

  public Author save(Author author) {
    logger.debug("Attempting to save {}", author);
    return authorRepository.save(author);
  }
}

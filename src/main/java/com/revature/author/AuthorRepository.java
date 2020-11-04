package com.revature.author;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author William Gentry
 */
public interface AuthorRepository extends JpaRepository<Author, Integer> {
}

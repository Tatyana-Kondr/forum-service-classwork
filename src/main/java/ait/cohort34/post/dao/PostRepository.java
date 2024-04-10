package ait.cohort34.post.dao;

import ait.cohort34.post.model.Post;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Stream;

public interface PostRepository extends CrudRepository<Post, String> {
    Stream<Post> findByAuthorIgnoreCase(String author);
    Stream<Post> findByTagsIn(Set<String> tags);
    Stream<Post> findByDateCreatedBetween(LocalDateTime dateFrom, LocalDateTime dateTo);
}

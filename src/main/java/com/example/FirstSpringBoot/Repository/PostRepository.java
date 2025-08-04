package com.example.FirstSpringBoot.Repository;

import com.example.FirstSpringBoot.Models.Post;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends MongoRepository<Post,String> {
    List<Post> findByCategoryOrderByDateDesc(String category);
    List<Post> findByCategoryIgnoreCase(String category, Pageable pageable);
    long countByCategoryIgnoreCase(String category);
    List<Post> findByCategory(String category);
    List<Post> findByAuthor(String author);
    Optional<Post> findById(String id);
    @Aggregation("{ $group: { _id: '$category' } }")
    List<CategoryResult> findDistinctCategories();
    interface CategoryResult {
        String get_id();
    }
}

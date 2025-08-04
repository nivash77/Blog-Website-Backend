package com.example.FirstSpringBoot.Services;

import com.example.FirstSpringBoot.Models.Command;
import com.example.FirstSpringBoot.Models.Post;
import com.example.FirstSpringBoot.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Post addPost(Post post) {
        if (post.getTitle() == null || post.getDesc() == null || post.getImg() == null ||
                post.getAuthor() == null || post.getDate() == null || post.getCategory() == null) {
            throw new IllegalArgumentException("All fields are required");
        }

        if (post.getCommands() == null) {
            post.setCommands(new ArrayList<>());
        }

        if (post.getViewedBy() == null) {
            post.setViewedBy(new ArrayList<>());
        }

        return postRepository.save(post);
    }

    public Optional<Post> getPostById(String id, String userId, boolean newView) {
        Optional<Post> optionalPost = postRepository.findById(id);

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            if (post.getViewedBy() == null) {
                post.setViewedBy(new ArrayList<>());
            }
//            System.out.println("UserId: " + userId);
//            System.out.println("Viewed Users: " + post.getViewedBy());

            if ( userId !=null && newView && !post.getViewedBy().contains(userId)) {
//                System.out.println("Incrementing view count...");
                post.getViewedBy().add(userId);
                post.setViews(post.getViews() + 1);
                postRepository.save(post);
            }

            return Optional.of(post);
        } else {
            return Optional.empty();
        }
    }



    public Object getposts(int page, String category, int limit) {
        int skip = (page - 1) * limit;
        List<Post> posts;
        long totalposts;

        if (category != null && !category.equalsIgnoreCase("All")) {
            posts = postRepository.findByCategory(category)
                    .stream()
                    .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                    .skip(skip)
                    .limit(limit)
                    .toList();
            totalposts = postRepository.findByCategory(category).size();
        } else {
            posts = postRepository.findAll(PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "date"))).getContent();
            totalposts = postRepository.count();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("posts", posts);
        response.put("totalPages", (int) Math.ceil((double) totalposts / limit));
        response.put("currentPage", page);

        return response;
    }

    public String  updatePostByFields(Post updatedPost) {

        if (updatedPost.getId() == null) {
            return "Post ID is required";
        }

        Optional<Post> optionalPost = postRepository.findById(updatedPost.getId());

        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();
            existingPost.setTitle(updatedPost.getTitle());
            existingPost.setDesc(updatedPost.getDesc());
            existingPost.setImg(updatedPost.getImg());
            existingPost.setCategory(updatedPost.getCategory());
            existingPost.setAuthor(updatedPost.getAuthor());
            existingPost.setDate(updatedPost.getDate());


            postRepository.save(existingPost);
            return "Post updated successfully";
        } else {
            return "Post not found";
        }
    }
    public List<Post> getPostsByAuthor(String author) {
        return postRepository.findByAuthor(author);
    }

    public boolean addCommandToPost(String postId, Command command) {
        Query query = new Query(Criteria.where("_id").is(postId));
        Update update = new Update().push("commands", command);
        return mongoTemplate.updateFirst(query, update, Post.class).getModifiedCount() > 0;
    }


    public List<String> getcategories() {
        List<PostRepository.CategoryResult> result = postRepository.findDistinctCategories();
        return result.stream().map(PostRepository.CategoryResult::get_id).collect(Collectors.toList());
    }
    public boolean deletePostById(String id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public boolean deleteCommentFromPost(String postId, int index) {
        Post post = mongoTemplate.findById(postId, Post.class);
        if (post == null || post.getCommands() == null || index < 0 || index >= post.getCommands().size()) {
            return false;
        }

        post.getCommands().remove(index);
        mongoTemplate.save(post);
        return true;
    }

    public String updateComment(String id, String username, String command) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            boolean updated = false;

            for (Command c : post.getCommands()) {
                if (c.getUsername().equals(username)) {
                    c.setCommand(command);
                    updated = true;
                    break;
                }
            }

            if (updated) {
                postRepository.save(post);
                return "Comment updated successfully";
            } else {
                return "Comment not found";
            }
        } else {
            return "Post not found";
        }
    }
}

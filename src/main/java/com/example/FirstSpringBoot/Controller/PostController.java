package com.example.FirstSpringBoot.Controller;

import com.example.FirstSpringBoot.Models.Command;
import com.example.FirstSpringBoot.Models.Post;
import com.example.FirstSpringBoot.Services.PostService;
import jdk.jfr.Category;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    PostService postService;


    @PostMapping("/addpost")
    public ResponseEntity<?> addpost(@RequestBody Post post){
        try {
            Post savedPost = postService.addPost(post);
            return ResponseEntity.ok().body(Map.of(
                    "success", true,
                    "data", savedPost
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }

    }

    @GetMapping("/post/{id}")
    public ResponseEntity<?> getPost(
            @PathVariable("id") String id,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false,defaultValue = "true") boolean newView
    ) {
        try {
            Optional<Post> postOptional = postService.getPostById(id,userId, newView);
            if (postOptional.isPresent()) {
                return ResponseEntity.ok().body(postOptional.get());
            } else {
                return ResponseEntity.status(404).body("Post not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }



    @GetMapping("/posts")
    public ResponseEntity<?> getposts(@RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String category) {
        try {
            int limit = 6;
            return ResponseEntity.ok(postService.getposts(page, category, limit));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error:" + e.getMessage());
        }
    }

    @PostMapping("/updatePost")
    public ResponseEntity<String> updatePost(@RequestBody Post post) {
        try {
            String response = postService.updatePostByFields(post);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating post: " + e.getMessage());
        }
    }
    @GetMapping("/userPost")
    public ResponseEntity<?> getPostsByAuthor(@RequestParam String username) {
        if (username == null || username.isEmpty()) {
            return ResponseEntity.badRequest().body("Username is required");
        }

        List<Post> posts = postService.getPostsByAuthor(username);
        return ResponseEntity.ok().body(posts);
    }

    @PostMapping("/addCommand")
    public ResponseEntity<?> addComment(@RequestBody Map<String, String> body) {
        String postId = body.get("postId");
        String username = body.get("username");
        String command = body.get("command");

        if (postId == null || username == null || command == null) {
            return ResponseEntity.badRequest().body("All fields are required");
        }

        Command newCommand = new Command(username, command);
        boolean result = postService.addCommandToPost(postId, newCommand);

        if (!result) {
            return ResponseEntity.status(500).body("Failed to add comment");
        }
        return ResponseEntity.ok(newCommand);
    }


    @GetMapping("/Categories")
    public ResponseEntity<?> getCategories(){
        try{
            List<String> categories=postService.getcategories();
            return ResponseEntity.ok().body(categories);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error:"+e.getMessage());
        }
    }

    @DeleteMapping("/deleteCommand")
    public ResponseEntity<?> deleteComment(@RequestBody Map<String, Object> body) {
        String postId = (String) body.get("id");
        Integer commandIndex = (Integer) body.get("commandIndex");

        if (postId == null || commandIndex == null) {
            return ResponseEntity.badRequest().body("Post ID and Command Index are required");
        }

        boolean result = postService.deleteCommentFromPost(postId, commandIndex);

        if (!result) {
            return ResponseEntity.status(404).body("Comment not found or no changes made");
        }

        return ResponseEntity.ok("Comment deleted successfully");
    }


    @PutMapping("/updateCommand")
    public ResponseEntity<Map<String, String>> updateComment(@RequestBody Map<String, String> request) {
        String id = request.get("id");
        String command = request.get("command");
        String username = request.get("username");

        if (id == null || command == null || username == null || command.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "All fields are required and comment cannot be empty"));
        }

        String message = postService.updateComment(id, username, command);

        if (message.equals("Comment updated successfully")) {
            return ResponseEntity.ok(Collections.singletonMap("message", message));
        } else if (message.equals("Comment not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", message));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", message));
        }
    }

}

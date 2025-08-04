package com.example.FirstSpringBoot.Controller;

import com.example.FirstSpringBoot.Exceptions.UserNotFoundException;
import com.example.FirstSpringBoot.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.FirstSpringBoot.Models.User;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService user_service;



//    @PostMapping("/addUser")
//    public String addUser(@RequestBody Map<String,Object> req){
//
//     return user_service.addUser((int) req.get("Id"),(String) req.get("username"),(String) req.get("email"),(String) req.get("password"),(String) req.get("DOB"));
//
//    }
    @PostMapping("/addUser")
    public User addUser(@RequestBody User user){
        return user_service.register(user);
    }

    @PostMapping("/checkUser")
    public ResponseEntity<Map<String, String>> checkUser(@RequestBody User user){
        boolean login=user_service.login(user.getUsername(),user.getPassword());
        Map<String, String> response = new HashMap<>();
        if (login) {
            response.put("message", "Login successful");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Invalid credentials");
            return ResponseEntity.status(401).body(response);
        }
    }
    @GetMapping("/getUser")
    public ResponseEntity<?> getUser(@RequestParam String username){

        try {
            User user = user_service.getUserByname(username);
           // System.out.println("🔍 Requested username: " + user.getDob());
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            //System.out.println("❌ Exception occurred:");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/updateUser")
    public String updateUser(@RequestBody Map<String,String> user){
        return user_service.updateUser(user.get("username"),user.get("email"),user.get("password"),user.get("dob"));
    }
    @PostMapping("/updatePassword")
    public String updatePassword(@RequestBody Map<String,String> user){
        return user_service.updatePassword(user.get("username"),user.get("password"));
    }
}

package com.example.FirstSpringBoot.Services;

import com.example.FirstSpringBoot.Exceptions.UserAlreadyExistsException;
import com.example.FirstSpringBoot.Exceptions.UserNotFoundException;
import com.example.FirstSpringBoot.Models.User;
import com.example.FirstSpringBoot.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import  java.util.List;

@Service
public class UserService {

    Map<Integer,User> users= new LinkedHashMap<Integer, User>();

    @Autowired
    UserRepository userRepository;
    public List<User> getUsers(){
        return List.copyOf(users.values());
    }

    public String addUser(int id, String username, String email, String password, String dob) {
//        if(users.containsKey(id)) {
//            return "User is already exist";
//        }
//       // User user=new User(username,email,password,dob);
//        users.put(id,user);
//        if(users.containsKey(id)){
//            return " User is successfully login";
//        }
        return "Adduser have have problem";
    }

    public User register(User user) {
        if(userRepository.existsByUsername(user.getUsername())){
            throw new UserAlreadyExistsException("Username already exists");
        }
        if(userRepository.existsByEmail(user.getEmail())){
            throw new UserAlreadyExistsException("Email is already exists");
        }
        user.hashPassword();
        return userRepository.save(user);
    }

    public boolean login(String username, String password) {
        if(!userRepository.existsByUsername(username)){
            return false;
        }
        User user=userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username '" + username + "' not found"));
        if(!user.comparePassword(password)){
            return false;
        }
        return true;
    }

    public User getUserByname(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username '" + username + "' not found"));
    }

    public String updateUser(String username, String email, String password, String dob) {
        User user=userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username '" + username + "' not found"));
        if(email!=null && !email.isEmpty()){
            user.setEmail(email);
        }
        if(dob!=null && !dob.isEmpty()){
            user.setDob(dob);
        }
        if(password!=null && !password.isEmpty()){
            user.setPassword(password);
            user.hashPassword();

        }
        userRepository.save(user);
        return "user update successful";
    }

    public String updatePassword(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return "Username and password are required";
        }

        User user=userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username '" + username + "' not found"));

            user.setPassword(password);
            user.hashPassword();
        userRepository.save(user);
        return "user password update successful";

    }

}

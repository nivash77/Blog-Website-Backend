package com.example.FirstSpringBoot.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.FirstSpringBoot.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User,Long> {

    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

}

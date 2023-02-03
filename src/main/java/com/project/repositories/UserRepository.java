package com.project.repositories;

import com.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    
//    Page<User> findByNazwaContainingIgnoreCase(String userName, Pageable pageable);
//
//    List<User> findByNazwaContainingIgnoreCase(String userName);
}

package com.project.services;

import com.project.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    Page<User> getUsers(Pageable pageable);
            
    Page<User> getPaginatedUsers(Integer pageNumber, Integer pageSize);
    
    Optional<User> getUserById(Integer id);
    
    Optional<User> getUserByName(String userName);

    User insert(User user);

    void updateUser(Integer id, User user);

    void deleteUser(Integer userId);

//    Page<User> searchByNazwa(String userName, Pageable pageable);
}

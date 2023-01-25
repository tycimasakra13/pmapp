package com.project.services;

import com.project.model.Projekt;
import com.project.model.User;
import com.project.repositories.ProjectRepository;
import com.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    public UserRepository repository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.repository = userRepository;
    }

    @Override
    public Page<User> getUsers(Pageable pageable) {
        return repository.findAll(pageable);
    }
    
    @Override
    public Page<User> getPaginatedUsers(Integer pageNumber, Integer pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return repository.findAll(pageable);
    }
    
    @Override
    public Optional<User> getUserById(Integer id) {
        return repository.findById(id);
    }
    
    @Override
    public Optional<User> getUserByName(String userName) {
        return repository.findByUserName(userName);
    }
    
    @Override
    public User insert(User user) {
        return repository.save(user);
    }

    @Override
    @Transactional
    public void updateUser(Integer id, User user) {
        User userFromDb = repository.findById(id).get();

        userFromDb.setUserName(user.getUserName());
        userFromDb.setPassword(user.getPassword());

        repository.save(userFromDb);
    }

    @Override
    @Transactional
    public void deleteUser(Integer userId) {
        repository.deleteById(userId);
    }

//    @Override
//    public Page<User> searchByNazwa(String userName, Pageable pageable) {
//        return repository.findByNazwaContainingIgnoreCase(userName, pageable);
//    }
}

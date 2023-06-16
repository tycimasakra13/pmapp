package com.project.services;

import com.project.model.Book;
import com.project.repositories.BookRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    private BookRepository bookRepository;
    
    @Override
    public Optional<Book> getBookById(Integer id) {
        return bookRepository.findById(id);
    }
    
}

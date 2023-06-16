package com.project.services;

import com.project.model.Book;
import java.util.Optional;

public interface BookService {
    Optional<Book> getBookById(Integer id);
}

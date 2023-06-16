package com.project.controllers;

import com.project.model.Author;
import com.project.model.Book;
import com.project.repositories.BookRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class BookController {
    private BookRepository bookRepository;
    
    @QueryMapping
    public Book bookById2(@Argument Integer id) {
        return bookRepository.getById(id);
    }

    @SchemaMapping
    public Author author(Book book) {
        return Author.getById(book.getAuthorId());
    }
    
    @GetMapping("/book")
    public String showBook() {
        return "book";
    }
    
    
}
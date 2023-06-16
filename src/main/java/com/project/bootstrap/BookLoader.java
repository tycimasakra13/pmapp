package com.project.bootstrap;

import com.project.model.Book;
import com.project.repositories.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookLoader implements CommandLineRunner {
    public final BookRepository bookRepository;

    public BookLoader(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadBooks();
    }

    private void loadBooks() {
        if (bookRepository.count() == 0) {
            int count = 10;
            for(int x = 1; x <= count; x++) {
                insertNewBook(x);
            }
        }
    }
    
    private void insertNewBook(Integer x) {
        Book book = new Book();
        book.setId(x);
        book.setName("ABC " + x);
        book.setPageCount(x * 15);
        book.setAuthorId("QWE " + x);
        bookRepository.save(book);
        System.out.println("Sample Books Loaded " + x);   
    }
}

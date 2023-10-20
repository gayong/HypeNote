package com.example.securitystudy.book.service;

import com.example.securitystudy.book.dto.CreateBookDto;
import com.example.securitystudy.book.entity.Book;
import com.example.securitystudy.book.repository.BookRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Mono<Book> save(CreateBookDto newBookDto) {
        Book newBook = new Book();
        newBook.setTitle(newBookDto.getTitle());
        newBook.setDate(newBookDto.getDate());
        newBook.setTime(newBookDto.getTime());

        return bookRepository.save(newBook);
    }

    public Flux<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Mono<Book> getBookById(String id) {
        return bookRepository.findById(id);
    }

    public Mono<Book> updateBook(String id, CreateBookDto updatedBookDto){
        return bookRepository.findById(id)
                .flatMap(existingBook -> {
                    existingBook.setTitle(updatedBookDto.getTitle());
                    existingBook.setDate(updatedBookDto.getDate());
                    existingBook.setTime(updatedBookDto.getTime());

                    return bookRepository.save(existingBook);
                });
    }

    public Mono<Void> deleteBook(String id){
        return bookRepository.deleteById(id);
    }
}
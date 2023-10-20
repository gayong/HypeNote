package com.example.securitystudy.book.repository;

import com.example.securitystudy.book.entity.Book;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BookRepository extends ReactiveMongoRepository<Book,String> {
    Flux<Book> findAll();
    Mono<Book> findById(String id);
}
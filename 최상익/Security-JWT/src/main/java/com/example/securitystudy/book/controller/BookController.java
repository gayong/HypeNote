package com.example.securitystudy.book.controller;

import com.example.securitystudy.book.dto.CreateBookDto;
import com.example.securitystudy.book.entity.Book;
import com.example.securitystudy.book.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Tag(name = "예약", description = "예약")
    @PostMapping("/create")
    @Operation(summary = "새 예약 생성")
    public Mono<Book> create(@RequestBody CreateBookDto createBookDto) {
        return bookService.save(createBookDto);
    }

    @Tag(name = "예약", description = "예약")
    @GetMapping("")
    @Operation(summary = "모든 예약 조회")
    public Flux<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @Tag(name = "예약", description = "예약")
    @GetMapping("/{id}")
    @Operation(summary = "단일 예약 조회")
    public Mono<Book> getSingleBooking(@PathVariable String id){
        return bookService.getBookById(id);
    }

    @Tag(name = "예약", description = "예약")
    @PutMapping("/{id}")
    @Operation(summary = "예약 수정")
    public Mono<Book> updateBooKing(@PathVariable String id,@RequestBody CreateBookDto updateBookDto){
        return this.bookService.updateBook(id,updateBookDto);
    }

    @Tag(name = "예약", description = "예약")
    @DeleteMapping("/{id}")
    @Operation(summary = "예약 삭제")
    public Mono<Void> deleteBooking(@PathVariable String id){
        return this.bookService.deleteBook(id);
    }
}
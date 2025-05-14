package com.library.LibraryManagement.controller;

import com.library.LibraryManagement.model.Book;
import com.library.LibraryManagement.repository.BookRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Book Management", description = "Operations related to books")
public class BookController {
    
    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Operation(
            summary = "Create new book",
            description = "Adds a new book to the library system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @PostMapping
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        Book savedBook = bookRepository.save(book);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book bookDetails) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(bookDetails.getTitle());
                    book.setAuthor(bookDetails.getAuthor());
                    book.setIsbn(bookDetails.getIsbn());
                    book.setPublishedDate(bookDetails.getPublishedDate());
                    return ResponseEntity.ok(bookRepository.save(book));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    bookRepository.delete(book);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(
            @RequestParam(required = false) String query) {

        if (query == null || query.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        List<Book> books = bookRepository
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query);

        return ResponseEntity.ok(books);
    }
}


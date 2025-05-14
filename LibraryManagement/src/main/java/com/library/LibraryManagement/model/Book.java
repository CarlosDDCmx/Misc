package com.library.LibraryManagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;


@Entity
@Schema(description = "Book entity representing a physical book in the library")
public class Book {
    @Schema(
            description = "Unique identifier of the book",
            example = "12",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(
            description = "Title of the book",
            example = "Cien años de soledad",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Title is mandatory")
    private String title;

    @Schema(
            description = "Author of the book",
            example = "Inés de la Cruz",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Author is mandatory")
    private String author;

    @Schema(
            description = "ISBN of the book",
            example = "12345678901234",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Column(unique = true)
    @NotBlank(message = "ISBN is mandatory")
    private String isbn;

    @Schema(
            description = "Publication date in yyyy-MM-dd format",
            example = "2023-09-20",
            type = "string",
            format = "date"
    )
    @NotNull(message = "Publication date is mandatory")
    private LocalDate publishedDate;

    // Getters and Setters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public LocalDate getPublishedDate() { return publishedDate; }
    public void setPublishedDate(LocalDate publishedDate) { this.publishedDate = publishedDate; }

}
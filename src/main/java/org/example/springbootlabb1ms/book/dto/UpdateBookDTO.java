package org.example.springbootlabb1ms.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class UpdateBookDTO {

    @NotBlank(message = "title is required")
    @Size(max = 200, message = "title must be 200 characters or less")
    private String title;

    @NotBlank(message = "author is required")
    @Size(max = 120, message = "author must be 120 characters or less")
    private String author;

    @NotBlank(message = "description is required")
    @Size(max = 2000, message = "description must be 2000 characters or less")
    private String description;

    @NotBlank(message = "publisher is required")
    @Size(max = 120, message = "publisher must be 120 characters or less")
    private String publisher;

    @PastOrPresent(message = "publication date cannot be in the future")
    private LocalDate publicationDate;

    @NotBlank(message = "isbn is required")
    @Size(max = 20, message = "isbn must be 20 characters or less")
    private String isbn;

    public UpdateBookDTO() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}

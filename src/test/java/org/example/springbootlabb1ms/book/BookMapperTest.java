package org.example.springbootlabb1ms.book;

import org.example.springbootlabb1ms.book.dto.BookDTO;
import org.example.springbootlabb1ms.book.dto.CreateBookDTO;
import org.example.springbootlabb1ms.book.dto.UpdateBookDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class BookMapperTest {

    private BookMapper bookMapper;

    @BeforeEach
    public void setUp() {
        bookMapper = new BookMapper();
    }

    @Test
    @DisplayName("toEntity-method should map CreateBookDTO to Book")
    void toEntity_shouldMapCreateBookDTOToBook() {
        CreateBookDTO dto = new CreateBookDTO();
        dto.setTitle("Clean Code");
        dto.setAuthor("Robert C. Martin");
        dto.setDescription("A book about writing cleaner code");
        dto.setPublisher("Pearson");
        dto.setPublicationDate(LocalDate.of(2008,8,21));
        dto.setIsbn("9780132350884");

        Book book  = bookMapper.toEntity(dto);

        assertThat(book.getId()).isNull();
        assertThat(book.getTitle()).isEqualTo("Clean Code");
        assertThat(book.getAuthor()).isEqualTo("Robert C. Martin");
        assertThat(book.getDescription()).isEqualTo("A book about writing cleaner code");
        assertThat(book.getPublisher()).isEqualTo("Pearson");
        assertThat(book.getPublicationDate()).isEqualTo(LocalDate.of(2008,8,21));
        assertThat(book.getIsbn()).isEqualTo("9780132350884");
    }

    @Test
    @DisplayName("updateEntity-method should update existing book from UpdateBookDTO")
    void updateEntity_shouldUpdateExistingBookFromUpdateBookDTO() {
        Book existingBook = new Book();

        existingBook.setId(1L);
        existingBook.setTitle("Old title");
        existingBook.setAuthor("Old author");
        existingBook.setDescription("Old description");
        existingBook.setPublisher("Old publisher");
        existingBook.setPublicationDate(LocalDate.of(2010,10,10));
        existingBook.setIsbn("old isbn");

        UpdateBookDTO dto = new UpdateBookDTO();

        dto.setTitle("New title");
        dto.setAuthor("New author");
        dto.setDescription("New description");
        dto.setPublisher("New publisher");
        dto.setPublicationDate(LocalDate.of(2011,11,11));
        dto.setIsbn("new isbn");

        bookMapper.updateEntity(dto, existingBook);

        assertThat(existingBook.getId().longValue()).isEqualTo(1L);
        assertThat(existingBook.getTitle()).isEqualTo("New title");
        assertThat(existingBook.getAuthor()).isEqualTo("New author");
        assertThat(existingBook.getDescription()).isEqualTo("New description");
        assertThat(existingBook.getPublisher()).isEqualTo("New publisher");
        assertThat(existingBook.getPublicationDate()).isEqualTo(LocalDate.of(2011,11,11));
        assertThat(existingBook.getIsbn()).isEqualTo("new isbn");
    }

    @Test
    @DisplayName("toDTO-method should map Book to BookDTO")
    void toDTO_shouldMapBookToBookDTO() {
        Book book = new Book();
        book.setId(2L);
        book.setTitle("Some title");
        book.setAuthor("Some author");
        book.setDescription("Some description");
        book.setPublisher("Some publisher");
        book.setPublicationDate(LocalDate.of(1999,9,19));
        book.setIsbn("9781234567890");

        BookDTO dto = bookMapper.toDto(book);

        assertThat(dto.getId()).isEqualTo(2L);
        assertThat(dto.getTitle()).isEqualTo("Some title");
        assertThat(dto.getAuthor()).isEqualTo("Some author");
        assertThat(dto.getDescription()).isEqualTo("Some description");
        assertThat(dto.getPublisher()).isEqualTo("Some publisher");
        assertThat(dto.getPublicationDate()).isEqualTo(LocalDate.of(1999,9,19));
        assertThat(dto.getIsbn()).isEqualTo("9781234567890");
    }
}

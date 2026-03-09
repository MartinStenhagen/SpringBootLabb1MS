package org.example.springbootlabb1ms.book;

import org.example.springbootlabb1ms.book.dto.CreateBookDTO;
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

    

}

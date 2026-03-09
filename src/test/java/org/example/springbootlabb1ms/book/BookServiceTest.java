package org.example.springbootlabb1ms.book;

import org.example.springbootlabb1ms.book.dto.BookDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    private BookService bookService;
    private BookMapper bookMapper;

    @BeforeEach
    void setUp() {
        bookMapper = new BookMapper();
        bookService = new BookService(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("findAll-method should return all books as DTO:s")
    void findAll_shouldReturnAllBooksAsDTOs()
    {
        Book book1 = new Book();
        Book book2 = new Book();

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        List<BookDTO> result = bookService.findAll();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getTitle()).isEqualTo(book1.getTitle());
        assertThat(result.get(1).getTitle()).isEqualTo(book2.getTitle());
    }

}

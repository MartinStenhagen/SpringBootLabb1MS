package org.example.springbootlabb1ms.book;

import org.example.springbootlabb1ms.ResourceNotFoundException;
import org.example.springbootlabb1ms.book.dto.BookDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @Test
    @DisplayName("findById-method should return BookDTO when book exists")
    void findById_shouldReturnBookDTOWhenBookExists()
    {
        Book book = new Book();

        book.setId(3L);
        book.setTitle("Another title");
        book.setAuthor("Another author");
        book.setDescription("Another description");
        book.setPublisher("Another publisher");
        book.setPublicationDate(LocalDate.of(1988,8,8));

        when(bookRepository.findById(3L)).thenReturn(Optional.of(book));

        BookDTO result = bookService.findById(3L);

        assertThat(result.getId()).isEqualTo(3L);
    }

    @Test
    @DisplayName("findById-method should throw ResourceDoesNotExistException when book does not exist")
    void findById_shouldThrowExceptionWhenBookDoesNotExist()
    {
        when(bookRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(()-> bookService.findById(100L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("book with id 100 not found");


    }

}

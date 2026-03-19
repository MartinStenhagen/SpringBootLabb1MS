package org.example.springbootlabb1ms.book;

import org.example.springbootlabb1ms.exception.DuplicateIsbnException;
import org.example.springbootlabb1ms.exception.ResourceNotFoundException;
import org.example.springbootlabb1ms.book.dto.BookDTO;
import org.example.springbootlabb1ms.book.dto.CreateBookDTO;
import org.example.springbootlabb1ms.book.dto.UpdateBookDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.tuple;

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
        book1.setId(1L);
        book1.setTitle("clean code");
        book1.setAuthor("robert c. martin");
        book1.setDescription("desc 1");
        book1.setPublisher("pearson");
        book1.setPublicationDate(LocalDate.of(2008, 8, 21));
        book1.setIsbn("isbn-1");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("effective java");
        book2.setAuthor("joshua bloch");
        book2.setDescription("desc 2");
        book2.setPublisher("addison-wesley");
        book2.setPublicationDate(LocalDate.of(2018, 1, 6));
        book2.setIsbn("isbn-2");

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        List<BookDTO> result = bookService.findAll();

        assertThat(result)
                .hasSize(2)
                .extracting(BookDTO::getTitle)
                .containsExactly("clean code", "effective java");
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
        assertThat(result.getTitle()).isEqualTo("Another title");
        assertThat(result.getAuthor()).isEqualTo("Another author");
        assertThat(result.getDescription()).isEqualTo("Another description");
        assertThat(result.getPublisher()).isEqualTo("Another publisher");
        assertThat(result.getPublicationDate()).isEqualTo(LocalDate.of(1988, 8, 8));
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

    @Test
    @DisplayName("create should save and return BookDTO")
    void create_shouldSaveAndReturnCreateBookDTO()
    {
        CreateBookDTO dto = new CreateBookDTO();

        dto.setTitle("Yet another title");
        dto.setAuthor("Yet another author");
        dto.setDescription("Yet another description");
        dto.setPublisher("Yet another publisher");
        dto.setPublicationDate(LocalDate.of(2000,1,1));
        dto.setIsbn("Yet another isbn");

        Book savedBook = new Book();
        savedBook.setId(4L);
        savedBook.setTitle("Yet another title");
        savedBook.setAuthor("Yet another author");
        savedBook.setDescription("Yet another description");
        savedBook.setPublisher("Yet another publisher");
        savedBook.setPublicationDate(LocalDate.of(2000,1,1));
        savedBook.setIsbn("Yet another isbn");

        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        BookDTO result = bookService.create(dto);

        assertThat(result.getId()).isEqualTo(savedBook.getId());
        assertThat(result.getTitle()).isEqualTo(dto.getTitle());
        assertThat(result.getAuthor()).isEqualTo(dto.getAuthor());
        assertThat(result.getDescription()).isEqualTo(dto.getDescription());
        assertThat(result.getPublisher()).isEqualTo(dto.getPublisher());
        assertThat(result.getPublicationDate()).isEqualTo(dto.getPublicationDate());
        assertThat(result.getIsbn()).isEqualTo(dto.getIsbn());

        verify(bookRepository).save(any(Book.class));
    }

    @Test
    @DisplayName("update should update an existing book and return BookDTO")
    void update_shouldUpdateAndReturnUpdateBookDTO()
    {
        Book existingBook = new Book();

        existingBook.setId(1L);
        existingBook.setTitle("Old title");
        existingBook.setAuthor("Old author");
        existingBook.setDescription("Old description");
        existingBook.setPublisher("Old publisher");
        existingBook.setPublicationDate(LocalDate.of(2000, 1, 1));
        existingBook.setIsbn("old-isbn");

        UpdateBookDTO dto = new UpdateBookDTO();
        dto.setTitle("New title");
        dto.setAuthor("New author");
        dto.setDescription("New description");
        dto.setPublisher("New publisher");
        dto.setPublicationDate(LocalDate.of(2022, 6, 15));
        dto.setIsbn("new-isbn");

        Book updatedBook = new Book();
        updatedBook.setId(1L);
        updatedBook.setAuthor("New author");
        updatedBook.setTitle("New title");
        updatedBook.setDescription("New description");
        updatedBook.setPublisher("New publisher");
        updatedBook.setPublicationDate (LocalDate.of(2022, 6, 15));
        updatedBook.setIsbn("new-isbn");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(updatedBook);

        BookDTO result = bookService.update(1L, dto);

        assertThat(result.getId()).isEqualTo(updatedBook.getId());
        assertThat(result.getTitle()).isEqualTo(updatedBook.getTitle());
        assertThat(result.getAuthor()).isEqualTo(updatedBook.getAuthor());
        assertThat(result.getDescription()).isEqualTo(updatedBook.getDescription());
        assertThat(result.getPublisher()).isEqualTo(updatedBook.getPublisher());
        assertThat(result.getPublicationDate()).isEqualTo(updatedBook.getPublicationDate());
        assertThat(result.getIsbn()).isEqualTo(updatedBook.getIsbn());

        verify(bookRepository).save(existingBook);
    }

    @Test
    @DisplayName("update should throw ResourceNotFoundException when a book does not exist")
    void update_shouldThrowExceptionWhenBookDoesNotExist() {
        UpdateBookDTO dto = new UpdateBookDTO();

        dto.setTitle("New title");
        dto.setAuthor("New author");
        dto.setDescription("New description");
        dto.setPublisher("New publisher");
        dto.setPublicationDate(LocalDate.of(2022, 6, 15));
        dto.setIsbn("new-isbn");

        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(()-> bookService
                .update(1L, dto))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("deleteById should delete a book that exists")
    void deleteById_shouldDeleteBookWhenBookExists()
    {
        when(bookRepository.existsById(1L)).thenReturn(true);

        bookService.deleteById(1L);

        verify(bookRepository).deleteById((1L));
    }

    @Test
    @DisplayName("deleteById should throw exception when trying to delete a book that does not exist")
    void deleteById_shouldThrowExceptionWhenBookNotExists()
    {
        when(bookRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(()-> bookService
                .deleteById(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("findByTitle should return matching books as DTOs")
    void findByTitle_shouldReturnMatchingBooks() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Clean Code");
        book.setAuthor("Robert C. Martin");
        book.setDescription("desc");
        book.setPublisher("Pearson");
        book.setPublicationDate(LocalDate.of(2008, 8, 21));
        book.setIsbn("9780132350884");

        when(bookRepository.findByTitleContainingIgnoreCase("clean")).thenReturn(List.of(book));

        List<BookDTO> result = bookService.findByTitle("clean");

        assertThat(result)
                .hasSize(1)
                .extracting(BookDTO::getTitle)
                .containsExactly("Clean Code");
    }

    @Test
    @DisplayName("findByAuthor should return matching books as DTOs")
    void findByAuthor_shouldReturnMatchingBooks() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Clean Code");
        book.setAuthor("Robert C. Martin");
        book.setDescription("desc");
        book.setPublisher("Pearson");
        book.setPublicationDate(LocalDate.of(2008, 8, 21));
        book.setIsbn("9780132350884");

        when(bookRepository.findByAuthorContainingIgnoreCase("martin")).thenReturn(List.of(book));

        List<BookDTO> result = bookService.findByAuthor("martin");

        assertThat(result)
                .hasSize(1)
                .extracting(BookDTO::getAuthor)
                .containsExactly("Robert C. Martin");
    }

    @Test
    @DisplayName("findByTitleAndAuthor should return matching books as DTOs")
    void findByTitleAndAuthor_shouldReturnMatchingBooks() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Clean Code");
        book.setAuthor("Robert C. Martin");
        book.setDescription("desc");
        book.setPublisher("Pearson");
        book.setPublicationDate(LocalDate.of(2008, 8, 21));
        book.setIsbn("9780132350884");

        when(bookRepository.findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase("clean", "martin"))
                .thenReturn(List.of(book));

        List<BookDTO> result = bookService.findByTitleAndAuthor("clean", "martin");

        assertThat(result)
                .hasSize(1)
                .extracting(BookDTO::getTitle, BookDTO::getAuthor)
                .containsExactly(tuple("Clean Code", "Robert C. Martin"));
    }

    @Test
    @DisplayName("findPaged should use title and author filter when both are provided")
    void findPaged_shouldUseTitleAndAuthorFilterWhenBothProvided() {
        Pageable pageable = PageRequest.of(0, 5);

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Clean Code");
        book.setAuthor("Robert C. Martin");

        Page<Book> page = new PageImpl<>(List.of(book), pageable, 1);

        when(bookRepository.findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase("clean", "martin", pageable))
                .thenReturn(page);

        Page<BookDTO> result = bookService.findPaged("clean", "martin", pageable);

        assertThat(result.getContent())
                .hasSize(1)
                .extracting(BookDTO::getTitle)
                .containsExactly("Clean Code");
    }

    @Test
    @DisplayName("create should throw DuplicateIsbnException when isbn already exists")
    void create_shouldThrowDuplicateIsbnExceptionWhenIsbnAlreadyExists() {
        CreateBookDTO dto = new CreateBookDTO();
        dto.setTitle("Clean Code");
        dto.setAuthor("Robert C. Martin");
        dto.setDescription("desc");
        dto.setPublisher("Pearson");
        dto.setPublicationDate(LocalDate.of(2008, 8, 21));
        dto.setIsbn("9780132350884");

        when(bookRepository.existsByIsbn("9780132350884")).thenReturn(true);

        assertThatThrownBy(() -> bookService.create(dto))
                .isInstanceOf(DuplicateIsbnException.class)
                .hasMessageContaining("isbn " + dto.getIsbn() + " already exists");
    }

    @Test
    @DisplayName("update should throw DuplicateIsbnException when isbn belongs to another book")
    void update_shouldThrowDuplicateIsbnExceptionWhenIsbnBelongsToAnotherBook() {
        UpdateBookDTO dto = new UpdateBookDTO();
        dto.setTitle("Clean Code");
        dto.setAuthor("Robert C. Martin");
        dto.setDescription("desc");
        dto.setPublisher("Pearson");
        dto.setPublicationDate(LocalDate.of(2008, 8, 21));
        dto.setIsbn("9780132350884");

        Book existingBook = new Book();
        existingBook.setId(1L);
        existingBook.setTitle("Old");
        existingBook.setAuthor("Old");
        existingBook.setDescription("Old");
        existingBook.setPublisher("Old");
        existingBook.setPublicationDate(LocalDate.of(2000, 1, 1));
        existingBook.setIsbn("1111111111");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.existsByIsbnAndIdNot("9780132350884", 1L)).thenReturn(true);

        assertThatThrownBy(() -> bookService.update(1L, dto))
                .isInstanceOf(DuplicateIsbnException.class)
                .hasMessageContaining("isbn " + dto.getIsbn() + " already exists");
    }

    @Test
    @DisplayName("findPaged should return all books when no filters are provided")
    void findPaged_shouldReturnAllBooksWhenNoFiltersAreProvided() {
        Pageable pageable = PageRequest.of(0, 5);

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Clean Code");
        book.setAuthor("Robert C. Martin");

        Page<Book> page = new PageImpl<>(List.of(book), pageable, 1);

        when(bookRepository.findAll(pageable)).thenReturn(page);

        Page<BookDTO> result = bookService.findPaged(null, null, pageable);

        assertThat(result.getContent())
                .hasSize(1)
                .extracting(BookDTO::getTitle)
                .containsExactly("Clean Code");
    }

    @Test
    @DisplayName("findPaged should use title filter when only title is provided")
    void findPaged_shouldUseTitleFilterWhenOnlyTitleIsProvided() {
        Pageable pageable = PageRequest.of(0, 5);

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Clean Code");
        book.setAuthor("Robert C. Martin");

        Page<Book> page = new PageImpl<>(List.of(book), pageable, 1);

        when(bookRepository.findByTitleContainingIgnoreCase("clean", pageable)).thenReturn(page);

        Page<BookDTO> result = bookService.findPaged("clean", null, pageable);

        assertThat(result.getContent())
                .hasSize(1)
                .extracting(BookDTO::getTitle)
                .containsExactly("Clean Code");
    }

    @Test
    @DisplayName("findPaged should use author filter when only author is provided")
    void findPaged_shouldUseAuthorFilterWhenOnlyAuthorIsProvided() {
        Pageable pageable = PageRequest.of(0, 5);

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Clean Code");
        book.setAuthor("Robert C. Martin");

        Page<Book> page = new PageImpl<>(List.of(book), pageable, 1);

        when(bookRepository.findByAuthorContainingIgnoreCase("martin", pageable)).thenReturn(page);

        Page<BookDTO> result = bookService.findPaged(null, "martin", pageable);

        assertThat(result.getContent())
                .hasSize(1)
                .extracting(BookDTO::getAuthor)
                .containsExactly("Robert C. Martin");
    }

}

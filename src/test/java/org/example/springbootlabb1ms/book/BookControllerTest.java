package org.example.springbootlabb1ms.book;

import org.example.springbootlabb1ms.book.dto.BookDTO;
import org.example.springbootlabb1ms.exception.GlobalExceptionHandler;
import org.example.springbootlabb1ms.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@Import(GlobalExceptionHandler.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Test
    @DisplayName("GET /books should return list view with paged books")
    void listBooks_shouldReturnListView() throws Exception {
        BookDTO book = new BookDTO();
        book.setId(1L);
        book.setTitle("Clean Code");
        book.setAuthor("Robert C. Martin");
        book.setDescription("A book about clean code");
        book.setPublisher("Pearson");
        book.setPublicationDate(LocalDate.of(2008, 8, 21));
        book.setIsbn("9780132350884");

        Page<BookDTO> page = new org.springframework.data.domain.PageImpl<>(List.of(book));

        when(bookService.findPaged(org.mockito.ArgumentMatchers.isNull(),
                org.mockito.ArgumentMatchers.isNull(),
                org.mockito.ArgumentMatchers.any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/list"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attributeExists("bookPage"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("size"));
    }

    @Test
    @DisplayName("GET /books/new should return create view")
    void showCreateForm_shouldReturnCreateView() throws Exception {
        mockMvc.perform(get("/books/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/create"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("errors"));
    }

    @Test
    @DisplayName("POST /books with valid data should redirect to /books")
    void createBook_withValidData_shouldRedirect() throws Exception {
        mockMvc.perform(post("/books")
                        .param("title", "Clean Code")
                        .param("author", "Robert C. Martin")
                        .param("description", "A book about clean code")
                        .param("publisher", "Pearson")
                        .param("publicationDate", "2008-08-21")
                        .param("isbn", "9780132350884"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
    }

    @Test
    @DisplayName("POST /books with invalid data should return create view")
    void createBook_withInvalidData_shouldReturnCreateView() throws Exception {
        mockMvc.perform(post("/books")
                        .param("title", "")
                        .param("author", "Robert C. Martin")
                        .param("description", "A book about clean code")
                        .param("publisher", "Pearson")
                        .param("publicationDate", "2008-08-21")
                        .param("isbn", "invalidisbn"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/create"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("errors"));
    }

    @Test
    @DisplayName("GET /books/{id}/edit should return edit view")
    void showEditForm_shouldReturnEditView() throws Exception {
        BookDTO book = new BookDTO();
        book.setId(1L);
        book.setTitle("Clean Code");
        book.setAuthor("Robert C. Martin");
        book.setDescription("A book about clean code");
        book.setPublisher("Pearson");
        book.setPublicationDate(LocalDate.of(2008, 8, 21));
        book.setIsbn("9780132350884");

        when(bookService.findById(1L)).thenReturn(book);

        mockMvc.perform(get("/books/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/edit"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("bookId"))
                .andExpect(model().attributeExists("errors"));
    }

    @Test
    @DisplayName("POST /books/{id} with invalid data should return edit view")
    void updateBook_withInvalidData_shouldReturnEditView() throws Exception {
        mockMvc.perform(post("/books/1")
                        .param("title", "")
                        .param("author", "Robert C. Martin")
                        .param("description", "A book about clean code")
                        .param("publisher", "Pearson")
                        .param("publicationDate", "2008-08-21")
                        .param("isbn", "invalidisbn"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/edit"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("bookId"))
                .andExpect(model().attributeExists("errors"));
    }

    @Test
    @DisplayName("POST /books/{id}/delete should redirect to /books")
    void deleteBook_shouldRedirect() throws Exception {
        doNothing().when(bookService).deleteById(1L);

        mockMvc.perform(post("/books/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
    }

    @Test
    @DisplayName("GET /books/{id}/edit when book is missing should return not-found view")
    void showEditForm_whenBookMissing_shouldReturnNotFoundView() throws Exception {
        when(bookService.findById(999L))
                .thenThrow(new ResourceNotFoundException("book with id 999 not found"));

        mockMvc.perform(get("/books/999/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/not-found"))
                .andExpect(model().attributeExists("message"));
    }

    @Test
    @DisplayName("POST /books with duplicate isbn should return create view with errors")
    void createBook_withDuplicateIsbn_shouldReturnCreateViewWithErrors() throws Exception {
        org.mockito.Mockito.doThrow(new org.example.springbootlabb1ms.exception.DuplicateIsbnException("isbn 9780132350884 already exists"))
                .when(bookService).create(org.mockito.ArgumentMatchers.any(org.example.springbootlabb1ms.book.dto.CreateBookDTO.class));

        mockMvc.perform(post("/books")
                        .param("title", "Clean Code")
                        .param("author", "Robert C. Martin")
                        .param("description", "A book about clean code")
                        .param("publisher", "Pearson")
                        .param("publicationDate", "2008-08-21")
                        .param("isbn", "9780132350884"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/create"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("errors"));
    }

    @Test
    @DisplayName("GET /books with title filter should return filtered list view")
    void listBooks_withTitleFilter_shouldReturnFilteredListView() throws Exception {
        Page<BookDTO> page = new org.springframework.data.domain.PageImpl<>(List.of(new BookDTO()));

        when(bookService.findPaged(org.mockito.ArgumentMatchers.eq("clean"),
                org.mockito.ArgumentMatchers.isNull(),
                org.mockito.ArgumentMatchers.any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/books").param("title", "clean"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/list"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attributeExists("bookPage"))
                .andExpect(model().attribute("title", "clean"));
    }

    @Test
    @DisplayName("GET /books with author filter should return filtered list view")
    void listBooks_withAuthorFilter_shouldReturnFilteredListView() throws Exception {
        Page<BookDTO> page = new org.springframework.data.domain.PageImpl<>(List.of(new BookDTO()));

        when(bookService.findPaged(org.mockito.ArgumentMatchers.isNull(),
                org.mockito.ArgumentMatchers.eq("martin"),
                org.mockito.ArgumentMatchers.any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/books").param("author", "martin"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/list"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attributeExists("bookPage"))
                .andExpect(model().attribute("author", "martin"));
    }

    @Test
    @DisplayName("GET /books with title and author filter should return filtered list view")
    void listBooks_withTitleAndAuthorFilter_shouldReturnFilteredListView() throws Exception {
        Page<BookDTO> page = new org.springframework.data.domain.PageImpl<>(List.of(new BookDTO()));

        when(bookService.findPaged(org.mockito.ArgumentMatchers.eq("clean"),
                org.mockito.ArgumentMatchers.eq("martin"),
                org.mockito.ArgumentMatchers.any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/books")
                        .param("title", "clean")
                        .param("author", "martin"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/list"))
                .andExpect(model().attribute("title", "clean"))
                .andExpect(model().attribute("author", "martin"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attributeExists("bookPage"));
    }

    @Test
    @DisplayName("GET /books with page and size should return paged list view")
    void listBooks_withPagination_shouldReturnPagedListView() throws Exception {
        Page<BookDTO> page = new org.springframework.data.domain.PageImpl<>(
                List.of(new BookDTO(), new BookDTO()),
                org.springframework.data.domain.PageRequest.of(1, 5),
                12
        );

        when(bookService.findPaged(org.mockito.ArgumentMatchers.isNull(),
                org.mockito.ArgumentMatchers.isNull(),
                org.mockito.ArgumentMatchers.any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/books")
                        .param("page", "1")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/list"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attributeExists("bookPage"))
                .andExpect(model().attribute("currentPage", 1))
                .andExpect(model().attribute("size", 5));
    }

    @Test
    @DisplayName("POST /books/{id} with duplicate isbn should return edit view with errors")
    void updateBook_withDuplicateIsbn_shouldReturnEditViewWithErrors() throws Exception {
        org.mockito.Mockito.doThrow(new org.example.springbootlabb1ms.exception.DuplicateIsbnException("isbn 9780132350884 already exists"))
                .when(bookService).update(org.mockito.ArgumentMatchers.eq(1L),
                        org.mockito.ArgumentMatchers.any(org.example.springbootlabb1ms.book.dto.UpdateBookDTO.class));

        mockMvc.perform(post("/books/1")
                        .param("title", "Clean Code")
                        .param("author", "Robert C. Martin")
                        .param("description", "A book about clean code")
                        .param("publisher", "Pearson")
                        .param("publicationDate", "2008-08-21")
                        .param("isbn", "9780132350884"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/edit"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("bookId"))
                .andExpect(model().attributeExists("errors"));
    }
}

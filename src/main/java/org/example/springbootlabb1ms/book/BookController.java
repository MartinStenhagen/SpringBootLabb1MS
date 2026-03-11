package org.example.springbootlabb1ms.book;

import jakarta.validation.Valid;
import org.example.springbootlabb1ms.book.dto.BookDTO;
import org.example.springbootlabb1ms.book.dto.CreateBookDTO;
import org.example.springbootlabb1ms.book.dto.UpdateBookDTO;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String listBooks(Model model) {
        List<BookDTO> books = bookService.findAll();
        model.addAttribute("books", books);
        return "books/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new BookDTO());
        return "books/create";
    }

    @PostMapping
    public String createBook(@Valid @ModelAttribute("book") CreateBookDTO book,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "books/create";
        }

        bookService.create(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        BookDTO existingBook = bookService.findById(id);

        UpdateBookDTO form  = new UpdateBookDTO();
        form.setTitle(existingBook.getTitle());
        form.setAuthor(existingBook.getAuthor());
        form.setDescription(existingBook.getDescription());
        form.setPublisher(existingBook.getPublisher());
        form.setPublicationDate(existingBook.getPublicationDate());
        form.setIsbn(existingBook.getIsbn());

        model.addAttribute("book", form);
        model.addAttribute("bookID", id);
        return "books/edit";
    }

    @GetMapping("/{id}")
    public String updateBook(@PathVariable Long id,
                             @Valid @ModelAttribute("book") UpdateBookDTO book,
                             BindingResult bindingResult,
                             Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            return "books/edit";
        }
        bookService.update(id, book);
        return "redirect:/books";

    }

    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }
}

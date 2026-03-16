package org.example.springbootlabb1ms.book;

import jakarta.validation.Valid;
import org.example.springbootlabb1ms.book.dto.BookDTO;
import org.example.springbootlabb1ms.book.dto.CreateBookDTO;
import org.example.springbootlabb1ms.book.dto.UpdateBookDTO;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String listBooks(@RequestParam(required = false) String title,
                            @RequestParam(required = false) String author,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size,
                            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<BookDTO> bookPage = bookService.findPaged(title, author, pageable);

        model.addAttribute("bookPage", bookPage);
        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("title", title);
        model.addAttribute("author", author);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);

        return "books/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new CreateBookDTO());
        model.addAttribute("errors", List.of());
        return "books/create";
    }

    @PostMapping
    public String createBook(@Valid @ModelAttribute("book") CreateBookDTO book,
                             BindingResult bindingResult,
                             Model model) {

        if (bindingResult.hasErrors()) {

            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();

            model.addAttribute("errors", errors);

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
        model.addAttribute("bookId", id);
        model.addAttribute("errors", List.of());
        return "books/edit";
    }

    @PostMapping("/{id}")
    public String updateBook(@PathVariable Long id,
                             @Valid @ModelAttribute("book") UpdateBookDTO book,
                             BindingResult bindingResult,
                             Model model) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();

            model.addAttribute("book", book);
            model.addAttribute("bookId", id);
            model.addAttribute("errors", errors);
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

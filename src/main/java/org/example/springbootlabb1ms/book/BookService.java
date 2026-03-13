package org.example.springbootlabb1ms.book;

import org.springframework.transaction.annotation.Transactional;
import org.example.springbootlabb1ms.exception.ResourceNotFoundException;
import org.example.springbootlabb1ms.book.dto.BookDTO;
import org.example.springbootlabb1ms.book.dto.CreateBookDTO;
import org.example.springbootlabb1ms.book.dto.UpdateBookDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional(readOnly=true)
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    public List<BookDTO> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    public BookDTO findById(Long id) {
        Book book = bookRepository
                .findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("book with id " + id + " not found"));

        return bookMapper.toDto(book);
    }

    public List<BookDTO> findByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    public List<BookDTO> findByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    public List<BookDTO> findByTitleAndAuthor(String title, String author) {
        return bookRepository.findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase(title, author)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Transactional
    public BookDTO create(CreateBookDTO dto) {
        Book book = bookMapper.toEntity(dto);
        Book savedBook = bookRepository.save(book);

        return bookMapper.toDto(savedBook);
    }

    @Transactional
    public BookDTO update(Long id, UpdateBookDTO dto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("book with id " + id + " not found"));

        bookMapper.updateEntity(dto, existingBook);
        Book updatedBook = bookRepository.save(existingBook);

        return bookMapper.toDto(updatedBook);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("book with id " + id + " not found");
        }
        bookRepository.deleteById(id);
    }
}

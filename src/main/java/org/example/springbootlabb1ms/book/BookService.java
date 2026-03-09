package org.example.springbootlabb1ms.book;

import org.example.springbootlabb1ms.ResourceNotFoundException;
import org.example.springbootlabb1ms.book.dto.BookDTO;
import org.example.springbootlabb1ms.book.dto.CreateBookDTO;
import org.example.springbootlabb1ms.book.dto.UpdateBookDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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

    public BookDTO create(CreateBookDTO dto) {
        Book book = bookMapper.toEntity(dto);
        Book savedBook = bookRepository.save(book);

        return bookMapper.toDto(savedBook);
    }

    public BookDTO update(Long id, UpdateBookDTO dto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("book with id " + id + " not found"));

        bookMapper.updateEntity(dto, existingBook);
        Book updatedBook = bookRepository.save(existingBook);

        return bookMapper.toDto(updatedBook);
    }

    public void deleteById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("book with id " + id + " not found");
        }
        bookRepository.deleteById(id);
    }
}

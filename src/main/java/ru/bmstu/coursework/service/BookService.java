package ru.bmstu.coursework.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.bmstu.coursework.domain.entities.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    List<Book> findAll();

    Optional<Book> findById(Long id);

    void save(MultipartFile file, Book book);

    Resource loadAsResource(String fileUrl);
}

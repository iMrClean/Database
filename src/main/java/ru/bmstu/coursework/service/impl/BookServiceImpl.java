package ru.bmstu.coursework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.bmstu.coursework.domain.entities.Book;
import ru.bmstu.coursework.repository.BookRepository;
import ru.bmstu.coursework.service.BookService;
import ru.bmstu.coursework.service.UserService;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final UserService userService;

    private static final String UPLOADED_FOLDER = "src/main/resources/static/pdf";

    private static final String LOCALHOST = "http://localhost:8080/";

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, UserService userService) {
        this.bookRepository = bookRepository;
        this.userService = userService;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public void save(MultipartFile file, Book book) {
        String absolutePath = new File(UPLOADED_FOLDER).getAbsolutePath();
        String filename = file.getOriginalFilename();
        try {
            Files.write(Paths.get(absolutePath + "/" + filename), file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Can't upload file, folder doesn't exist");
        }
        Book saveBook = new Book();
        saveBook.setAuthor(book.getAuthor());
        saveBook.setTitle(book.getTitle());
        saveBook.setFileUrl("pdf/" + filename);
        saveBook.setUser(userService.getCurrentUser());
        bookRepository.save(saveBook);
    }

    @Override
    public Resource loadAsResource(String fileUrl) {
        try {
            Resource resource = new UrlResource(LOCALHOST + fileUrl);
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + fileUrl);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + fileUrl, e);
        }
    }

}

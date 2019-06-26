package ru.bmstu.coursework.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.bmstu.coursework.domain.entities.Book;
import ru.bmstu.coursework.service.BookService;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/home")
    public ModelAndView getAllBooks(Model model) {
        model.addAttribute("bookList", bookService.findAll());
        return new ModelAndView("books/home");
    }


    @GetMapping("/upload")
    public ModelAndView getUploadForm() {
        return new ModelAndView("books/upload", "book", new Book());
    }

    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file, @Valid Book book) {
        bookService.save(file, book);
        return "redirect:/books/home";
    }
    
    @GetMapping("/download/{id}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable Long id) {
        Book book = bookService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));
        Resource file = bookService.loadAsResource(book.getFileUrl());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}

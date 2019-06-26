package ru.bmstu.coursework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bmstu.coursework.domain.entities.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

}

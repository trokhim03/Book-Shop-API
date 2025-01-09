package mate.academy.bookshop.repository;

import mate.academy.bookshop.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}

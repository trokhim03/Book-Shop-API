package mate.academy.bookshop.repository.specification;

import mate.academy.bookshop.dto.BookSearchParameters;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(BookSearchParameters bookSearchParameters);
}

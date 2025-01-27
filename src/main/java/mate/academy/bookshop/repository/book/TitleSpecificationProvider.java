package mate.academy.bookshop.repository.book;

import java.util.Arrays;
import mate.academy.bookshop.model.Book;
import mate.academy.bookshop.repository.specification.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    public static final String TITLE_FIELD = "title";

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root.get(TITLE_FIELD)
                .in(Arrays.stream(params).toArray());
    }

    @Override
    public String getKey() {
        return TITLE_FIELD;
    }
}

package mate.academy.bookshop.repository.specification.book;

import lombok.RequiredArgsConstructor;
import mate.academy.bookshop.dto.book.BookSearchParameters;
import mate.academy.bookshop.model.Book;
import mate.academy.bookshop.repository.book.AuthorSpecificationProvider;
import mate.academy.bookshop.repository.book.TitleSpecificationProvider;
import mate.academy.bookshop.repository.specification.SpecificationBuilder;
import mate.academy.bookshop.repository.specification.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters bookSearchParameters) {
        Specification<Book> specification = Specification.where(null);
        if (bookSearchParameters.author() != null && bookSearchParameters.author().length > 0) {
            specification = specification
                    .and(specificationProviderManager
                            .getSpecificationProvider(AuthorSpecificationProvider.AUTHOR_FIELD)
                            .getSpecification(bookSearchParameters.author()));
        }
        if (bookSearchParameters.title() != null && bookSearchParameters.title().length > 0) {
            specification = specification
                    .and(specificationProviderManager
                            .getSpecificationProvider(TitleSpecificationProvider.TITLE_FIELD)
                    .getSpecification(bookSearchParameters.title()));
        }

        return specification;
    }
}

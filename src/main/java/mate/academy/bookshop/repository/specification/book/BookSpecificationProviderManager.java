package mate.academy.bookshop.repository.specification.book;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookshop.exceptions.SpecificationProviderNotFoundException;
import mate.academy.bookshop.model.Book;
import mate.academy.bookshop.repository.specification.SpecificationProvider;
import mate.academy.bookshop.repository.specification.SpecificationProviderManager;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> specificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return specificationProviders.stream()
                .filter(b -> b.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new SpecificationProviderNotFoundException("Can't find correct"
                        + " specification provider for key " + key));
    }
}

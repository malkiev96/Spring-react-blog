package ru.malkiev.blog.specification;

import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import ru.malkiev.blog.entity.Image;
import ru.malkiev.blog.entity.Image_;

import java.util.Optional;
import java.util.function.Supplier;

@Data
public class ImageSpecification implements Supplier<Optional<Specification<Image>>> {

    @Override
    public Optional<Specification<Image>> get() {
        SpecificationBuilder<Image> specBuilder = new SpecificationBuilder<>();
        specBuilder.accept(emptySpecification);
        return specBuilder.build();
    }

    private Specification<Image> emptySpecification =
            (root, query, cb) -> cb.isNotEmpty(root.get(Image_.POSTS));
}

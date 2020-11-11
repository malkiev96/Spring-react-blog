package ru.malkiev.blog.util;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class SpecBuilder<T> implements Consumer<Specification<T>> {

    private final List<Specification<T>> specs = new ArrayList<>();

    @Override
    public void accept(Specification<T> spec) {
        specs.add(spec);
    }

    public Optional<Specification<T>> build() {
        if (specs.isEmpty()) return Optional.empty();
        Specification<T> spec = specs.remove(0);
        for (Specification<T> s : specs) {
            spec = spec != null ? spec.and(s) : null;
        }
        return Optional.ofNullable(spec);
    }
}

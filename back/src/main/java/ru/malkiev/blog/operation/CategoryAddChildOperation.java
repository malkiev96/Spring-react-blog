package ru.malkiev.blog.operation;

import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.entity.Category;
import ru.malkiev.blog.repository.CategoryRepository;

import java.util.List;
import java.util.function.Function;

@Component
@AllArgsConstructor
public class CategoryAddChildOperation implements Function<Pair<List<Integer>, Category>, Category> {

    private final CategoryRepository repository;

    @Override
    public Category apply(Pair<List<Integer>, Category> pair) {
        List<Category> childs = repository.findAllById(pair.getFirst());
        Category category = pair.getSecond();
        childs.forEach(ch -> ch.setParent(category));
        category.setChilds(childs);
        return category;
    }
}

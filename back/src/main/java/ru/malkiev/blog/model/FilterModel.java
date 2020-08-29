package ru.malkiev.blog.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import ru.malkiev.blog.entity.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = false)
@Data
public class FilterModel extends RepresentationModel<FilterModel> {

    private List<CategoryItem> categories;
    private List<DataItem> tags;
    private List<Role> roles;
    private List<Post.Status> statuses;
    private List<AuthProvider> providers;

    public FilterModel(List<Category> categories, List<Tag> tags) {
        this.roles = Arrays.asList(Role.values());
        this.statuses = Post.Status.all;
        this.providers = Arrays.asList(AuthProvider.values());
        this.categories = categories.stream().map(CategoryItem::new).collect(Collectors.toList());
        this.tags = tags.stream().map(DataItem::new).collect(Collectors.toList());
    }

    @Getter
    public static class CategoryItem extends DataItem{

        private final List<CategoryItem> childs;

        public CategoryItem(Category category) {
            super(category);
            this.childs = category.getChilds()
                    .stream()
                    .map(CategoryItem::new)
                    .collect(Collectors.toList());
        }
    }
}

package ru.malkiev.springsocial.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import ru.malkiev.springsocial.entity.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = false)
@Data
public class FilterModel extends RepresentationModel<FilterModel> {

    private List<CategoryItem> categories = new ArrayList<>();
    private List<TagItem> tags = new ArrayList<>();
    private List<Role> roles = Arrays.asList(Role.values());
    private List<Post.Status> statuses = Post.Status.all;
    private List<AuthProvider> providers = Arrays.asList(AuthProvider.values());

    public FilterModel(List<Category> categories, List<Tag> tags) {
        this.categories = categories.stream().map(CategoryItem::new).collect(Collectors.toList());
        this.tags = tags.stream().map(TagItem::new).collect(Collectors.toList());
    }

    @Getter
    public static class CategoryItem {
        private final int id;
        private final String name;
        private final String description;
        private final List<CategoryItem> childs;

        public CategoryItem(Category category) {
            this.id = category.getId();
            this.name = category.getName();
            this.description = category.getDescription();
            this.childs = category.getChilds().stream().map(CategoryItem::new).collect(Collectors.toList());
        }
    }

    @Getter
    public static class TagItem {
        private final int id;
        private final String name;
        private final String description;

        public TagItem(Tag tag) {
            this.id = tag.getId();
            this.name = tag.getName();
            this.description = tag.getDescription();
        }
    }

}

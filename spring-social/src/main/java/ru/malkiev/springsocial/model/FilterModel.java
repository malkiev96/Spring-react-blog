package ru.malkiev.springsocial.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import ru.malkiev.springsocial.entity.AuthProvider;
import ru.malkiev.springsocial.entity.Category;
import ru.malkiev.springsocial.entity.Role;
import ru.malkiev.springsocial.entity.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class FilterModel extends RepresentationModel<FilterModel> {

    private List<CategoryItem> categories = new ArrayList<>();
    private List<TagItem> tags = new ArrayList<>();
    private List<Role> roles = Arrays.asList(Role.values());
    private List<AuthProvider> providers = Arrays.asList(AuthProvider.values());

    public void addCategories(Category category){
        this.categories.add(new CategoryItem(category));
    }

    public void addTags(Tag tag){
        this.tags.add(new TagItem(tag));
    }

    @Getter
    public static class CategoryItem {
        private final int id;
        private final String name;
        private final String description;
        private final List<CategoryItem> childs;

        public CategoryItem(Category category){
            this.id = category.getId();
            this.name = category.getName();
            this.description = category.getDescription();
            this.childs = toItemList(category.getChilds());
        }

        private List<CategoryItem> toItemList(List<Category> categories){
            List<CategoryItem> items = new ArrayList<>();
            categories.forEach(category -> items.add(new CategoryItem(category)));
            return items;
        }
    }

    @Getter
    public static class TagItem {
        private final int id;
        private final String name;
        private final String description;

        public TagItem(Tag tag){
            this.id = tag.getId();
            this.name = tag.getName();
            this.description = tag.getDescription();
        }
    }

}

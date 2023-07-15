package ru.malkiev.blog.api.mapper;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import ru.malkiev.blog.api.model.PostDetailModel;
import ru.malkiev.blog.api.model.PostModel;
import ru.malkiev.blog.domain.entity.Post;

@Mapper(componentModel = "spring", uses = {
    DateMapper.class,
    DocumentMapper.class,
    TagMapper.class,
    CategoryMapper.class
})
public interface PostMapper {

  PostModel toModel(Post post);

  PostDetailModel toDetailModel(Post post);

  default Page<PostModel> toPagedModel(Page<Post> posts) {
    return posts.map(this::toModel);
  }

}

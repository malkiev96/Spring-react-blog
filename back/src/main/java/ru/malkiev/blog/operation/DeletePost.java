package ru.malkiev.blog.operation;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.assembler.PostAssembler;
import ru.malkiev.blog.entity.Post;
import ru.malkiev.blog.entity.User;
import ru.malkiev.blog.service.UserService;

import java.util.function.UnaryOperator;

import static ru.malkiev.blog.entity.PostStatus.DELETED;

@Component
@AllArgsConstructor
public class DeletePost implements UnaryOperator<Post> {

    private final UserService userService;

    @Override
    public Post apply(@NonNull Post post) {
        User user = userService.getCurrentUserOrError();
        if (PostAssembler.canDeletePost(post, user)) {
            post.setStatus(DELETED);
            return post;
        } else throw new IllegalArgumentException("Can't delete post by status : " + post.getStatus());
    }
}

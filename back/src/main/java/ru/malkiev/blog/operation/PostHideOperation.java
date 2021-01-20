package ru.malkiev.blog.operation;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.assembler.PostAssembler;
import ru.malkiev.blog.entity.Post;
import ru.malkiev.blog.entity.User;
import ru.malkiev.blog.service.UserService;

import java.util.function.UnaryOperator;

import static ru.malkiev.blog.entity.PostStatus.CREATED;

@Component
@AllArgsConstructor
public class PostHideOperation implements UnaryOperator<Post> {

    private final UserService userService;

    @Override
    public Post apply(@NonNull Post post) {
        User user = userService.getCurrentUserOrError();
        if (PostAssembler.canHidePost(post, user)) {
            post.setStatus(CREATED);
            return post;
        } else throw new IllegalArgumentException("Can't hide post by status " + post.getStatus());
    }
}

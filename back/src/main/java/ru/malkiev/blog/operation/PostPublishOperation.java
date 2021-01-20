package ru.malkiev.blog.operation;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.assembler.PostAssembler;
import ru.malkiev.blog.entity.Post;
import ru.malkiev.blog.entity.User;
import ru.malkiev.blog.service.UserService;

import java.util.function.UnaryOperator;

import static ru.malkiev.blog.entity.PostStatus.PUBLISHED;

@Component
@AllArgsConstructor
public class PostPublishOperation implements UnaryOperator<Post> {

    private final UserService userService;

    @Override
    public Post apply(@NonNull Post post) {
        User user = userService.getCurrentUserOrError();
        if (PostAssembler.canPublishPost(post, user)) {
            post.setStatus(PUBLISHED);
            return post;
        } else throw new IllegalArgumentException("Can't publish post by status " + post.getStatus());
    }
}

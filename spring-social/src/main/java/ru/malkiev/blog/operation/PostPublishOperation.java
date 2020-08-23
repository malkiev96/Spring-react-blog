package ru.malkiev.blog.operation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.assembler.PostAssembler;
import ru.malkiev.blog.entity.Post;
import ru.malkiev.blog.entity.User;
import ru.malkiev.blog.exception.UserNotFoundException;
import ru.malkiev.blog.repository.PostRepository;
import ru.malkiev.blog.service.UserService;

import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;

import static ru.malkiev.blog.entity.Post.Status.PUBLISHED;

@Component
@AllArgsConstructor
public class PostPublishOperation implements UnaryOperator<Post> {

    private final PostRepository repository;
    private final UserService userService;

    @Override
    public Post apply(Post post) {
        Objects.requireNonNull(post);
        Optional<User> user = userService.getCurrentUser();
        if (user.isPresent()) {
            if (PostAssembler.canPublishPost(post, user.get())) {
                post.setStatus(PUBLISHED);
                return repository.save(post);
            } else throw new IllegalArgumentException("Can't publish post by status " + post.getStatus());
        }
        throw new UserNotFoundException();
    }
}

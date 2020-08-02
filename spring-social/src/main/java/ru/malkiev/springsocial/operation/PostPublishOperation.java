package ru.malkiev.springsocial.operation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.malkiev.springsocial.assembler.PostAssembler;
import ru.malkiev.springsocial.entity.Post;
import ru.malkiev.springsocial.entity.User;
import ru.malkiev.springsocial.exception.UserNotFoundException;
import ru.malkiev.springsocial.repository.PostRepository;
import ru.malkiev.springsocial.service.UserService;

import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;

import static ru.malkiev.springsocial.entity.Post.Status.PUBLISHED;

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
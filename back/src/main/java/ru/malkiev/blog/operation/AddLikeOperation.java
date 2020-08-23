package ru.malkiev.blog.operation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.entity.Post;
import ru.malkiev.blog.entity.PostLike;
import ru.malkiev.blog.entity.User;
import ru.malkiev.blog.exception.UserNotFoundException;
import ru.malkiev.blog.repository.PostLikeRepository;
import ru.malkiev.blog.service.UserService;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Component
@AllArgsConstructor
public class AddLikeOperation implements Function<Post, Boolean> {

    private final PostLikeRepository likeRepository;
    private final UserService userService;

    @Override
    public Boolean apply(Post post) {
        Objects.requireNonNull(post);
        Optional<User> currentUser = userService.getCurrentUser();
        if (currentUser.isPresent()) {
            Optional<PostLike> postLike = likeRepository.findByCreatedByAndPost(currentUser.get(), post);
            if (postLike.isPresent()) {
                likeRepository.delete(postLike.get());
                return false;
            } else {
                likeRepository.save(PostLike.of(post));
                return true;
            }
        } else throw new UserNotFoundException();
    }
}

package ru.malkiev.blog.operation;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.entity.Post;
import ru.malkiev.blog.entity.PostLike;
import ru.malkiev.blog.entity.User;
import ru.malkiev.blog.repository.PostLikeRepository;
import ru.malkiev.blog.service.UserService;

import java.util.function.Function;

@Component
@AllArgsConstructor
public class AddLikeOperation implements Function<Post, Boolean> {

    private final PostLikeRepository likeRepository;
    private final UserService userService;

    @Override
    public Boolean apply(@NonNull Post post) {
        User currentUser = userService.getCurrentUserOrError();
        return likeRepository.findByCreatedByAndPost(currentUser, post)
                .map(like -> {
                    likeRepository.delete(like);
                    return false;
                })
                .orElseGet(() -> {
                    likeRepository.save(PostLike.of(post));
                    return true;
                });
    }
}

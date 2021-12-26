package ru.malkiev.blog.operation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.entity.Post;
import ru.malkiev.blog.entity.PostRating;
import ru.malkiev.blog.entity.User;
import ru.malkiev.blog.repository.PostRatingRepository;
import ru.malkiev.blog.service.UserService;

import java.util.function.Function;

@Component
@AllArgsConstructor
public class DeleteStar implements Function<Post, Double> {

    private final PostRatingRepository repository;
    private final UserService userService;

    @Override
    public Double apply(Post post) {
        User currentUser = userService.getCurrentUserOrError();
        PostRating rating = repository.findByCreatedByAndPost(currentUser, post)
                .orElseThrow(() -> new IllegalArgumentException("Can't find user star"));
        repository.delete(rating);

        return repository.getAvgRatingByPost(post);
    }
}

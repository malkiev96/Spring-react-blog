package ru.malkiev.blog.operation;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.entity.Post;
import ru.malkiev.blog.entity.PostRating;
import ru.malkiev.blog.entity.User;
import ru.malkiev.blog.repository.PostRatingRepository;
import ru.malkiev.blog.service.UserService;

import java.util.Optional;
import java.util.function.Function;

@Component
@AllArgsConstructor
public class AddStarOperation implements Function<Pair<Post, Integer>, Double> {

    private final PostRatingRepository repository;
    private final UserService userService;

    @Override
    public Double apply(@NonNull Pair<Post, Integer> pair) {
        Post post = pair.getFirst();
        int star = pair.getSecond();

        User currentUser = userService.getCurrentUserOrError();
        if (star >= 1 && star <= 5) {
            Optional<PostRating> rating = repository.findByCreatedByAndPost(currentUser, post);
            if (rating.isPresent()) throw new IllegalArgumentException("Can't star post again");
            repository.save(PostRating.of(post, star));
            return repository.getAvgRatingByPost(post);
        } else throw new IllegalArgumentException("Star can't be " + star);
    }
}

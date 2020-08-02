package ru.malkiev.springsocial.operation;

import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import ru.malkiev.springsocial.entity.Post;
import ru.malkiev.springsocial.entity.PostRating;
import ru.malkiev.springsocial.entity.User;
import ru.malkiev.springsocial.exception.UserNotFoundException;
import ru.malkiev.springsocial.repository.PostRatingRepository;
import ru.malkiev.springsocial.service.UserService;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Component
@AllArgsConstructor
public class AddStarOperation implements Function<Pair<Post, Integer>, Post> {

    private final PostRatingRepository repository;
    private final UserService userService;

    @Override
    public Post apply(Pair<Post, Integer> pair) {
        Post post = pair.getFirst();
        Objects.requireNonNull(post);
        int star = pair.getSecond();

        Optional<User> currentUser = userService.getCurrentUser();
        if (star >= 1 && star <= 5) {
            if (currentUser.isPresent()) {
                Optional<PostRating> rating = repository.findByCreatedByAndPost(currentUser.get(), post);
                if (rating.isPresent()) throw new IllegalArgumentException("Can't star post again");
                repository.save(PostRating.of(post, star));
                return post;
            } else throw new UserNotFoundException();
        } else throw new IllegalArgumentException("Star can't be " + star);
    }
}

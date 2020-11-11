package ru.malkiev.blog.operation;

import lombok.NonNull;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.entity.User;
import ru.malkiev.blog.model.payload.UserEditDto;

import java.util.function.Function;

@Component
public class UserEditOperation implements Function<Pair<UserEditDto, User>, User> {

    @Override
    public User apply(@NonNull Pair<UserEditDto, User> pair) {
        UserEditDto dto = pair.getFirst();
        User user = pair.getSecond();

        user.setName(dto.getName());
        user.setAbout(dto.getAbout());
        user.setBirthDate(dto.getBirthDate());
        user.setCity(dto.getCity());
        user.setImageUrl(dto.getImageUrl());

        return user;
    }
}

package ru.malkiev.springsocial.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.malkiev.springsocial.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;


}

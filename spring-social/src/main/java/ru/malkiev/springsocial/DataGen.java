package ru.malkiev.springsocial;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.malkiev.springsocial.repository.*;

@Component
@AllArgsConstructor
public class DataGen implements CommandLineRunner {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;

    @Override
    public void run(String... args) throws Exception {
//        User user = userRepository.getOne(5);
//        Image image = imageRepository.getOne(3);
//
//        for (int i = 0; i < 23; i++) {
//            Random random = new Random();
//
//            Post post = new Post();
//            post.setTags(tagRepository.findAll());
//            if (random.nextBoolean()) post.setImagePreview(image);
//            if (random.nextBoolean()) {
//                post.setPosted(true);
//                post.setPostedDate(new Date());
//            }
//            post.setCreatedBy(user);
//            post.setImages(imageRepository.findAll());
//            post.setCategory(categoryRepository.getOne(random.nextInt(4)+1));
//            String title = random.ints(97, 122 + 1)
//                    .limit(23)
//                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
//                    .toString();
//            String text = random.ints(97, 122 + 1)
//                    .limit(500)
//                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
//                    .toString();
//            String desc = random.ints(97, 122 + 1)
//                    .limit(100)
//                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
//                    .toString();
//            post.setTitle(title);
//            post.setText(text);
//            post.setDescription(desc);
//            postRepository.save(post);
//        }
    }
}

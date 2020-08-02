package ru.malkiev.springsocial;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.malkiev.springsocial.entity.*;
import ru.malkiev.springsocial.repository.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Random;

@Component
@AllArgsConstructor
public class DataGen implements CommandLineRunner {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        loadCategories();
        loadTags();
        loadUsers();
        loadPosts();
    }

    private void loadPosts() {
        for (int i = 0; i < 23; i++) {
            Random random = new Random();
            User user = userRepository.getOne(random.nextInt(4) + 1);

            Post post = new Post();
            post.setTags(Arrays.asList(
                    tagRepository.getOne(random.nextInt(7) + 1),
                    tagRepository.getOne(random.nextInt(7) + 1))
            );
            post.setStatus(Post.Status.randomStatus());
            post.setCreatedBy(user);
            post.setImages(imageRepository.findAll());
            post.setCategory(categoryRepository.getOne(random.nextInt(8) + 1));
            String title = random.ints(97, 122 + 1)
                    .limit(23)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            String text = "Sussex result matter any end see. It speedily me addition weddings vicinity in pleasure. Happiness commanded an conveying breakfast in. Regard her say warmly elinor. Him these are visit front end for seven walls. Money eat scale now ask law learn. Side its they just any upon see last. He prepared no shutters perceive do greatest. Ye at unpleasant solicitude in companions interested. \n" +
                    "\n" +
                    "Demesne far hearted suppose venture excited see had has. Dependent on so extremely delivered by. Yet \uFEFFno jokes worse her why. Bed one supposing breakfast day fulfilled off depending questions. Whatever boy her exertion his extended. Ecstatic followed handsome drawings entirely mrs one yet outweigh. Of acceptance insipidity remarkably is invitation. \n" +
                    "\n" +
                    "Travelling alteration impression six all uncommonly. Chamber hearing inhabit joy highest private ask him our believe. Up nature valley do warmly. Entered of cordial do on no hearted. Yet agreed whence and unable limits. Use off him gay abilities concluded immediate allowance. \n" +
                    "\n" +
                    "Son agreed others exeter period myself few yet nature. Mention mr manners opinion if garrets enabled. To an occasional dissimilar impossible sentiments. Do fortune account written prepare invited no passage. Garrets use ten you the weather ferrars venture friends. Solid visit seems again you nor all. \n" +
                    "\n" +
                    "Saw yet kindness too replying whatever marianne. Old sentiments resolution admiration unaffected its mrs literature. Behaviour new set existence dashwoods. It satisfied to mr commanded consisted disposing engrossed. Tall snug do of till on easy. Form not calm new fail. \n" +
                    "\n" +
                    "Silent sir say desire fat him letter. Whatever settling goodness too and honoured she building answered her. Strongly thoughts remember mr to do consider debating. Spirits musical behaved on we he farther letters. Repulsive he he as deficient newspaper dashwoods we. Discovered her his pianoforte insipidity entreaties. Began he at terms meant as fancy. Breakfast arranging he if furniture we described on. Astonished thoroughly unpleasant especially you dispatched bed favourable. \n" +
                    "\n" +
                    "Unpleasant astonished an diminution up partiality. Noisy an their of meant. Death means up civil do an offer wound of. Called square an in afraid direct. Resolution diminution conviction so mr at unpleasing simplicity no. No it as breakfast up conveying earnestly immediate principle. Him son disposed produced humoured overcame she bachelor improved. Studied however out wishing but inhabit fortune windows. ";
            String desc = random.ints(97, 122 + 1)
                    .limit(100)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            post.setTitle(title);
            post.setText(text);
            post.setDescription(desc);
            postRepository.save(post);
        }
    }

    private void loadUsers() {
        userRepository.save(User.builder()
                .email("malkiev96@gmail.com")
                .emailVerified(true)
                .name("admin")
                .password(passwordEncoder.encode("admin"))
                .provider(AuthProvider.LOCAL)
                .role(Role.ROLE_ADMIN)
                .imageUrl("https://html5css.ru/w3css/img_avatar3.png")
                .about("About user")
                .city("Yekaterinburg")
                .birthDate(LocalDate.of(1996,11,23))
                .build());
        userRepository.save(User.builder()
                .email("test@gmail.com")
                .emailVerified(true)
                .name("test")
                .password(passwordEncoder.encode("test"))
                .provider(AuthProvider.LOCAL)
                .role(Role.ROLE_ADMIN)
                .imageUrl("https://html5css.ru/w3css/img_avatar3.png")
                .build());
        userRepository.save(User.builder()
                .email("test123@gmail.com")
                .emailVerified(true)
                .name("test123")
                .password(passwordEncoder.encode("test123"))
                .provider(AuthProvider.LOCAL)
                .role(Role.ROLE_USER)
                .imageUrl("https://html5css.ru/w3css/img_avatar3.png")
                .build());
        userRepository.save(User.builder()
                .email("test321@gmail.com")
                .emailVerified(true)
                .name("test321")
                .password(passwordEncoder.encode("test321"))
                .provider(AuthProvider.LOCAL)
                .role(Role.ROLE_USER)
                .imageUrl("https://html5css.ru/w3css/img_avatar3.png")
                .build());
    }

    private void loadTags() {
        tagRepository.save(Tag.builder().name("spring").description("Spring framework").build());
        tagRepository.save(Tag.builder().name("test").description("test framework").build());
        tagRepository.save(Tag.builder().name("tag").description("tag desc").build());
        tagRepository.save(Tag.builder().name("java").description("java tag").build());
        tagRepository.save(Tag.builder().name("maven").description("maven").build());
        tagRepository.save(Tag.builder().name("plugins").description("plugins").build());
        tagRepository.save(Tag.builder().name("demo").description("demo desc").build());
    }

    private void loadCategories() {
        Category java = Category.builder().name("Java").description("Java").build();
        java = categoryRepository.save(java);
        Category spring = Category.builder().name("Spring").description("Spring").build();
        spring = categoryRepository.save(spring);
        categoryRepository.save(Category.builder().name("Hibernate").description("Hib").parent(java).build());
        categoryRepository.save(Category.builder().name("Spring boot").description("Spring Boot").parent(java).build());
        categoryRepository.save(Category.builder().name("Oracle").description("Hib").parent(java).build());
        categoryRepository.save(Category.builder().name("Maven").description("Hib").parent(java).build());

        categoryRepository.save(Category.builder().name("Hateoas").description("Hib").parent(spring).build());
        categoryRepository.save(Category.builder().name("Mvc").description("Hib").parent(spring).build());
        categoryRepository.save(Category.builder().name("Jpa").description("Hib").parent(spring).build());
    }
}

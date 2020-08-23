package ru.malkiev.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.malkiev.blog.config.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class SpringSocialApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSocialApplication.class, args);
	}
}

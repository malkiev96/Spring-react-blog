package ru.malkiev.blog.application.operation;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;
import ru.malkiev.blog.api.dto.UserEditDto;
import ru.malkiev.blog.application.service.DocumentService;
import ru.malkiev.blog.application.service.StorePath;
import ru.malkiev.blog.domain.entity.Document;
import ru.malkiev.blog.domain.entity.User;

@Component
@RequiredArgsConstructor
public class EditUser implements Function<Pair<UserEditDto, User>, User> {

  private final DocumentService documentService;

  @Override
  public User apply(@NonNull Pair<UserEditDto, User> pair) {
    UserEditDto dto = pair.getFirst();
    User user = pair.getSecond();

    user.setName(dto.getName());
    user.setAbout(dto.getAbout());
    user.setBirthDate(dto.getBirthDate());
    user.setCity(dto.getCity());
    user.setPreview(createPreview(dto.getPreviewId()));

    return user;
  }

  private Document createPreview(@Nullable String fileId) {
    if (fileId == null) {
      return null;
    } else {
      String decoded = UriUtils.decode(fileId, StandardCharsets.UTF_8);
      StorePath storePath = StorePath.parseOrThrow(decoded);
      return documentService.createDocument(storePath);
    }
  }

}

package ru.malkiev.blog.api.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class DateMapper {

  public String asString(LocalDateTime time) {
    return time != null ? time.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) : null;
  }

}

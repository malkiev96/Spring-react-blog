package ru.malkiev.blog.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;

@Getter
@RequiredArgsConstructor
public enum DocumentType {
  XML(false, MediaType.APPLICATION_OCTET_STREAM),
  XLS(false, MediaType.APPLICATION_OCTET_STREAM),
  DOC(false, MediaType.APPLICATION_OCTET_STREAM),
  PDF(false, MediaType.APPLICATION_OCTET_STREAM),
  ZIP(false, MediaType.APPLICATION_OCTET_STREAM),
  RAR(false, MediaType.APPLICATION_OCTET_STREAM),
  GIF(true, MediaType.IMAGE_GIF),
  JPEG(true, MediaType.IMAGE_JPEG),
  PNG(true, MediaType.IMAGE_PNG),
  SVG(true, MediaType.APPLICATION_OCTET_STREAM),
  UNKNOWN(false, MediaType.APPLICATION_OCTET_STREAM);

  private final boolean image;
  private final MediaType mediaType;

  public static DocumentType of(String filename) {
    if (filename == null) {
      return UNKNOWN;
    }
    String extension = FilenameUtils.getExtension(filename);
    switch (extension.toLowerCase()) {
      case "xml":
        return XML;
      case "xls":
      case "xlsx":
        return XLS;
      case "doc":
      case "docx":
        return DOC;
      case "pdf":
        return PDF;
      case "zip":
        return ZIP;
      case "rar":
        return RAR;
      case "gif":
        return GIF;
      case "jpg":
      case "jpeg":
        return JPEG;
      case "png":
        return PNG;
      case "svg":
        return SVG;
      default:
        return UNKNOWN;
    }
  }

}

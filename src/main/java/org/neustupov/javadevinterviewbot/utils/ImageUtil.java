package org.neustupov.javadevinterviewbot.utils;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.bson.types.Binary;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.repository.QuestionRepositoryMongo;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ImageUtil {

  private QuestionRepositoryMongo repository;

  public ImageUtil(QuestionRepositoryMongo repository) {
    this.repository = repository;
  }

  public Optional<Binary> getImageData(String inputMsg) {
    return repository.findById(Long.parseLong(inputMsg.replace("/q", "")))
        .map(Question::getImage);
  }

  public File getPhotoFile(Binary imageData) {
    File imageFile = new File(UUID.randomUUID().toString());
      try {
        FileUtils.writeByteArrayToFile(imageFile, imageData.getData());
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      }
    return imageFile;
  }
}

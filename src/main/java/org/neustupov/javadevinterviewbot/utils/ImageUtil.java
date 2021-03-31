package org.neustupov.javadevinterviewbot.utils;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.bson.types.Binary;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.repository.QuestionRepository;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ImageUtil {

  private QuestionRepository repository;

  public ImageUtil(QuestionRepository repository) {
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
      log.info("File with name: " + imageFile.getName() + " is created");
    return imageFile;
  }

  public void deleteTempFile(File imageTempFile){
    FileUtils.deleteQuietly(imageTempFile);
    log.info("File with name: " + imageTempFile.getName() + " is deleted");
  }
}

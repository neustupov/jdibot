package org.neustupov.javadevinterviewbot.utils;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.bson.types.Binary;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.repository.QuestionRepository;
import org.springframework.stereotype.Component;

/**
 * Утилита для работы с изображениями
 */
@Slf4j
@Component
@FieldDefaults(makeFinal=true, level = AccessLevel.PRIVATE)
public class ImageUtil {

  /**
   * Репозиторий вопросов
   */
  QuestionRepository repository;

  public ImageUtil(QuestionRepository repository) {
    this.repository = repository;
  }

  /**
   * Извлекает данные изображения из репозитория вопросов
   *
   * @param inputMsg Сообщение
   * @return Optional<Binary>
   */
  public Optional<Binary> getImageData(String inputMsg) {
    return repository.findById(Long.parseLong(inputMsg.replace("/q", "")))
        .map(Question::getImage);
  }

  /**
   * Создает файл изображения
   *
   * @param imageData Данные изображения
   * @return Файл
   */
  public File getImageFile(Binary imageData) {
    File imageFile = new File(UUID.randomUUID().toString());
    try {
      FileUtils.writeByteArrayToFile(imageFile, imageData.getData());
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    log.info("File with name: " + imageFile.getName() + " is created");
    return imageFile;
  }

  /**
   * Удаляет файл
   *
   * @param imageTempFile Файл
   */
  public void deleteTempFile(File imageTempFile) {
    FileUtils.deleteQuietly(imageTempFile);
    log.info("File with name: " + imageTempFile.getName() + " is deleted");
  }
}

package org.neustupov.javadevinterviewbot.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.neustupov.javadevinterviewbot.TestData.getQuestionWithIdAndImage;

import java.io.File;
import java.util.Optional;
import org.apache.commons.io.FileUtils;
import org.bson.types.Binary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ImageUtilTest {

  @Autowired
  private ImageUtil imageUtil;

  @MockBean
  private QuestionRepository repository;

  private Question question;

  @BeforeEach
  void setUp() {
    question = getQuestionWithIdAndImage();
    when(repository.findById(anyLong())).thenReturn(Optional.of(question));
  }

  @Test
  void getImageData() {
    Optional<Binary> imageData = imageUtil.getImageData("/q700");
    assertTrue(imageData.isPresent());
  }

  @Test
  void getPhotoFile() throws Exception {
    byte[] bytes = new byte[]{4, 5, 6, 7, 8, 9, 0};
    Binary binary = new Binary(bytes);
    File file = imageUtil.getPhotoFile(binary);
    assertTrue(file.exists());
    assertArrayEquals(FileUtils.readFileToByteArray(file), bytes);
    FileUtils.deleteQuietly(file);
  }

  @Test
  void deleteTempFile() {
    File file = imageUtil.getPhotoFile(new Binary(new byte[]{4, 5, 6, 7, 8, 9, 0}));
    assertTrue(file.exists());

    imageUtil.deleteTempFile(file);
    assertFalse(file.exists());

    FileUtils.deleteQuietly(file);
  }
}
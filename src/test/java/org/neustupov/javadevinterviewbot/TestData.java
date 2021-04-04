package org.neustupov.javadevinterviewbot;

import java.util.Arrays;
import java.util.List;
import org.bson.types.Binary;
import org.neustupov.javadevinterviewbot.model.menu.Category;
import org.neustupov.javadevinterviewbot.model.menu.Level;
import org.neustupov.javadevinterviewbot.model.GenericBuilder;
import org.neustupov.javadevinterviewbot.model.Question;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public class TestData {

  public interface Buttons {
    String SPRING_PART_1 = "Spring part 1";
    String SPRING_PART_2 = "Spring part 2";
    String SPRING_PART_3 = "Spring part 3";

    String OOP = "ООП";
    String COLLECTIONS = "Коллекции";
    String PATTERNS = "Паттерны";
    String SPRING = "Spring";

    String JUNIOR = "Junior";
    String MIDDLE = "Middle";
    String SENIOR = "Senior";

    String QUESTIONS = "Вопросы";
    String SEARCH = "Поиск";
    String TESTS = "Тестирование";

    String QUESTIONS_BUTTON = "buttonQuestions";
    String SEARCH_BUTTON = "buttonSearch";
    String TESTS_BUTTON = "buttonTest";

    String CATEGORY_BUTTON_CALLBACK = "buttonCategoryCallback";
    String SPRING_BUTTON_CALLBACK = "buttonSpringCallback";

    String OOP_CATEGORY_BUTTON = "buttonOOP";
    String COLLECTIONS_CATEGORY_BUTTON = "buttonCollections";
    String PATTERNS_CATEGORY_BUTTON = "buttonPatterns";
    String SPRING_BUTTON = "buttonSpring";

    String JUNIOR_LEVEL_BUTTON = "buttonJunior";
    String MIDDLE_LEVEL_BUTTON = "buttonMiddle";
    String SENIOR_LEVEL_BUTTON = "buttonSenior";

    String JUNIOR_LEVEL_BUTTON_CALLBACK = "buttonJuniorCallback";
    String MIDDLE_LEVEL_BUTTON_CALLBACK = "buttonMiddleCallback";
    String SENIOR_LEVEL_BUTTON_CALLBACK = "buttonSeniorCallback";
  }

  public static Update getUpdate() {
    return GenericBuilder.of(Update::new)
        .with(Update::setUpdateId, 123)
        .build();
  }

  public static CallbackQuery getCallbackQuery() {
    return GenericBuilder.of(CallbackQuery::new)
        .with(CallbackQuery::setId, "100500")
        .with(CallbackQuery::setData, "buttonJunior")
        .with(CallbackQuery::setFrom, getUser())
        .build();
  }

  public static Message getMessage() {
    Chat chat = new Chat();
    chat.setId(869489319L);
    chat.setType("private");
    chat.setFirstName("Vova");
    chat.setLastName("Pupkin");

    Message message = new Message();
    message.setMessageId(100500);
    message.setDate(1615311730);
    message.setChat(chat);

    return message;
  }

  public static User getUser() {
    return GenericBuilder.of(User::new)
        .with(User::setId, 100)
        .build();
  }

  public static List<Question> getListOfQuestion() {
    return Arrays.asList(
        GenericBuilder.of(Question::new)
            .with(Question::setId, 700L)
            .with(Question::setLevel, Level.JUNIOR)
            .with(Question::setCategory, Category.OOP)
            .with(Question::setSmallDescription, "Test 1")
            .build(),
        GenericBuilder.of(Question::new)
            .with(Question::setId, 800L)
            .with(Question::setLevel, Level.JUNIOR)
            .with(Question::setCategory, Category.COLLECTIONS)
            .with(Question::setSmallDescription, "Test 2")
            .build());
  }

  public static Question getQuestion() {
    return GenericBuilder.of(Question::new)
        .with(Question::setLevel, Level.JUNIOR)
        .with(Question::setCategory, Category.COLLECTIONS)
        .with(Question::setSmallDescription, "Test 3")
        .build();
  }

  public static Question getQuestionWithIdAndImage() {
    Question question = getQuestion();
    question.setId(999L);
    question.setImage(new Binary(new byte[]{4,5,6,7,8,9,0}));
    return question;
  }
}

package org.neustupov.javadevinterviewbot.botapi.handlers.lists;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.botapi.handlers.InputMessageHandler;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.cache.UserDataCache;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.model.UserContext;
import org.neustupov.javadevinterviewbot.repository.CategoryRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryListsHandler implements InputMessageHandler {

  UserDataCache userDataCache;
  CategoryRepository categoryRepository;

  public CategoryListsHandler(UserDataCache userDataCache,
      CategoryRepository categoryRepository) {
    this.userDataCache = userDataCache;
    this.categoryRepository = categoryRepository;
  }

  @Override
  public SendMessage handle(Message message) {
    long chatId = message.getChatId();
    int userId = message.getFrom().getId();
    UserContext userContext = userDataCache.getUserContext(userId);
    List<Question> qList =  categoryRepository.getAllQuestionsByCategory(userContext.getCategory());
    StringBuffer sb = new StringBuffer();
    qList.forEach(q-> {
      sb.append(q.getLink());
      sb.append(" ");
      sb.append(q.getSmallDescription());
      sb.append("\n");
    });
    return new SendMessage(chatId, sb.toString());
  }

  @Override
  public BotState getHandlerName() {
    return BotState.SHOW_CATEGORY;
  }

  private String parseToJson(Object object){
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    String json = "";
    try {
      json = ow.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return json;
  }
}

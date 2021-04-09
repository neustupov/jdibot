package org.neustupov.javadevinterviewbot.botapi.messagecreator;

import static org.neustupov.javadevinterviewbot.botapi.handlers.filldata.PaginationHandler.Pagination.NEXT;
import static org.neustupov.javadevinterviewbot.botapi.handlers.filldata.PaginationHandler.Pagination.PREVIOUS;

import java.util.List;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.model.RangePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Сервис, отвечающий за расчет пагинации
 */
@Component
@NoArgsConstructor
public class PaginationService {

  @Value("${app.pagination}")
  private Integer maxObjects;

  /**
   * Выбирает из списка вопроса те, что попадают в текщий диапазон отображения
   *
   * @param qList Список вопросов
   * @param rangePair Диапазон
   * @return Актуальный список вопросов, соответствующий диапазону
   */
  List<Question> getCurrentList(List<Question> qList, RangePair rangePair) {
    return qList.stream()
        .skip(rangePair.getFrom())
        .limit(rangePair.getTo() - rangePair.getFrom()).collect(Collectors.toList());
  }

  /**
   * Вычисляет новый диапазон для пагинации
   *
   * @param listSize Размер списка
   * @param rangePair Предыдущий диапазон
   * @param pagination Направление смещения
   * @return Новый диапазон
   */
  RangePair getNewRange(Integer listSize, RangePair rangePair, String pagination) {
    int from = rangePair.getFrom();
    int to = rangePair.getTo();
    switch (pagination) {
      case NEXT:
        from = from + maxObjects;
        int tempTo = to + maxObjects;
        if (tempTo <= listSize) {
          to = tempTo;
        } else {
          to = listSize;
        }
        break;
      case PREVIOUS:
        int tempFrom = from - maxObjects;
        if (tempFrom >= 0) {
          from = tempFrom;
        } else {
          from = 0;
        }
        to = from + maxObjects;
        break;
    }
    return RangePair.builder().from(from).to(to).build();
  }

  /**
   * Определяет необходимость отображения кнопки ->
   *
   * @param qList Список вопросов
   * @param range Диапазон
   * @return boolean
   */
  boolean addNextButton(List<Question> qList, RangePair range) {
    return qList != null && ((range.getTo() != null && qList.size() > range.getTo()));
  }

  /**
   * Определяет необходимость отображения кнопки <-
   *
   * @param range Диапазон
   * @return boolean
   */
  boolean addPreviousButton(RangePair range) {
    return range.getFrom() != null && range.getFrom() > 0;
  }
}

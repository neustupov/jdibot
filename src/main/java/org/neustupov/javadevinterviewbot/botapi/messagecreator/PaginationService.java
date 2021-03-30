package org.neustupov.javadevinterviewbot.botapi.messagecreator;

import static org.neustupov.javadevinterviewbot.botapi.handlers.filldata.PaginationHandler.Pagination.NEXT;
import static org.neustupov.javadevinterviewbot.botapi.handlers.filldata.PaginationHandler.Pagination.PREVIOUS;

import java.util.List;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import org.neustupov.javadevinterviewbot.cache.DataCache;
import org.neustupov.javadevinterviewbot.model.GenericBuilder;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.model.RangePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class PaginationService {

  @Value("${app.search.pagination.max}")
  private Integer maxObjects;

  List<Question> getCurrentList(List<Question> qList, RangePair rangePair) {
    return qList.stream()
        .skip(rangePair.getFrom())
        .limit(rangePair.getTo() - rangePair.getFrom()).collect(Collectors.toList());
  }

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
    return GenericBuilder.of(RangePair::new)
        .with(RangePair::setFrom, from)
        .with(RangePair::setTo, to).build();
  }

  boolean addNextButton(List<Question> qList, RangePair newRange) {
    return qList != null && ((newRange.getTo() != null && qList.size() > newRange.getTo()));
  }

  boolean addPreviousButton(RangePair newRange) {
    return newRange.getFrom() != null && newRange.getFrom() > 0;
  }
}

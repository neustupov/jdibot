package org.neustupov.javadevinterviewbot.botapi.messagecreator;

import static org.junit.jupiter.api.Assertions.*;
import static org.neustupov.javadevinterviewbot.TestData.getListOfQuestion;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neustupov.javadevinterviewbot.model.GenericBuilder;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.model.RangePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class PaginationServiceTest {

  @Autowired
  private PaginationService paginationService;

  private List<Question> qList;
  private RangePair rangePair;

  @BeforeEach
  void setUp() {
    qList = getListOfQuestion();

    rangePair = GenericBuilder.of(RangePair::new)
        .with(RangePair::setFrom, 0)
        .with(RangePair::setTo, 1)
        .build();
  }

  @Test
  void getCurrentList() {
    List<Question> resultList = paginationService.getCurrentList(qList, rangePair);
    assertEquals(resultList.size(), 1);
  }

  @Test
  void getNewRange() {
    RangePair resultRangePairNext = paginationService.getNewRange(10, rangePair, "next");
    assertEquals(resultRangePairNext.getFrom(), 1);
    assertEquals(resultRangePairNext.getTo(), 2);

    RangePair rangePairPrev = GenericBuilder.of(RangePair::new)
        .with(RangePair::setFrom, 5)
        .with(RangePair::setTo, 6)
        .build();
    RangePair resultRangePairPrev = paginationService.getNewRange(10, rangePairPrev, "previous");
    assertEquals(resultRangePairPrev.getFrom(), 4);
    assertEquals(resultRangePairPrev.getTo(), 5);
  }

  @Test
  void addNextButton() {
    assertTrue(paginationService.addNextButton(qList, rangePair));

    RangePair rp = GenericBuilder.of(RangePair::new)
        .with(RangePair::setFrom, 1)
        .with(RangePair::setTo, 2)
        .build();

    assertFalse(paginationService.addNextButton(qList, rp));
  }

  @Test
  void addPreviousButton() {
    assertFalse(paginationService.addPreviousButton(rangePair));

    RangePair rp = GenericBuilder.of(RangePair::new)
        .with(RangePair::setFrom, 1)
        .with(RangePair::setTo, 2)
        .build();

    assertTrue(paginationService.addPreviousButton(rp));
  }
}
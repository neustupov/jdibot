package org.neustupov.javadevinterviewbot.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.neustupov.javadevinterviewbot.botapi.states.Category;
import org.neustupov.javadevinterviewbot.botapi.states.Level;
import org.neustupov.javadevinterviewbot.model.GenericBuilder;
import org.neustupov.javadevinterviewbot.model.RangePair;
import org.neustupov.javadevinterviewbot.model.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataMongoTest
@ExtendWith(SpringExtension.class)
class UserContextRepositoryTest {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private UserContextRepository userContextRepository;

  @Test
  void saveTest(){
    RangePair rangePairOne = GenericBuilder.of(RangePair::new)
        .with(RangePair::setFrom, 0)
        .with(RangePair::setTo, 2)
        .build();

    UserContext userContextOne = GenericBuilder.of(UserContext::new)
        .with(UserContext::setUserId, 666L)
        .with(UserContext::setLevel, Level.JUNIOR)
        .with(UserContext::setCategory, Category.OOP)
        .with(UserContext::setSearchField, "oop")
        .with(UserContext::setRange, rangePairOne)
        .with(UserContext::setRoute, "next")
        .build();

    this.mongoTemplate.save(userContextOne);
    List<UserContext> contextList = this.userContextRepository.findAll();
    assertEquals(contextList.size(), 1);

    assertEquals(contextList.get(0).getUserId(), 666L);
    assertEquals(contextList.get(0).getLevel(), Level.JUNIOR);
    assertEquals(contextList.get(0).getCategory(), Category.OOP);
    assertEquals(contextList.get(0).getSearchField(), "oop");
    assertEquals(contextList.get(0).getRange(), rangePairOne);
    assertEquals(contextList.get(0).getRoute(), "next");
  }

  @Test
  void findByIdTest(){
    RangePair rangePairThree = GenericBuilder.of(RangePair::new)
        .with(RangePair::setFrom, 0)
        .with(RangePair::setTo, 2)
        .build();

    UserContext userContextThree = GenericBuilder.of(UserContext::new)
        .with(UserContext::setUserId, 888L)
        .with(UserContext::setLevel, Level.JUNIOR)
        .with(UserContext::setCategory, Category.OOP)
        .with(UserContext::setSearchField, "oop")
        .with(UserContext::setRange, rangePairThree)
        .with(UserContext::setRoute, "next")
        .build();

    UserContext userContextFour = GenericBuilder.of(UserContext::new)
        .with(UserContext::setUserId, 999L)
        .build();

    this.mongoTemplate.save(userContextThree);
    this.mongoTemplate.save(userContextFour);

    Optional<UserContext> context = this.userContextRepository.findById(888L);
    assertTrue(context.isPresent());
    UserContext uc = context.orElse(null);

    assertEquals(uc.getUserId(), 888L);
    assertEquals(uc.getLevel(), Level.JUNIOR);
    assertEquals(uc.getCategory(), Category.OOP);
    assertEquals(uc.getSearchField(), "oop");
    assertEquals(uc.getRange(), rangePairThree);
    assertEquals(uc.getRoute(), "next");

  }
}
package org.neustupov.javadevinterviewbot;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.neustupov.javadevinterviewbot.model.menu.Category;
import org.neustupov.javadevinterviewbot.model.menu.Level;
import org.neustupov.javadevinterviewbot.model.GenericBuilder;
import org.neustupov.javadevinterviewbot.model.Question;

public class QuestionAggregator implements ArgumentsAggregator {

  @Override
  public Object aggregateArguments(ArgumentsAccessor argumentsAccessor,
      ParameterContext parameterContext) throws ArgumentsAggregationException {

    return GenericBuilder.of(Question::new)
        .with(Question::setCategory, argumentsAccessor.get(0, Category.class))
        .with(Question::setLevel, argumentsAccessor.get(1, Level.class))
        .with(Question::setSmallDescription, argumentsAccessor.getString(2))
        .with(Question::setLargeDescription, argumentsAccessor.getString(3))
        .build();
  }
}

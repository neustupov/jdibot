package org.neustupov.javadevinterviewbot.model;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Билдер для классов, в которых нельзя по тем или иным причинам использовать билдер ломбока
 * @param <T>
 */
@FieldDefaults(makeFinal=true, level = AccessLevel.PRIVATE)
public class GenericBuilder<T>{

  Supplier<T> instantiator;

  List<Consumer<T>> instanceModifiers = new ArrayList<>();

  public GenericBuilder(Supplier<T> instantiator) {
    this.instantiator = instantiator;
  }

  public static <T> GenericBuilder<T> of(Supplier<T> instantiator) {
    return new GenericBuilder<>(instantiator);
  }

  public <U> GenericBuilder<T> with(BiConsumer<T, U> consumer, U value) {
    Consumer<T> c = instance -> consumer.accept(instance, value);
    instanceModifiers.add(c);
    return this;
  }

  public T build() {
    T value = instantiator.get();
    instanceModifiers.forEach(modifier -> modifier.accept(value));
    instanceModifiers.clear();
    return value;
  }

}

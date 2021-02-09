package org.neustupov.javadevinterviewbot.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.neustupov.javadevinterviewbot.botapi.states.Category;
import org.neustupov.javadevinterviewbot.botapi.states.Level;
import org.neustupov.javadevinterviewbot.model.Question;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepositoryInMemoryImpl implements CategoryRepository {

  private List<Question> qList = new ArrayList<>();

  {
    qList.add(Question.builder()
        .link("/q0000")
        .category(Category.OOP)
        .level(Level.JUNIOR)
        .smallDescription("Назовите принципы ООП и расскажите о каждом")
        .largeDescription(
            "Объе́ктно-ориенти́рованное программи́рование (ООП) — это методология программирования, основанная на представлении программы в виде совокупности объектов, каждый из которых "
                + "является экземпляром определенного класса, а классы образуют иерархию наследования."
                + "Основные принципы ООП: абстракция, инкапсуляция, наследование, полиморфизм."
                + "Абстракция –  означает выделение значимой информации и исключение из рассмотрения незначимой. С точки зрения программирования это правильное разделение программы на объекты. "
                + "Абстракция позволяет отобрать главные характеристики и опустить второстепенные."
                + "Пример: описание должностей в компании. Здесь название должности значимая информация, а описание обязанностей у каждой должности это второстепенная информация. К примеру главной "
                + "характеристикой для “директор” будет то, что это должность чем-то управляет, а чем именно (директор по персоналу, финансовый директор, исполнительный директор) это уже второстепенная "
                + "информация."
                + "Инкапсуляция – свойство системы, позволяющее объединить данные и методы, работающие с ними, в классе. Для Java корректно будет говорить, что инкапсуляция это “сокрытие реализации”. "
                + "Пример из жизни – пульт от телевизора. Мы нажимаем кнопочку “увеличить громкость” и она увеличивается, но в этот момент происходят десятки процессов, которые скрыты от нас. Для Java: "
                + "можно создать класс с 10 методами, например вычисляющие площадь сложной фигуры, но сделать из них 9 private. 10й метод будет называться “вычислитьПлощадь()” и объявлен public, а в "
                + "нем уже будут вызываться необходимые скрытые от пользователя методы. Именно его и будет вызывать пользователь."
                + "Наследование – свойство системы, позволяющее описать новый класс на основе уже существующего с частично или полностью заимствующейся функциональностью. Класс, от которого "
                + "производится наследование, называется базовым, родительским или суперклассом. Новый класс — потомком, наследником, дочерним или производным классом."
                + "Полиморфизм – свойство системы использовать объекты с одинаковым интерфейсом без информации о типе и внутренней структуре объекта")
        .build());
    qList.add(Question.builder()
        .link("/q0001")
        .category(Category.OOP)
        .level(Level.JUNIOR)
        .smallDescription("Дайте определение понятию “класс”")
        .largeDescription(
            "Класс – это описатель общих свойств группы объектов. Этими свойствами могут быть как характеристики объектов (размер, вес, цвет и т.п.), так и поведения, роли и т.п.")
        .build());
    qList.add(Question.builder()
        .link("/q0002")
        .level(Level.JUNIOR)
        .category(Category.COLLECTIONS)
        .smallDescription("Дайте определение понятию “коллекция”")
        .largeDescription(
            "Коллекциями/контейнерами в Java принято называть классы, основная цель которых – хранить набор других элементов.")
        .build());
    qList.add(Question.builder()
        .link("/q0003")
        .level(Level.JUNIOR)
        .category(Category.COLLECTIONS)
        .smallDescription("Назовите преимущества использования коллекций")
        .largeDescription(
            "Массивы обладают значительными недостатками. Одним из них является конечный размер массива, как следствие, необходимость следить за размером массива. Другим — индексная "
                + "адресация, что не всегда удобно, т.к. ограничивает возможности добавления и удаления объектов. Чтобы избавиться от этих недостатков уже несколько десятилетий программисты используют "
                + "рекурсивные типы данных, такие как списки и деревья. Стандартный набор коллекций Java служит для избавления программиста от необходимости самостоятельно реализовывать эти типы данных "
                + "и снабжает его дополнительными возможностями.")
        .build());
    qList.add(Question.builder()
        .link("/q0004")
        .category(Category.OOP)
        .level(Level.JUNIOR)
        .smallDescription("Тест")
        .largeDescription(
            "Тест тест")
        .build());
    qList.add(Question.builder()
        .link("/q0005")
        .category(Category.OOP)
        .level(Level.JUNIOR)
        .smallDescription("Тест1")
        .largeDescription(
            "Тест1 тест")
        .build());
    qList.add(Question.builder()
        .link("/q0006")
        .category(Category.OOP)
        .level(Level.JUNIOR)
        .smallDescription("Тест2")
        .largeDescription(
            "Тест2 тест")
        .build());
  }

  @Override
  public List<Question> getAllQuestionsByCategoryAndLevel(Category category, Level level) {
    return qList.stream()
        .filter(q -> q.getCategory().equals(category) && q.getLevel().equals(level))
        .collect(Collectors.toList());
  }

  @Override
  public Question getQuestionByLink(String link) {
    return qList.stream().filter(q -> q.getLink().equals(link)).findAny().get();
  }

  @Override
  public List<Question> search(String searchString) {
    return qList.stream()
        .filter(q -> q.getSmallDescription().toLowerCase().contains(searchString.toLowerCase()))
        .collect(Collectors.toList());
  }
}

package org.neustupov.javadevinterviewbot.repository;

import org.neustupov.javadevinterviewbot.model.MessageIdStorage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Репозиторий MessageIdStorage
 */
public interface MessageIdStorageRepository extends MongoRepository<MessageIdStorage, Long> {

}

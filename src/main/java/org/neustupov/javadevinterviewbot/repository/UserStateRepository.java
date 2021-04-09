package org.neustupov.javadevinterviewbot.repository;

import org.neustupov.javadevinterviewbot.model.UserState;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Репозиторий UserState
 */
public interface UserStateRepository extends MongoRepository<UserState, Long> {

}

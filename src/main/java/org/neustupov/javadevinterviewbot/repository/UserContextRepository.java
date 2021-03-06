package org.neustupov.javadevinterviewbot.repository;

import org.neustupov.javadevinterviewbot.model.UserContext;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Репозиторий UserContext
 */
public interface UserContextRepository extends MongoRepository<UserContext, Long> {

}

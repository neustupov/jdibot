package org.neustupov.javadevinterviewbot.repository;

import org.neustupov.javadevinterviewbot.model.MessageIdKeeper;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageIdKeeperRepository extends MongoRepository<MessageIdKeeper, Long> {

}

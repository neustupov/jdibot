package org.neustupov.javadevinterviewbot.service;

import java.util.Optional;
import org.neustupov.javadevinterviewbot.model.GenericBuilder;
import org.neustupov.javadevinterviewbot.model.MessageIdKeeper;
import org.neustupov.javadevinterviewbot.repository.MessageIdKeeperRepository;
import org.springframework.stereotype.Service;

@Service
public class MessageIdKeeperServiceImpl implements MessageIdKeeperService {

  private MessageIdKeeperRepository messageIdKeeperRepository;

  public MessageIdKeeperServiceImpl(
      MessageIdKeeperRepository messageIdKeeperRepository) {
    this.messageIdKeeperRepository = messageIdKeeperRepository;
  }

  @Override
  public void save(MessageIdKeeper messageIdKeeper) {
    messageIdKeeperRepository.save(messageIdKeeper);
  }

  @Override
  public MessageIdKeeper getKeeperByChatId(long chatId) {
    Optional<MessageIdKeeper> messageIdKeeperOptional = messageIdKeeperRepository.findById(chatId);

    return messageIdKeeperOptional
        .orElseGet(() ->
            messageIdKeeperRepository.save(GenericBuilder.of(MessageIdKeeper::new)
                .with(MessageIdKeeper::setChatId, chatId)
                .build()));
  }
}

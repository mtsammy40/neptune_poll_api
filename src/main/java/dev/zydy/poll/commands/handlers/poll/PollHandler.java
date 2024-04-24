package dev.zydy.poll.commands.handlers.poll;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zydy.poll.commands.entity.Option;
import dev.zydy.poll.commands.entity.Poll;
import dev.zydy.poll.commands.events.PollCreatedEvent;
import dev.zydy.poll.commands.repository.OptionRepository;
import dev.zydy.poll.commands.repository.PollRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@ProcessingGroup("poll")
@RequiredArgsConstructor
public class PollHandler {
    private final PollRepository pollRepository;
    private final OptionRepository optionRepository;

    @EventHandler
    void on(PollCreatedEvent event) throws JsonProcessingException {
        Poll pollEntity = Poll.builder()
                .isPersisted(false)
                .id(event.id())
                .title(event.title())
                .description(event.description())
                .build();
        var options = event.options().stream().map(option -> Option.builder()
                        .isPersisted(false)
                        .id(option.id())
                        .pollId(pollEntity.getId())
                        .displayName(option.displayName())
                        .build())
                .toList();
        log.info("Creating poll: " + new ObjectMapper().writeValueAsString(pollEntity));
        pollRepository.save(pollEntity)
                .flatMapMany(poll -> optionRepository.saveAll(options))
                .onErrorMap(throwable -> new RuntimeException("Failed to save poll", throwable))
                .subscribe();

    }
}

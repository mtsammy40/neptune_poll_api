package dev.zydy.poll.commands.handlers.poll;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zydy.poll.commands.entity.Option;
import dev.zydy.poll.commands.entity.Poll;
import dev.zydy.poll.commands.events.PollCreatedEvent;
import dev.zydy.poll.commands.events.PollUpdatedEvent;
import dev.zydy.poll.commands.repository.OptionRepository;
import dev.zydy.poll.commands.repository.PollRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
@ProcessingGroup("poll")
@RequiredArgsConstructor
public class PollHandler {
    private final PollRepository pollRepository;
    private final OptionRepository optionRepository;

    @EventHandler
    void on(PollCreatedEvent event) throws JsonProcessingException {
        log.info("Handling poll created event: " + new ObjectMapper().writeValueAsString(event));
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

    @EventHandler
    void on(PollUpdatedEvent event) throws JsonProcessingException {
        log.info("Handling poll updated event: " + new ObjectMapper().writeValueAsString(event));
        var optionIds = event.options().stream()
                .map(dev.zydy.poll.commands.model.Option::id)
                .filter(Objects::nonNull)
                .map(UUID::toString)
                .toList();
        pollRepository.findById(event.id().toString())
                .map(poll -> {
                    poll.setTitle(event.title());
                    poll.setDescription(event.description());
                    return poll;
                })
                .flatMap(pollRepository::save)
                .flatMap(poll -> optionRepository.deleteAllById(optionIds))
                .thenMany(optionRepository.saveAll(event.options().stream()
                        .map(option -> Option.builder()
                                .isPersisted(false)
                                .id(option.id())
                                .pollId(event.id())
                                .displayName(option.displayName())
                                .build())
                        .toList()))
                .onErrorMap(throwable -> new RuntimeException("Failed to save poll", throwable))
                .subscribe();

    }
}

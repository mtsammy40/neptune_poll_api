package dev.zydy.poll.commands.aggregate;

import dev.zydy.poll.commands.command.CreatePollCommand;
import dev.zydy.poll.commands.command.UpdatePollCommand;
import dev.zydy.poll.commands.events.PollCreatedEvent;
import dev.zydy.poll.commands.events.PollUpdatedEvent;
import dev.zydy.poll.commands.model.Option;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Aggregate
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class Poll {
    @AggregateIdentifier
    private UUID id;
    private String title;
    private String description;
    private List<Option> options;

    @CommandHandler()
    public Poll(CreatePollCommand createPollCommand) {
        log.info("Handling create poll command {}", createPollCommand);
        validatePollData(createPollCommand.title(), createPollCommand.description(), createPollCommand.options());

        var optionsWithIds = createPollCommand.options().stream()
                .map(option -> new Option(UUID.randomUUID(), option.displayName()))
                .toList();

        AggregateLifecycle.apply(new PollCreatedEvent(
                UUID.fromString(createPollCommand.id()),
                createPollCommand.title(),
                createPollCommand.description(),
                optionsWithIds));

    }

    @CommandHandler
    public void handle(UpdatePollCommand updatePollCommand) {
        log.info("Handling update poll command {}", updatePollCommand);
        validatePollData(updatePollCommand.title(), updatePollCommand.description(), updatePollCommand.options());

        var optionsWithIds = updatePollCommand.options().stream()
                .map(option -> new Option(UUID.randomUUID(), option.displayName()))
                .toList();

        AggregateLifecycle.apply(new PollUpdatedEvent(
                UUID.fromString(updatePollCommand.id()),
                updatePollCommand.title(),
                updatePollCommand.description(),
                optionsWithIds));
    }

    @EventSourcingHandler
    public void on(PollCreatedEvent pollCreatedEvent) {
        id = pollCreatedEvent.id();
        title = pollCreatedEvent.title();
        description = pollCreatedEvent.description();
        options = pollCreatedEvent.options();
    }

    @EventSourcingHandler
    public void on(PollUpdatedEvent pollCreatedEvent) {
        title = pollCreatedEvent.title();
        description = pollCreatedEvent.description();
        options = pollCreatedEvent.options();
    }

    void validatePollData(String title, String description, List<Option> options) throws IllegalArgumentException {
        if (!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("Title is required");
        }
        if (!StringUtils.hasText(description)) {
            throw new IllegalArgumentException("Description is required");
        }
        if (options.isEmpty()) {
            throw new IllegalArgumentException("Options are required");
        }
        if (options.size() < 2) {
            throw new IllegalArgumentException("At least two options are required");
        }
        if (options.stream().anyMatch(option -> !StringUtils.hasText(option.displayName()))) {
            throw new IllegalArgumentException("Option display name is required");
        }
    }
}

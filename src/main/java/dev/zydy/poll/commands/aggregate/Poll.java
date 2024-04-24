package dev.zydy.poll.commands.aggregate;

import dev.zydy.poll.commands.command.CreatePollCommand;
import dev.zydy.poll.commands.events.PollCreatedEvent;
import dev.zydy.poll.commands.model.Option;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class Poll {
    @AggregateIdentifier
    private UUID id;
    private String title;
    private String description;
    private List<Option> options;

    @CommandHandler()
    public Poll(CreatePollCommand createPollCommand) {
        if (!StringUtils.hasText(createPollCommand.title())) {
            throw new IllegalArgumentException("Title is required");
        }
        if (!StringUtils.hasText(createPollCommand.description())) {
            throw new IllegalArgumentException("Description is required");
        }
        if (createPollCommand.options().isEmpty()) {
            throw new IllegalArgumentException("Options are required");
        }
        if (createPollCommand.options().size() < 2) {
            throw new IllegalArgumentException("At least two options are required");
        }
        if (createPollCommand.options().stream().anyMatch(option -> !StringUtils.hasText(option.displayName()))) {
            throw new IllegalArgumentException("Option display name is required");
        }

        var optionsWithIds = createPollCommand.options().stream()
                .map(option -> new Option(UUID.randomUUID(), option.displayName()))
                .toList();

        AggregateLifecycle.apply(new PollCreatedEvent(
                UUID.fromString(createPollCommand.id()),
                createPollCommand.title(),
                createPollCommand.description(),
                optionsWithIds));

    }

    @EventSourcingHandler
    public void on(PollCreatedEvent pollCreatedEvent) {
        id = pollCreatedEvent.id();
        title = pollCreatedEvent.title();
        description = pollCreatedEvent.description();
        options = pollCreatedEvent.options();
    }
}

package dev.zydy.poll.commands.events;

import dev.zydy.poll.commands.model.Option;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.List;
import java.util.UUID;

public record PollCreatedEvent(
        @TargetAggregateIdentifier
        UUID id,
        String title,
        String description,
        List<Option> options
) {
}
